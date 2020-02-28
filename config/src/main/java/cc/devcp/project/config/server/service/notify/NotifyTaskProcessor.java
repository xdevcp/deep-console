package cc.devcp.project.config.server.service.notify;

import cc.devcp.project.config.server.constant.Constants;
import cc.devcp.project.config.server.manager.AbstractTask;
import cc.devcp.project.config.server.manager.TaskProcessor;
import cc.devcp.project.config.server.monitor.MetricsMonitor;
import cc.devcp.project.config.server.service.ServerListService;
import cc.devcp.project.config.server.service.notify.NotifyService.HttpResult;
import cc.devcp.project.config.server.service.trace.ConfigTraceService;
import cc.devcp.project.config.server.utils.RunningConfigUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cc.devcp.project.core.utils.SystemUtils.LOCAL_IP;

/**
 * 通知服务。数据库变更后，通知所有server，包括自己，加载新数据。
 *
 * @author Nacos
 */
public class NotifyTaskProcessor implements TaskProcessor {

    public NotifyTaskProcessor(ServerListService serverListService) {
        this.serverListService = serverListService;
    }

    @Override
    public boolean process(String taskType, AbstractTask task) {
        NotifyTask notifyTask = (NotifyTask)task;
        String dataId = notifyTask.getDataId();
        String group = notifyTask.getGroup();
        String tenant = notifyTask.getTenant();
        long lastModified = notifyTask.getLastModified();

        boolean isok = true;

        for (String ip : serverListService.getServerList()) {
            isok = notifyToDump(dataId, group, tenant, lastModified, ip) && isok;
        }
        return isok;
    }

    /**
     * 通知其他server
     */
    boolean notifyToDump(String dataId, String group, String tenant, long lastModified, String serverIp) {
        long delayed = System.currentTimeMillis() - lastModified;
        try {
            // XXX 為了方便系统beta，不改变notify.do接口，新增lastModifed参数通过Http header传递
            List<String> headers = Arrays.asList(
                NotifyService.NOTIFY_HEADER_LAST_MODIFIED, String.valueOf(lastModified),
                NotifyService.NOTIFY_HEADER_OP_HANDLE_IP, LOCAL_IP);
            String urlString = MessageFormat.format(URL_PATTERN, serverIp, RunningConfigUtils.getContextPath(), dataId,
                group);

            HttpResult result = NotifyService.invokeURL(urlString, headers, Constants.ENCODE);
            if (result.code == HttpStatus.SC_OK) {
                ConfigTraceService.logNotifyEvent(dataId, group, tenant, null, lastModified, LOCAL_IP,
                    ConfigTraceService.NOTIFY_EVENT_OK, delayed, serverIp);

                MetricsMonitor.getNotifyRtTimer().record(delayed, TimeUnit.MILLISECONDS);

                return true;
            } else {
                MetricsMonitor.getConfigNotifyException().increment();
                log.error("[notify-error] {}, {}, to {}, result {}", new Object[] {dataId, group,
                    serverIp, result.code});
                ConfigTraceService.logNotifyEvent(dataId, group, tenant, null, lastModified, LOCAL_IP,
                    ConfigTraceService.NOTIFY_EVENT_ERROR, delayed, serverIp);
                return false;
            }
        } catch (Exception e) {
            MetricsMonitor.getConfigNotifyException().increment();
            log.error(
                "[notify-exception] " + dataId + ", " + group + ", to " + serverIp + ", "
                    + e.toString());
            log.debug("[notify-exception] " + dataId + ", " + group + ", to " + serverIp + ", " + e.toString(), e);
            ConfigTraceService.logNotifyEvent(dataId, group, tenant, null, lastModified, LOCAL_IP,
                ConfigTraceService.NOTIFY_EVENT_EXCEPTION, delayed, serverIp);
            return false;
        }
    }

    static final Logger log = LoggerFactory.getLogger(NotifyTaskProcessor.class);

    static final String URL_PATTERN = "http://{0}{1}" + Constants.COMMUNICATION_CONTROLLER_PATH + "/dataChange"
        + "?dataId={2}&group={3}";

    final ServerListService serverListService;
}
