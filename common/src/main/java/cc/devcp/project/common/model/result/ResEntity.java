package cc.devcp.project.common.model.result;

import cc.devcp.project.common.enums.CommEnum;
import cc.devcp.project.common.exception.CommException;
import cc.devcp.project.common.exception.CommRuntimeException;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.Serializable;

/**
 * @version 1.0.0
 * @author: lzy
 * @date: 2019/6/13 11:04
 */
public class ResEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String FORMAT = "[%s.%s]:/**%s**/";

    @ApiModelProperty(value = "请求成功标识", allowableValues = "true,false", example = "false")
    private Boolean success;

    @ApiModelProperty(value = "错误码，错误时返回", example = "40000")
    private Integer errCode;

    @ApiModelProperty(value = "错误信息，错误时返回", example = "参数错误")
    private String errMsg;

    @ApiModelProperty(value = "返回数据")
    private T data;

    /**
     * ok
     *
     * @param data
     * @return success, data
     */
    public static ResEntity ok(Object data) {
        ResEntity res = new ResEntity();
        res.setSuccess(Boolean.TRUE);
        res.setData(data);
        return res;
    }

    /**
     * ok
     *
     * @return success
     */
    public static ResEntity ok() {
        return ok(null);
    }

    /**
     * fail
     *
     * @param errCode
     * @param errMsg
     * @param data
     * @return success, errCode, errMsg, data
     */
    public static ResEntity fail(Integer errCode, String errMsg, Object data) {
        ResEntity res = new ResEntity();
        res.setSuccess(Boolean.FALSE);
        res.setErrCode(errCode);
        res.setErrMsg(errMsg);
        res.setData(data);
        return res;
    }

    /**
     * fail
     *
     * @param errCode
     * @param errMsg
     * @return success, errCode, errMsg
     */
    public static ResEntity fail(Integer errCode, String errMsg) {
        return fail(errCode, errMsg, null);
    }


    /**
     * fail
     *
     * @param bindingResult
     * @return success, errCode, errMsg
     */
    public static ResEntity fail(BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            if (stringBuilder.length() > 1) {
                stringBuilder.append("、");
            }
            stringBuilder.append(String.format(FORMAT, fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return fail(40000, stringBuilder.toString(), null);
    }

    /**
     * fail
     *
     * @param commEnum
     * @return success, errCode, errMsg,
     */
    public static ResEntity fail(CommEnum<Integer> commEnum) {
        return fail(commEnum.getCode(), commEnum.getContent(), null);
    }

    /**
     * fail
     *
     * @param commEnum
     * @param data
     * @return success, errCode, errMsg, data
     */
    public static ResEntity fail(CommEnum<Integer> commEnum, Object data) {
        return fail(commEnum.getCode(), commEnum.getContent(), data);
    }


    /**
     * fail
     *
     * @param e
     * @return success, errCode, errMsg
     */
    public static ResEntity fail(CommException e) {
        return fail(e.getErrCode(), e.getErrMsg(), null);
    }


    /**
     * fail
     *
     * @param e
     * @param data
     * @return success, errCode, errMsg, data
     */
    public static ResEntity fail(CommException e, Object data) {
        return fail(e.getErrCode(), e.getErrMsg(), data);
    }

    /**
     * fail
     *
     * @param e
     * @return success, errCode, errMsg
     */
    public static ResEntity fail(CommRuntimeException e) {
        return fail(e.getErrCode(), e.getErrMsg(), null);
    }

    /**
     * fail
     *
     * @param e
     * @param data
     * @return success, errCode, errMsg, data
     */
    public static ResEntity fail(CommRuntimeException e, Object data) {
        return fail(e.getErrCode(), e.getErrMsg(), data);
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResEntity{" +
            "success=" + success +
            ", errCode=" + errCode +
            ", errMsg='" + errMsg + '\'' +
            ", data=" + data +
            '}';
    }
}
