package cc.devcp.project.console.module.def.controller;


import cc.devcp.project.common.result.RestResult;
import cc.devcp.project.console.security.AppAuthConfig;
import cc.devcp.project.console.security.roles.AppRoleServiceImpl;
import cc.devcp.project.core.auth.ActionTypes;
import cc.devcp.project.core.auth.Secured;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Permission operation controller
 *
 * @author nkorange
 * @since 1.2.0
 */
@RestController
@RequestMapping("/v1/auth/permissions")
public class PermissionController {

    @Autowired
    private AppRoleServiceImpl nacosRoleService;

    /**
     * Query permissions of a role
     *
     * @param role     the role
     * @param pageNo   page index
     * @param pageSize page size
     * @return permission of a role
     */
    @GetMapping
    @Secured(resource = AppAuthConfig.CONSOLE_RESOURCE_NAME_PREFIX + "permissions", action = ActionTypes.READ)
    public Object getPermissions(@RequestParam int pageNo, @RequestParam int pageSize,
                                 @RequestParam(name = "role", defaultValue = StringUtils.EMPTY) String role) {
        return nacosRoleService.getPermissionsFromDatabase(role, pageNo, pageSize);
    }

    /**
     * Add a permission to a role
     *
     * @param role       the role
     * @param resource the related resource
     * @param action the related action
     * @return ok if succeed
     */
    @PostMapping
    @Secured(resource = AppAuthConfig.CONSOLE_RESOURCE_NAME_PREFIX + "permissions", action = ActionTypes.WRITE)
    public Object addPermission(@RequestParam String role, @RequestParam String resource, @RequestParam String action) {
        nacosRoleService.addPermission(role, resource, action);
        return new RestResult<>(200, "add permission ok!");
    }

    /**
     * Delete a permission from a role
     *
     * @param role       the role
     * @param resource the related resource
     * @param action the related action
     * @return ok if succeed
     */
    @DeleteMapping
    @Secured(resource = AppAuthConfig.CONSOLE_RESOURCE_NAME_PREFIX + "permissions", action = ActionTypes.WRITE)
    public Object deletePermission(@RequestParam String role, @RequestParam String resource, @RequestParam String action) {
        nacosRoleService.deletePermission(role, resource, action);
        return new RestResult<>(200, "delete permission ok!");
    }
}
