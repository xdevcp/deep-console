package cc.devcp.project.common.constant;

/**
 * Constant 路由规范
 *
 * @author deep
 */
public interface GlobalRouter {

    /**
     * 版本号
     */
    String VER = "/v1";

    /**
     * 需要鉴权
     */
    String AUTH = VER + "/auth";

    /**
     * 无需鉴权
     */
    String OPEN = VER + "/open";

    /**
     * 测试
     */
    String TEST = "/test";
}
