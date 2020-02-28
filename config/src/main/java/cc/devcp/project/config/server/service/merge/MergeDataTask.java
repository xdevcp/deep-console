package cc.devcp.project.config.server.service.merge;

import cc.devcp.project.config.server.manager.AbstractTask;

/**
 * 表示对数据进行聚合的任务。
 *
 * @author jiuRen
 */
class MergeDataTask extends AbstractTask {

    MergeDataTask(String dataId, String groupId, String tenant, String clientIp) {
        this(dataId, groupId, tenant, null, clientIp);
    }

    MergeDataTask(String dataId, String groupId, String tenant, String tag, String clientIp) {
        this.dataId = dataId;
        this.groupId = groupId;
        this.tenant = tenant;
        this.tag = tag;
        this.clientIp = clientIp;

        // 聚合延迟
        setTaskInterval(DELAY);
        setLastProcessTime(System.currentTimeMillis());
    }

    @Override
    public void merge(AbstractTask task) {
    }

    public String getId() {
        return toString();
    }

    @Override
    public String toString() {
        return "MergeTask[" + dataId + ", " + groupId + ", " + tenant + ", " + clientIp + "]";
    }

    public String getClientIp() {
        return clientIp;
    }

    static final long DELAY = 0L;

    final String dataId;
    final String groupId;
    final String tenant;
    final String tag;
    private final String clientIp;
}
