package cc.devcp.project.core.auth;

/**
 * Resource action type definitions
 *
 * @author nkorange
 * @since 1.2.0
 */
public enum ActionTypes {
    /**
     * Read
     */
    READ("r"),
    /**
     * Write
     */
    WRITE("w");

    private String action;

    ActionTypes(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return action;
    }
}
