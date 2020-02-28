package cc.devcp.project.core.auth;


/**
 * User information in authorization.
 *
 * @author nkorange
 * @since 1.2.0
 */
public class User {

    /**
     * Unique string representing user
     */
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
