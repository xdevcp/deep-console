package cc.devcp.project.config.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Trace Util
 *
 * @author Nacos
 */
public class TraceLogUtil {
    /**
     * 记录server各个接口的请求记录
     */
    public static Logger requestLog = LoggerFactory.getLogger("cc.devcp.project.config.request");

    /**
     * 记录各个client的轮询请求记录
     */
    public static Logger pollingLog = LoggerFactory.getLogger("cc.devcp.project.config.polling");

}
