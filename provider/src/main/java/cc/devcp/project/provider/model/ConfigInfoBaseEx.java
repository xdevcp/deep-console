package cc.devcp.project.provider.model;

/**
 * 不能增加字段，为了兼容老前台接口（老接口增加一个字段会出现不兼容问题）设置的model。
 *
 * @author Nacos
 */
public class ConfigInfoBaseEx extends ConfigInfoBase {

    private static final long serialVersionUID = -1L;
    //不能增加字段
    /**
     * 批量查询时, 单条数据的状态码, 具体的状态码在Constants.java中
     */
    private int status;
    /**
     * 批量查询时, 单条数据的信息
     */
    private String message;

    public ConfigInfoBaseEx() {
        super();
    }

    public ConfigInfoBaseEx(String dataId, String group, String content) {
        super(dataId, group, content);
    }

    public ConfigInfoBaseEx(String dataId, String group, String content,
                            int status, String message) {
        super(dataId, group, content);
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "ConfigInfoBaseEx [status=" + status + ", message=" + message
            + ", dataId=" + getDataId() + ", group()=" + getGroup()
            + ", content()=" + getContent() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
