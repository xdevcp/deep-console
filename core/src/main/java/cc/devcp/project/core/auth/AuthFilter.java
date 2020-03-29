package cc.devcp.project.core.auth;

import cc.devcp.project.core.code.ControllerMethodsCache;
import cc.devcp.project.core.utils.Constants;
import cc.devcp.project.core.utils.ExceptionUtil;
import cc.devcp.project.core.utils.Loggers;
import cc.devcp.project.core.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;

/**
 * Unified filter to handle authentication and authorization
 *
 * @author nkorange
 * @since 1.2.0
 */
public class AuthFilter implements Filter {

    @Autowired
    private AuthConfigs authConfigs;

    @Autowired
    private AuthManager authManager;

    @Autowired
    private ControllerMethodsCache methodsCache;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        if (!authConfigs.isAuthEnabled()) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String userAgent = WebUtils.getUserAgent(req);

        if (StringUtils.startsWith(userAgent, Constants.NACOS_SERVER_HEADER)) {
            chain.doFilter(req, resp);
            return;
        }

        try {

            String path = new URI(req.getRequestURI()).getPath();
            Method method = methodsCache.getMethod(req.getMethod(), path);

            if (method == null) {
                chain.doFilter(request, response);
                return;
            }

            if (method.isAnnotationPresent(Secured.class) && authConfigs.isAuthEnabled()) {

                if (Loggers.AUTH.isDebugEnabled()) {
                    Loggers.AUTH.debug("auth start, request: {} {}", req.getMethod(), req.getRequestURI());
                }

                Secured secured = method.getAnnotation(Secured.class);
                String action = secured.action().toString();
                String resource = secured.resource();

                if (StringUtils.isBlank(resource)) {
                    ResourceParser parser = secured.parser().newInstance();
                    resource = parser.parseName(req);
                }

                if (StringUtils.isBlank(resource)) {
                    // deny if we don't find any resource:
                    throw new AccessException("resource name invalid!");
                }

                authManager.auth(new Permission(resource, action), authManager.login(req));

            }

        } catch (AccessException e) {
            if (Loggers.AUTH.isDebugEnabled()) {
                Loggers.AUTH.debug("access denied, request: {} {}, reason: {}", req.getMethod(), req.getRequestURI(), e.getErrMsg());
            }
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getErrMsg());
            return;
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ExceptionUtil.getAllExceptionMsg(e));
            return;
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server failed," + e.getMessage());
            return;
        }

        chain.doFilter(request, response);
    }
}
