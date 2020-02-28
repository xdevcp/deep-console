package cc.devcp.project.config.server.model;

import java.sql.Timestamp;

/**
 * sub 数据结构体
 *
 * @author Nacos
 */
public class SubInfo {

    private String appName;
    private String dataId;
    private String group;
    private String localIp;
    private Timestamp date;

    public String getAppName() {
        return appName;
    }

    public String getDataId() {
        return dataId;
    }

    public String getGroup() {
        return group;
    }

    public Timestamp getDate() {
        return new Timestamp(date.getTime());
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setDate(Timestamp date) {
        this.date = new Timestamp(date.getTime());
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

}
