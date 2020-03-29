package cc.devcp.project.auth.security.auth;

import cc.devcp.project.common.exception.GlobalException;

/**
 * Exception to be thrown if authorization is failed.
 *
 * @author nkorange
 * @since 1.2.0
 */
public class AccessException extends GlobalException {

    public AccessException(){

    }

    public AccessException(int code) {
        this.setErrCode(code);
    }

    public AccessException(String msg) {
        this.setErrMsg(msg);
    }

}
