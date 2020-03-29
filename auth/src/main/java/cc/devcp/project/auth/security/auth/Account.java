package cc.devcp.project.auth.security.auth;


/**
 * User information in authorization.
 *
 * @author nkorange
 * @since 1.2.0
 */
public class Account {

    /**
     * Unique string representing user
     */
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
