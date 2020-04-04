package cc.devcp.project.auth.security.users;


import cc.devcp.project.auth.model.User;
import cc.devcp.project.auth.security.auth.AuthConfigs;
import cc.devcp.project.auth.server.auth.UserPersistService;
import cc.devcp.project.common.model.page.Page;
import cc.devcp.project.core.utils.Loggers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Custem user service
 *
 * @author wfnuser
 * @author nkorange
 */
@Service
public class AppUserDetailsServiceImpl implements UserDetailsService {

    private Map<String, User> userMap = new ConcurrentHashMap<>();

    @Autowired
    private UserPersistService userPersistService;

    @Autowired
    private AuthConfigs authConfigs;

    @Scheduled(initialDelay = 5000, fixedDelay = 15000)
    private void reload() {
        try {
            Page<User> users = getUsersFromDatabase(1, Integer.MAX_VALUE);
            if (users == null) {
                return;
            }

            Map<String, User> map = new ConcurrentHashMap<>(16);
            for (User user : users.getPageItems()) {
                map.put(user.getUsername(), user);
            }
            userMap = map;
        } catch (Exception e) {
            Loggers.AUTH.warn("[LOAD-USERS] load failed", e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMap.get(username);
        if (!authConfigs.isCachingEnabled()) {
            user = userPersistService.findUserByUsername(username);
        }

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new AppUserDetails(user);
    }

    public void updateUserPassword(String username, String password) {
        userPersistService.updateUserPassword(username, password);
    }

    public Page<User> getUsersFromDatabase(int pageNo, int pageSize) {
        return userPersistService.getUsers(pageNo, pageSize);
    }

    public User getUser(String username) {
        User user = userMap.get(username);
        if (!authConfigs.isCachingEnabled()) {
            user = getUserFromDatabase(username);
        }
        return user;
    }

    public User getUserFromDatabase(String username) {
        return userPersistService.findUserByUsername(username);
    }

    public void createUser(String username, String password) {
        userPersistService.createUser(username, password);
    }

    public void deleteUser(String username) {
        userPersistService.deleteUser(username);
    }
}
