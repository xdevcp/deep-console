package cc.devcp.project.provider.service.merge;

import cc.devcp.project.provider.manager.TaskManager;
import cc.devcp.project.provider.model.ConfigInfo;
import cc.devcp.project.provider.model.ConfigInfoAggr;
import cc.devcp.project.provider.model.ConfigInfoChanged;
import cc.devcp.project.common.model.page.Page;
import cc.devcp.project.provider.service.PersistService;
import cc.devcp.project.provider.utils.ContentUtils;
import cc.devcp.project.provider.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static cc.devcp.project.core.utils.SystemUtils.LOCAL_IP;

/**
 * 数据聚合服务。
 * <p>
 * 启动时做全量聚合 + 修改数据触发的单条聚合
 *
 * @author jiuRen
 */
@Service
public class MergeDatumService {

    private PersistService persistService;
    static final int INIT_THREAD_COUNT = 40;
    static final AtomicInteger FINISHED = new AtomicInteger();
    static int total = 0;

    @Autowired
    public MergeDatumService(PersistService persistService) {
        this.persistService = persistService;
        mergeTasks = new TaskManager("cc.devcp.project.MergeDatum");
        mergeTasks.setDefaultTaskProcessor(new MergeTaskProcessor(persistService, this));

    }

    static List<List<ConfigInfoChanged>> splitList(List<ConfigInfoChanged> list, int count) {
        List<List<ConfigInfoChanged>> result = new ArrayList<List<ConfigInfoChanged>>(count);
        for (int i = 0; i < count; i++) {
            result.add(new ArrayList<ConfigInfoChanged>());
        }
        for (int i = 0; i < list.size(); i++) {
            ConfigInfoChanged config = list.get(i);
            result.get(i % count).add(config);
        }
        return result;
    }

    /**
     * 数据变更后调用，添加聚合任务
     */
    public void addMergeTask(String dataId, String groupId, String tenant, String tag, String clientIp) {
        MergeDataTask task = new MergeDataTask(dataId, groupId, tenant, tag, clientIp);
        mergeTasks.addTask(task.getId(), task);
    }

    /**
     * 数据变更后调用，添加聚合任务
     */
    public void addMergeTask(String dataId, String groupId, String tenant, String clientIp) {
        MergeDataTask task = new MergeDataTask(dataId, groupId, tenant, clientIp);
        mergeTasks.addTask(task.getId(), task);
    }

    public void mergeAll() {
        for (ConfigInfoChanged item : persistService.findAllAggrGroup()) {
            addMergeTask(item.getDataId(), item.getGroup(), item.getTenant(), LOCAL_IP);
        }
    }

    class MergeAllDataWorker extends Thread {
        static final int PAGE_SIZE = 10000;

        private List<ConfigInfoChanged> configInfoList;

        public MergeAllDataWorker(List<ConfigInfoChanged> configInfoList) {
            super("MergeAllDataWorker");
            this.configInfoList = configInfoList;
        }

        @Override
        public void run() {
            for (ConfigInfoChanged configInfo : configInfoList) {
                String dataId = configInfo.getDataId();
                String group = configInfo.getGroup();
                String tenant = configInfo.getTenant();
                try {
                    List<ConfigInfoAggr> datumList = new ArrayList<ConfigInfoAggr>();
                    int rowCount = persistService.aggrConfigInfoCount(dataId, group, tenant);
                    int pageCount = (int)Math.ceil(rowCount * 1.0 / PAGE_SIZE);
                    for (int pageNo = 1; pageNo <= pageCount; pageNo++) {
                        Page<ConfigInfoAggr> page = persistService.findConfigInfoAggrByPage(dataId, group, tenant,
                            pageNo, PAGE_SIZE);
                        if (page != null) {
                            datumList.addAll(page.getPageItems());
                            log.info("[merge-query] {}, {}, size/total={}/{}", dataId, group, datumList.size(),
                                rowCount);
                        }
                    }

                    final Timestamp time = TimeUtils.getCurrentTime();
                    // 聚合
                    if (datumList.size() > 0) {
                        ConfigInfo cf = MergeTaskProcessor.merge(dataId, group, tenant, datumList);
                        persistService.insertOrUpdate(null, null, cf, time, null, false);
                        log.info("[merge-ok] {}, {}, size={}, length={}, md5={}, content={}", dataId, group,
                            datumList.size(), cf.getContent().length(), cf.getMd5(),
                            ContentUtils.truncateContent(cf.getContent()));
                    }
                    // 删除
                    else {
                        persistService.removeConfigInfo(dataId, group, tenant, LOCAL_IP, null);
                        log.warn("[merge-delete] delete config info because no datum. dataId=" + dataId + ", groupId="
                            + group);
                    }

                } catch (Exception e) {
                    log.info("[merge-error] " + dataId + ", " + group + ", " + e.toString(), e);
                }
                FINISHED.incrementAndGet();
                if (FINISHED.get() % 100 == 0) {
                    log.info("[all-merge-dump] {} / {}", FINISHED.get(), total);
                }
            }
            log.info("[all-merge-dump] {} / {}", FINISHED.get(), total);
        }
    }

    // =====================

    private static final Logger log = LoggerFactory.getLogger(MergeDatumService.class);

    final TaskManager mergeTasks;

}
