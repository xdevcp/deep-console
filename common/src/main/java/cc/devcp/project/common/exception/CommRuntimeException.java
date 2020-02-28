package cc.devcp.project.common.exception;

import cc.devcp.project.common.enums.CommEnum;

/**
 * @author lizhiyuan
 * @create 2018-03-12 17:57
 * @desc
 **/
public class CommRuntimeException extends RuntimeException{

    private static final long serialVersionUID = -238091758285157331L;

    private static final String MESSAGE_FORMAT = "ErrorCode:%d ; ErrorMessage:%s ; ExceptionMessage:%s ; ";

    private Integer errCode;
    private String errMsg;

    public CommRuntimeException() {
        super();
    }

    public CommRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommRuntimeException(String message) {
        super(message);
    }

    public CommRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造方法
     *
     * @param errCode
     * @param errMsg
     */
    public CommRuntimeException(Integer errCode, String errMsg) {
        super(String.format(MESSAGE_FORMAT, errCode, errMsg, "null"));
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    /**
     * 构造方法
     * @param errCode
     * @param errMsg
     * @param cause
     */
    public CommRuntimeException(Integer errCode, String errMsg, Throwable cause) {
        super(String.format(MESSAGE_FORMAT, errCode, errMsg, ExceptionUtil.getMessage(cause)), cause);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    /**
     * 基于commEnum的构造方法
     *
     * @param commEnum
     */
    public CommRuntimeException(CommEnum<Integer> commEnum) {
        super(String.format(MESSAGE_FORMAT, commEnum.getCode(), commEnum.getContent(), "null"));
        this.errCode = commEnum.getCode();
        this.errMsg = commEnum.getContent();
    }


    /**
     * 基于commEnum的构造方法
     *
     * @param commEnum
     */
    public CommRuntimeException(CommEnum<Integer> commEnum, Throwable cause) {
        super(String.format(MESSAGE_FORMAT, commEnum.getCode(), commEnum.getContent(), ExceptionUtil.getMessage(cause)), cause);
        this.errCode = commEnum.getCode();
        this.errMsg = commEnum.getContent();
    }
    public Integer getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

}
