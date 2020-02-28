package cc.devcp.project.console.module.def.controller;


import cc.devcp.project.config.server.model.RestResult;
import cc.devcp.project.console.security.AppAuthConfig;
import cc.devcp.project.console.security.roles.AppRoleServiceImpl;
import cc.devcp.project.core.auth.ActionTypes;
import cc.devcp.project.core.auth.Secured;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Role operation controller
 *
 * @author nkorange
 * @since 1.2.0
 */
@RestController
@RequestMapping("/v1/auth/roles")
public class RoleController {

    @Autowired
    private AppRoleServiceImpl roleService;

    /**
     * Get roles list
     *
     * @param pageNo   number index of page
     * @param pageSize page size
     * @param username optional, username of user
     * @return role list
     */
    @GetMapping
    @Secured(resource = AppAuthConfig.CONSOLE_RESOURCE_NAME_PREFIX + "roles", action = ActionTypes.READ)
    public Object getRoles(@RequestParam int pageNo, @RequestParam int pageSize,
                           @RequestParam(name = "username", defaultValue = "") String username) {
        return roleService.getRolesFromDatabase(username, pageNo, pageSize);
    }

    /**
     * Add a role to a user
     * <p>
     * This method is used for 2 functions:
     * 1. create a role and bind it to GLOBAL_ADMIN.
     * 2. bind a role to an user.
     *
     * @param role
     * @param username
     * @return
     */
    @PostMapping
    @Secured(resource = AppAuthConfig.CONSOLE_RESOURCE_NAME_PREFIX + "roles", action = ActionTypes.WRITE)
    public Object addRole(@RequestParam String role, @RequestParam String username) {
        roleService.addRole(role, username);
        return new RestResult<>(200, "add role ok!");
    }

    /**
     * Delete a role. If no username is specified, all users under this role are deleted
     *
     * @param role     role
     * @param username username
     * @return ok if succeed
     */
    @DeleteMapping
    @Secured(resource = AppAuthConfig.CONSOLE_RESOURCE_NAME_PREFIX + "roles", action = ActionTypes.WRITE)
    public Object deleteRole(@RequestParam String role,
                             @RequestParam(name = "username", defaultValue = StringUtils.EMPTY) String username) {
        if (StringUtils.isBlank(username)) {
            roleService.deleteRole(role);
        } else {
            roleService.deleteRole(role, username);
        }
        return new RestResult<>(200, "delete role of user " + username + " ok!");
    }

}
