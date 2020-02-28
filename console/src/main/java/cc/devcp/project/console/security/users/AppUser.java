package cc.devcp.project.console.security.users;

import com.alibaba.fastjson.JSON;
import cc.devcp.project.core.auth.User;

/**
 * @author nkorange
 * @since 1.2.0
 */
public class AppUser extends User {

    private String token;

    private boolean globalAdmin = false;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isGlobalAdmin() {
        return globalAdmin;
    }

    public void setGlobalAdmin(boolean globalAdmin) {
        this.globalAdmin = globalAdmin;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
