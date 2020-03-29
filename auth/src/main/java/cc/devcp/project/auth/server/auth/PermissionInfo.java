package cc.devcp.project.auth.server.auth;

/**
 * @author nkorange
 * @since 1.2.0
 */
public class PermissionInfo {

    /**
     * Role name
     */
    private String role;

    /**
     * Resource
     */
    private String resource;

    /**
     * Action on resource
     */
    private String action;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
