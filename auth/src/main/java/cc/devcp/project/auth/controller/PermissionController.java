package cc.devcp.project.auth.controller;


import cc.devcp.project.auth.security.AppAuthConfig;
import cc.devcp.project.auth.security.auth.ActionTypes;
import cc.devcp.project.auth.security.auth.Secured;
import cc.devcp.project.auth.security.roles.AppRoleServiceImpl;
import cc.devcp.project.common.model.page.PageParam;
import cc.devcp.project.common.model.result.RestResult;
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
    public Object getPermissions(@RequestParam(required = false) Integer pageNo,
                                 @RequestParam(required = false) Integer pageSize,
                                 @RequestParam(name = "role", defaultValue = StringUtils.EMPTY) String role) {
        PageParam pageParam = PageParam.of(pageNo, pageSize);
        return nacosRoleService.getPermissionsFromDatabase(role, pageParam);
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