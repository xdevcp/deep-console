package cc.devcp.project.console.security.roles;


import cc.devcp.project.config.server.auth.PermissionInfo;
import cc.devcp.project.config.server.auth.PermissionPersistService;
import cc.devcp.project.config.server.auth.RoleInfo;
import cc.devcp.project.config.server.auth.RolePersistService;
import cc.devcp.project.config.server.model.Page;
import cc.devcp.project.console.security.AppAuthConfig;
import cc.devcp.project.console.security.users.AppUserDetailsServiceImpl;
import cc.devcp.project.core.auth.AuthConfigs;
import cc.devcp.project.core.auth.Permission;
import cc.devcp.project.core.utils.Loggers;
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

    public static final String GLOBAL_ADMIN_ROLE = "GLOBAL_ADMIN";

    @Autowired
    private AuthConfigs authConfigs;

    @Autowired
    private RolePersistService rolePersistService;

    @Autowired
    private AppUserDetailsServiceImpl userDetailsService;

    @Autowired
    private PermissionPersistService permissionPersistService;

    private Map<String, List<RoleInfo>> roleInfoMap = new ConcurrentHashMap<>();

    private Map<String, List<PermissionInfo>> permissionInfoMap = new ConcurrentHashMap<>();

    @Scheduled(initialDelay = 5000, fixedDelay = 15000)
    private void reload() {
        try {
            Page<RoleInfo> roleInfoPage = rolePersistService.getRolesByUserName(StringUtils.EMPTY, 1, Integer.MAX_VALUE);
            if (roleInfoPage == null) {
                return;
            }
            Set<String> roleSet = new HashSet<>(16);
            Map<String, List<RoleInfo>> tmpRoleInfoMap = new ConcurrentHashMap<>(16);
            for (RoleInfo roleInfo : roleInfoPage.getPageItems()) {
                if (!tmpRoleInfoMap.containsKey(roleInfo.getUsername())) {
                    tmpRoleInfoMap.put(roleInfo.getUsername(), new ArrayList<>());
                }
                tmpRoleInfoMap.get(roleInfo.getUsername()).add(roleInfo);
                roleSet.add(roleInfo.getRole());
            }

            Map<String, List<PermissionInfo>> tmpPermissionInfoMap = new ConcurrentHashMap<>(16);
            for (String role : roleSet) {
                Page<PermissionInfo> permissionInfoPage = permissionPersistService.getPermissions(role, 1, Integer.MAX_VALUE);
                tmpPermissionInfoMap.put(role, permissionInfoPage.getPageItems());
            }

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
            Page<PermissionInfo> permissionInfoPage = getPermissionsFromDatabase(role, 1, Integer.MAX_VALUE);
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
        rolePersistService.addRole(role, username);
    }

    public void deleteRole(String role, String userName) {
        rolePersistService.deleteRole(role, userName);
    }

    public void deleteRole(String role) {

        rolePersistService.deleteRole(role);
    }

    public Page<PermissionInfo> getPermissionsFromDatabase(String role, int pageNo, int pageSize) {
        Page<PermissionInfo> pageInfo = permissionPersistService.getPermissions(role, pageNo, pageSize);
        if (pageInfo == null) {
            return new Page<>();
        }
        return pageInfo;
    }

    public void addPermission(String role, String resource, String action) {
        permissionPersistService.addPermission(role, resource, action);
    }

    public void deletePermission(String role, String resource, String action) {
        permissionPersistService.deletePermission(role, resource, action);
    }
}
