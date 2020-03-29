package cc.devcp.project.auth.security.roles;


import cc.devcp.project.auth.security.auth.AuthConfigs;
import cc.devcp.project.auth.security.auth.Permission;
import cc.devcp.project.common.model.page.PageParam;
import cc.devcp.project.auth.server.auth.PermissionInfo;
import cc.devcp.project.auth.server.auth.PermissionPersistService;
import cc.devcp.project.auth.server.auth.RoleInfo;
import cc.devcp.project.auth.server.auth.RolePersistService;
import cc.devcp.project.auth.security.AppAuthConfig;
import cc.devcp.project.auth.security.users.AppUserDetailsServiceImpl;
import cc.devcp.project.core.utils.Loggers;
import cc.devcp.project.provider.model.Page;
import cn.hutool.core.collection.ConcurrentHashSet;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Nacos builtin role service.
 *
 * @author nkorange
 * @since 1.2.0
 */
@Service
public class AppRoleServiceImpl {

    public static final String GLOBAL_ADMIN_ROLE = "ROLE_ADMIN";

    @Autowired
    private AuthConfigs authConfigs;

    @Autowired
    private RolePersistService rolePersistService;

    @Autowired
    private AppUserDetailsServiceImpl userDetailsService;

    @Autowired
    private PermissionPersistService permissionPersistService;

    private Set<String> roleSet = new ConcurrentHashSet<>();

    private Map<String, List<RoleInfo>> roleInfoMap = new ConcurrentHashMap<>();

    private Map<String, List<PermissionInfo>> permissionInfoMap = new ConcurrentHashMap<>();

    @Scheduled(initialDelay = 5000, fixedDelay = 15000)
    private void reload() {
        try {
            Page<RoleInfo> roleInfoPage = rolePersistService.getRolesByUserName(StringUtils.EMPTY, 1, Integer.MAX_VALUE);
            if (roleInfoPage == null) {
                return;
            }
            Set<String> tmpRoleSet = new HashSet<>(16);
            Map<String, List<RoleInfo>> tmpRoleInfoMap = new ConcurrentHashMap<>(16);
            for (RoleInfo roleInfo : roleInfoPage.getPageItems()) {
                if (!tmpRoleInfoMap.containsKey(roleInfo.getUsername())) {
                    tmpRoleInfoMap.put(roleInfo.getUsername(), new ArrayList<>());
                }
                tmpRoleInfoMap.get(roleInfo.getUsername()).add(roleInfo);
                tmpRoleSet.add(roleInfo.getRole());
            }

            Map<String, List<PermissionInfo>> tmpPermissionInfoMap = new ConcurrentHashMap<>(16);
            for (String role : tmpRoleSet) {
                Page<PermissionInfo> permissionInfoPage = permissionPersistService.getPermissions(role, 1, Integer.MAX_VALUE);
                tmpPermissionInfoMap.put(role, permissionInfoPage.getPageItems());
            }

            roleSet = tmpRoleSet;
            roleInfoMap = tmpRoleInfoMap;
            permissionInfoMap = tmpPermissionInfoMap;
        } catch (Exception e) {
            Loggers.AUTH.warn("[LOAD-ROLES] load failed", e);
        }
    }

    /**
     * Determine if the user has permission of the resource.
     * <p>
     * Note if the user has many roles, this method returns true if any one role of the user has the
     * desired permission.
     *
     * @param username   user info
     * @param permission permission to auth
     * @return true if granted, false otherwise
     */
    public boolean hasPermission(String username, Permission permission) {

        List<RoleInfo> roleInfoList = getRoles(username);
        if (Collections.isEmpty(roleInfoList)) {
            return false;
        }

        // Global admin pass:
        for (RoleInfo roleInfo : roleInfoList) {
            if (GLOBAL_ADMIN_ROLE.equals(roleInfo.getRole())) {
                return true;
            }
        }

        // Old global admin can pass resource 'console/':
        if (permission.getResource().startsWith(AppAuthConfig.CONSOLE_RESOURCE_NAME_PREFIX)) {
            return false;
        }

        // For other roles, use a pattern match to decide if pass or not.
        for (RoleInfo roleInfo : roleInfoList) {
            List<PermissionInfo> permissionInfoList = getPermissions(roleInfo.getRole());
            if (Collections.isEmpty(permissionInfoList)) {
                continue;
            }
            for (PermissionInfo permissionInfo : permissionInfoList) {
                String permissionResource = permissionInfo.getResource().replaceAll("\\*", ".*");
                String permissionAction = permissionInfo.getAction();
                if (permissionAction.contains(permission.getAction()) &&
                    Pattern.matches(permissionResource, permission.getResource())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<RoleInfo> getRoles(String username) {
        List<RoleInfo> roleInfoList = roleInfoMap.get(username);
        if (!authConfigs.isCachingEnabled()) {
            Page<RoleInfo> roleInfoPage = getRolesFromDatabase(username, 1, Integer.MAX_VALUE);
            if (roleInfoPage != null) {
                roleInfoList = roleInfoPage.getPageItems();
            }
        }
        return roleInfoList;
    }

    public Page<RoleInfo> getRolesFromDatabase(String userName, int pageNo, int pageSize) {
        Page<RoleInfo> roles = rolePersistService.getRolesByUserName(userName, pageNo, pageSize);
        if (roles == null) {
            return new Page<>();
        }
        return roles;
    }

    public List<PermissionInfo> getPermissions(String role) {
        List<PermissionInfo> permissionInfoList = permissionInfoMap.get(role);
        if (!authConfigs.isCachingEnabled()) {
            PageParam pageParam = PageParam.of(1, Integer.MAX_VALUE);
            Page<PermissionInfo> permissionInfoPage = getPermissionsFromDatabase(role, pageParam);
            if (permissionInfoPage != null) {
                permissionInfoList = permissionInfoPage.getPageItems();
            }
        }
        return permissionInfoList;
    }

    public Page<PermissionInfo> getPermissionsByRoleFromDatabase(String role, int pageNo, int pageSize) {
        return permissionPersistService.getPermissions(role, pageNo, pageSize);
    }

    public void addRole(String role, String username) {
        if (userDetailsService.getUser(username) == null) {
            throw new IllegalArgumentException("user '" + username + "' not found!");
        }
        if (GLOBAL_ADMIN_ROLE.equals(role)) {
            throw new IllegalArgumentException("role '" + GLOBAL_ADMIN_ROLE + "' is not permitted to create!");
        }
        rolePersistService.addRole(role, username);
        roleSet.add(role);
    }

    public void deleteRole(String role, String userName) {
        rolePersistService.deleteRole(role, userName);
    }

    public void deleteRole(String role) {
        rolePersistService.deleteRole(role);
        roleSet.remove(role);
    }

    public Page<PermissionInfo> getPermissionsFromDatabase(String role, PageParam pageParam) {
        int pageNo = pageParam.getCurrent();
        int pageSize = pageParam.getSize();
        Page<PermissionInfo> pageInfo = permissionPersistService.getPermissions(role, pageNo, pageSize);
        if (pageInfo == null) {
            return new Page<>();
        }
        return pageInfo;
    }

    public void addPermission(String role, String resource, String action) {
        if (!roleSet.contains(role)) {
            throw new IllegalArgumentException("role " + role + " not found!");
        }
        permissionPersistService.addPermission(role, resource, action);
    }

    public void deletePermission(String role, String resource, String action) {
        permissionPersistService.deletePermission(role, resource, action);
    }
}
