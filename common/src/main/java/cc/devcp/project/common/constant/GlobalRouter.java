package cc.devcp.project.common.constant;

/**
 * Constant 全局路由
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
    String AUTH = "auth";
    String VER_AUTH = VER + CommonConst.ROOT_WEB_CONTEXT_PATH + AUTH;

    /**
     * 无需鉴权
     */
    String OPEN = "open";
    String VER_OPEN = VER + CommonConst.ROOT_WEB_CONTEXT_PATH + OPEN;

    /**
     * 测试
     */
    String TEST = "test";
    String VER_OPEN_TEST = VER_OPEN + CommonConst.ROOT_WEB_CONTEXT_PATH + TEST;

    /**
     * 运维
     */
    String OPS = "ops";
    String VER_OPEN_OPS = VER_OPEN + CommonConst.ROOT_WEB_CONTEXT_PATH + OPS;

    /**
     * 模块
     */
    String CONSOLE = "console";
    String VER_CONSOLE = VER + "/" + CONSOLE;
}
