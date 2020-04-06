package cc.devcp.project.core.filter;

import cc.devcp.project.common.constant.CxtRouter;
import cc.devcp.project.common.model.result.ResponseWrapper;
import cc.devcp.project.common.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     description:
 * </pre>
 *
 * @author deep.wu
 * @version 2020-04-04
 */
@Slf4j
public class LoggerFilter extends OncePerRequestFilter {

    private final static String JSON = "json";

    private List<String> filterPathList;

    @PostConstruct
    public void init() {
        filterPathList = new ArrayList<>();
        filterPathList.add(CxtRouter.gateway_ver_auth);
        filterPathList.add(CxtRouter.gateway_ver_open);
        filterPathList.add(CxtRouter.gateway_ver_console);
    }

    public boolean doIgnore(String path) {
        for (String filterPath : filterPathList) {
            if (path.indexOf(filterPath) > -1) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String path = httpServletRequest.getServletPath();
        if (doIgnore(path)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        log.info("========== Request Start ==========");
        RequestWrapper requestWrapper = null;
        String url = httpServletRequest.getRequestURL().toString();
        log.info("RequestUrl: {}; RequestPath：{}", url, path);
        log.info("RequestParamMap: {}", JacksonUtil.toJson(httpServletRequest.getParameterMap()));
        if (null != httpServletRequest.getContentType() && httpServletRequest.getContentType().toLowerCase().indexOf(JSON) > -1) {
            requestWrapper = new RequestWrapper(httpServletRequest);
            log.info("RequestBody: {}", requestWrapper.getBody());
        }
        log.info("ResponseStatus: {}", httpServletResponse.getStatus());
        ResponseWrapper responseWrapper = new ResponseWrapper(httpServletResponse);
        if (null == requestWrapper) {
            filterChain.doFilter(httpServletRequest, responseWrapper);
        } else {
            filterChain.doFilter(requestWrapper, responseWrapper);
        }
        String content = responseWrapper.getTextContent();
        log.info("ResponseBody: {}", content);
        httpServletResponse.getOutputStream().write(content.getBytes());
        log.info("========== Request End ==========");
    }

}
