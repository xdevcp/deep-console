package cc.devcp.project.provider.model;

/**
 * 配置信息类
 *
 * @author boyan
 * @date 2010-5-4
 */
public class ConfigInfo extends ConfigInfoBase {
    static final long serialVersionUID = -1L;

    private String tenant;

    private String appName;

    private String type;

    public ConfigInfo() {

    }

    public ConfigInfo(String dataId, String group, String content) {
        super(dataId, group, content);
    }

    public ConfigInfo(String dataId, String group, String appName, String content) {
        super(dataId, group, content);
        this.appName = appName;
    }

    public ConfigInfo(String dataId, String group, String tenant, String appName, String content) {
        super(dataId, group, content);
        this.tenant = tenant;
        this.appName = appName;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "ConfigInfo{" + "id=" + getId() + ", dataId='" + getDataId() + '\'' + ", group='" + getGroup() + '\''
            + ", tenant='" + tenant + '\'' + ", appName='" + appName + '\'' + ", content='" + getContent() + '\''
            + ", md5='" + getMd5() + '\'' + '}';
    }

}
