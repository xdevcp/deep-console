package cc.devcp.project.common.constant;

/**
 * Constant 全局路由
 * CONTEXT_PATH ${ctx}
 *
 * @author deep
 */
public interface CxtRouter {

    String ROOT_WEB_CONTEXT_PATH = "/";

    /**
     * 网关
     */
    String GATEWAY_STR = "gateway";
    String GATEWAY = ROOT_WEB_CONTEXT_PATH + GATEWAY_STR;

    /**
     * 版本号
     */
    String VER_STR = "v1";
    String VER = ROOT_WEB_CONTEXT_PATH + VER_STR;
    String GATEWAY_VER = GATEWAY + VER;

    /**
     * 需要鉴权
     */
    String AUTH_STR = "auth";
    String AUTH = ROOT_WEB_CONTEXT_PATH + AUTH_STR;
    String GATEWAY_VER_AUTH = GATEWAY_VER + AUTH;

    /**
     * 模块
     */
    String CONSOLE_STR = "console";
    String CONSOLE = ROOT_WEB_CONTEXT_PATH + CONSOLE_STR;
    String GATEWAY_VER_CONSOLE = GATEWAY_VER + CONSOLE;

    /**
     * 无需鉴权
     */
    String OPEN_STR = "open";
    String OPEN = ROOT_WEB_CONTEXT_PATH + OPEN_STR;
    String GATEWAY_VER_OPEN = GATEWAY_VER + OPEN;

    /**
     * 测试
     */
    String TEST_STR = "test";
    String TEST = ROOT_WEB_CONTEXT_PATH + TEST_STR;
    String GATEWAY_VER_OPEN_TEST = GATEWAY_VER_OPEN + TEST;

    /**
     * 运维
     */
    String OPS_STR = "ops";
    String OPS = ROOT_WEB_CONTEXT_PATH + OPS_STR;
    String GATEWAY_VER_OPEN_OPS = GATEWAY_VER_OPEN + OPS;

}
