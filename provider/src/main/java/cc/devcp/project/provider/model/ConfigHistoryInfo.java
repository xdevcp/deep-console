package cc.devcp.project.provider.model;

import java.sql.Timestamp;

/**
 * history Info
 *
 * @author Nacos
 */
public class ConfigHistoryInfo {

    /**
     * id, nid, data_id, group_id, content, md5, gmt_create, gmt_modified, （配置创建时间，配置变更时间） src_user, src_ip, (变更操作者)
     * op_type（变更操作类型）
     */

    private long id;
    /**
     * 上次改动历史的id
     */
    private long lastId = -1;

    private String dataId;
    private String group;
    private String tenant;
    private String appName;
    private String md5;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLastId() {
        return lastId;
    }

    public void setLastId(long lastId) {
        this.lastId = lastId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSrcIp() {
        return srcIp;
    }

    public void setSrcIp(String srcIp) {
        this.srcIp = srcIp;
    }

    public String getSrcUser() {
        return srcUser;
    }

    public void setSrcUser(String srcUser) {
        this.srcUser = srcUser;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public Timestamp getCreatedTime() {
        return new Timestamp(createdTime.getTime());
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = new Timestamp(createdTime.getTime());
    }

    public Timestamp getLastModifiedTime() {
        return new Timestamp(lastModifiedTime.getTime());
    }

    public void setLastModifiedTime(Timestamp lastModifiedTime) {
        this.lastModifiedTime = new Timestamp(lastModifiedTime.getTime());
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    private String content;

    private String srcIp;
    private String srcUser;
    /**
     * 操作类型, 包括插入、更新、删除
     */
    private String opType;

    private Timestamp createdTime;
    private Timestamp lastModifiedTime;
}
