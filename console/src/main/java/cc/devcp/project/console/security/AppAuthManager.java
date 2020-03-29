package cc.devcp.project.console.security;

import cc.devcp.project.common.constant.CommonConst;
import cc.devcp.project.config.server.auth.RoleInfo;
import cc.devcp.project.console.security.roles.AppRoleServiceImpl;
import cc.devcp.project.console.security.users.AppUser;
import cc.devcp.project.core.auth.AccessException;
import cc.devcp.project.core.auth.AuthManager;
import cc.devcp.project.core.auth.Permission;
import cc.devcp.project.core.auth.User;
import cc.devcp.project.core.utils.Loggers;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Builtin access control entry of Nacos
 *
 * @author nkorange
 * @since 1.2.0
 */
@Component
public class AppAuthManager implements AuthManager {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private JwtTokenManager tokenManager;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppRoleServiceImpl roleService;

    @Override
    public User login(Object request) throws AccessException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = resolveToken(req);
        if (StringUtils.isBlank(token)) {
            throw new AccessException("user not found!");
        }

        try {
            tokenManager.validateToken(token);
        } catch (ExpiredJwtException e) {
            throw new AccessException("token expired!");
        } catch (Exception e) {
            throw new AccessException("token invalid!");
        }

        Authentication authentication = tokenManager.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String username = authentication.getName();
        AppUser user = new AppUser();
        user.setUserName(username);
        user.setToken(token);
        List<RoleInfo> roleInfoList = roleService.getRoles(username);
        if (roleInfoList != null) {
            for (RoleInfo roleInfo : roleInfoList) {
                if (roleInfo.getRole().equals(AppRoleServiceImpl.GLOBAL_ADMIN_ROLE)) {
                    user.setGlobalAdmin(true);
                    break;
                }
            }
        }

        return user;
    }

    @Override
    public void auth(Permission permission, User user) throws AccessException {
        if (Loggers.AUTH.isDebugEnabled()) {
            Loggers.AUTH.debug("auth permission: {}, user: {}", permission, user);
        }

        if (!roleService.hasPermission(user.getUserName(), permission)) {
            throw new AccessException("authorization failed!");
        }
    }

    /**
     * Get token from header
     */
    private String resolveToken(HttpServletRequest request) throws AccessException {
        String bearerToken = request.getHeader(AppAuthConfig.AUTHORIZATION_HEADER);
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        bearerToken = request.getParameter(CommonConst.ACCESS_TOKEN);
        if (StringUtils.isBlank(bearerToken)) {
            String userName = request.getParameter("username");
            String password = request.getParameter("password");
            bearerToken = resolveTokenFromUser(userName, password);
        }

        return bearerToken;
    }

    private String resolveTokenFromUser(String userName, String rawPassword) throws AccessException {

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, rawPassword);
            authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            throw new AccessException("unknown user!");
        }

        return tokenManager.createToken(userName);
    }
}
