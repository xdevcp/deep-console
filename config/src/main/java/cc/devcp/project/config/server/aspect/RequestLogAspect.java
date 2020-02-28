package cc.devcp.project.config.server.aspect;

import cc.devcp.project.config.server.monitor.MetricsMonitor;
import cc.devcp.project.config.server.service.ConfigService;
import cc.devcp.project.config.server.utils.GroupKey2;
import cc.devcp.project.config.server.utils.LogUtil;
import cc.devcp.project.config.server.utils.MD5;
import cc.devcp.project.config.server.utils.RequestUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * * Created with IntelliJ IDEA. User: dingjoey Date: 13-12-12 Time: 21:12 client api && sdk api 请求日志打点逻辑
 *
 * @author Nacos
 */
@Aspect
@Component
public class RequestLogAspect {
    /**
     * publish config
     */
    private static final String CLIENT_INTERFACE_PUBLISH_SINGLE_CONFIG
        = "execution(* cc.devcp.project.config.server.controller.ConfigController.publishConfig(..)) && args"
        + "(request,response,dataId,group,tenant,content,..)";

    /**
     * get config
     */
    private static final String CLIENT_INTERFACE_GET_CONFIG
        = "execution(* cc.devcp.project.config.server.controller.ConfigController.getConfig(..)) && args(request,"
        + "response,dataId,group,tenant,..)";

    /**
     * remove config
     */
    private static final String CLIENT_INTERFACE_REMOVE_ALL_CONFIG
        = "execution(* cc.devcp.project.config.server.controller.ConfigController.deleteConfig(..)) && args(request,"
        + "response,dataId,group,tenant,..)";


    /**
     * publishSingle
     */
    @Around(CLIENT_INTERFACE_PUBLISH_SINGLE_CONFIG)
    public Object interfacePublishSingle(ProceedingJoinPoint pjp, HttpServletRequest request,
                                         HttpServletResponse response, String dataId, String group, String tenant,
                                         String content) throws Throwable {
        final String md5 = content == null ? null : MD5.getInstance().getMD5String(content);
        MetricsMonitor.getPublishMonitor().incrementAndGet();
        return logClientRequest("publish", pjp, request, response, dataId, group, tenant, md5);
    }

    /**
     * removeAll
     */
    @Around(CLIENT_INTERFACE_REMOVE_ALL_CONFIG)
    public Object interfaceRemoveAll(ProceedingJoinPoint pjp, HttpServletRequest request, HttpServletResponse response,
                                     String dataId, String group, String tenant) throws Throwable {
        return logClientRequest("remove", pjp, request, response, dataId, group, tenant, null);
    }

    /**
     * getConfig
     */
    @Around(CLIENT_INTERFACE_GET_CONFIG)
    public Object interfaceGetConfig(ProceedingJoinPoint pjp, HttpServletRequest request, HttpServletResponse response,
                                     String dataId, String group, String tenant) throws Throwable {
        final String groupKey = GroupKey2.getKey(dataId, group, tenant);
        final String md5 = ConfigService.getContentMd5(groupKey);
        MetricsMonitor.getConfigMonitor().incrementAndGet();
        return logClientRequest("get", pjp, request, response, dataId, group, tenant, md5);
    }

    /**
     * client api request log rt | status | requestIp | opType | dataId | group | datumId | md5
     */
    private Object logClientRequest(String requestType, ProceedingJoinPoint pjp, HttpServletRequest request,
                                    HttpServletResponse response, String dataId, String group, String tenant,
                                    String md5) throws Throwable {
        final String requestIp = RequestUtil.getRemoteIp(request);
        String appName = request.getHeader(RequestUtil.CLIENT_APPNAME_HEADER);
        final long st = System.currentTimeMillis();
        Object retVal = pjp.proceed();
        final long rt = System.currentTimeMillis() - st;
        // rt | status | requestIp | opType | dataId | group | datumId | md5 |
        // appName
        LogUtil.clientLog.info("{}|{}|{}|{}|{}|{}|{}|{}|{}", rt, retVal, requestIp, requestType, dataId, group, tenant,
            md5, appName);
        return retVal;
    }

}
