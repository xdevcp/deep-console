package cc.devcp.project.config.server.service.notify;

import cc.devcp.project.config.server.manager.AbstractTask;

/**
 * Notify task
 *
 * @author Nacos
 */
public class NotifyTask extends AbstractTask {

    private String dataId;
    private String group;
    private String tenant;
    private long lastModified;
    private int failCount;

    public NotifyTask(String dataId, String group, String tenant, long lastModified) {
        this.dataId = dataId;
        this.group = group;
        this.setTenant(tenant);
        this.lastModified = lastModified;
        setTaskInterval(3000L);
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

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public void merge(AbstractTask task) {
        // 进行merge, 但什么都不做, 相同的dataId和group的任务，后来的会代替之前的

    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

}
