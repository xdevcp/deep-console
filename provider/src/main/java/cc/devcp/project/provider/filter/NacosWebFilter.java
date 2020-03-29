package cc.devcp.project.provider.filter;

import cc.devcp.project.provider.constant.Constants;
import cc.devcp.project.provider.utils.LogUtil;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * encode filter
 *
 * @author Nacos
 */
@Order(1)
@WebFilter(filterName = "webFilter", urlPatterns = "/*")
public class NacosWebFilter implements Filter {

    static private String webRootPath;

    static public String rootPath() {
        return webRootPath;
    }

    /**
     * 方便测试
     *
     * @param path web path
     */
    static public void setWebRootPath(String path) {
        webRootPath = path;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext ctx = filterConfig.getServletContext();
        setWebRootPath(ctx.getRealPath("/"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        request.setCharacterEncoding(Constants.ENCODE);
        response.setContentType("application/json;charset=" + Constants.ENCODE);

        try {
            chain.doFilter(request, response);
        } catch (IOException ioe) {
            LogUtil.defaultLog.debug("Filter catch exception, " + ioe.toString(), ioe);
            throw ioe;
        } catch (ServletException se) {
            LogUtil.defaultLog.debug("Filter catch exception, " + se.toString(), se);
            throw se;
        }
    }

    @Override
    public void destroy() {
    }

}
