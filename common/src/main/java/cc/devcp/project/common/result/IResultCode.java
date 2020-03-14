package cc.devcp.project.common.result;

/**
 * @author klw
 * @ClassName: IResultCode
 * @Description: result code enum needs to be implemented this interface
 * @date 2019/6/28 14:44
 */
public interface IResultCode {

    /**
     * get the result code
     *
     * @author klw
     * @Date 2019/6/28 14:56
     * @Param []
     * @return java.lang.String
     */
    int getCode();

    /**
     * get the result code's message
     *
     * @author klw
     * @Date 2019/6/28 14:56
     * @Param []
     * @return java.lang.String
     */
    String getCodeMsg();
}
