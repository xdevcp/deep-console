package cc.devcp.project.auth.security.users;

import cc.devcp.project.auth.security.auth.Account;
import com.alibaba.fastjson.JSON;

/**
 * @author nkorange
 * @since 1.2.0
 */
public class AppUser extends Account {

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
