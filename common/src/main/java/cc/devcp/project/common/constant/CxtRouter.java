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
    String gateway_str = "gateway";
    String gateway = ROOT_WEB_CONTEXT_PATH + gateway_str;

    /**
     * 版本号
     */
    String ver_str = "v1";
    String ver = ROOT_WEB_CONTEXT_PATH + ver_str;
    String gateway_ver = gateway + ver;

    /**
     * 需要鉴权
     */
    String auth_str = "auth";
    String auth = ROOT_WEB_CONTEXT_PATH + auth_str;
    String gateway_ver_auth = gateway_ver + auth;

    /**
     * 模块
     */
    String console_str = "console";
    String console = ROOT_WEB_CONTEXT_PATH + console_str;
    String gateway_ver_console = gateway_ver + console;

    /**
     * 无需鉴权
     */
    String open_str = "open";
    String open = ROOT_WEB_CONTEXT_PATH + open_str;
    String gateway_ver_open = gateway_ver + open;

    /**
     * 测试
     */
    String test_str = "test";
    String test = ROOT_WEB_CONTEXT_PATH + test_str;
    String gateway_ver_open_test = gateway_ver_open + test;

    /**
     * 运维
     */
    String ops_str = "ops";
    String ops = ROOT_WEB_CONTEXT_PATH + ops_str;
    String gateway_ver_open_ops = gateway_ver_open + ops;

}
