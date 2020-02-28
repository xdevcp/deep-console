package cc.devcp.project.core.auth;

/**
 * Permission to auth
 *
 * @author nkorange
 * @since 1.2.0
 */
public class Permission {

    public Permission() {

    }

    public Permission(String resource, String action) {
        this.resource = resource;
        this.action = action;
    }

    /**
     * An unique key of resource
     */
    private String resource;

    /**
     * Action on resource, refer to class ActionTypes
     */
    private String action;

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
