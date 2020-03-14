package cc.devcp.project.common.result;

import org.springframework.util.Assert;

/**
 * @author klw
 * @ClassName: ResultBuilder
 * @Description: util for generating cc.devcp.project.config.server.model.RestResult
 * @date 2019/6/28 14:47
 */
public class ResultBuilder {

    public static <T extends Object> RestResult<T> buildResult(IResultCode resultCode, T resultData){
        Assert.notNull(resultCode, "the resultCode can not be null");
        RestResult<T> rr = new RestResult<>(resultCode.getCode(), resultCode.getCodeMsg(), resultData);
        return rr;
    }

    public static <T extends Object> RestResult<T> buildSuccessResult(T resultData){
        return buildResult(ResultCodeEnum.SUCCESS, resultData);
    }

    public static <T extends Object> RestResult<T> buildSuccessResult(String successMsg, T resultData){
        RestResult<T> rr = buildResult(ResultCodeEnum.SUCCESS, resultData);
        rr.setMessage(successMsg);
        return rr;
    }

    public static <T extends Object> RestResult<T> buildSuccessResult(){
        return buildResult(ResultCodeEnum.SUCCESS, null);
    }

    public static <T extends Object> RestResult<T> buildSuccessResult(String successMsg){
        RestResult<T> rr = buildResult(ResultCodeEnum.SUCCESS, null);
        rr.setMessage(successMsg);
        return rr;
    }
}
