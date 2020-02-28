package cc.devcp.project.config.server.monitor;

import cc.devcp.project.config.server.service.ClientTrackService;
import cc.devcp.project.config.server.service.ConfigService;
import cc.devcp.project.config.server.service.TimerTaskService;
import cc.devcp.project.config.server.service.notify.AsyncNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static cc.devcp.project.config.server.utils.LogUtil.memoryLog;

/**
 * Memory monitor
 *
 * @author deep
 */
@Service
public class MemoryMonitor {

    @Autowired
    public MemoryMonitor(AsyncNotifyService notifySingleService) {

        TimerTaskService.scheduleWithFixedDelay(new PrintMemoryTask(), DELAY_SECONDS,
            DELAY_SECONDS, TimeUnit.SECONDS);

        TimerTaskService.scheduleWithFixedDelay(new PrintGetConfigResponeTask(), DELAY_SECONDS,
            DELAY_SECONDS, TimeUnit.SECONDS);

        TimerTaskService.scheduleWithFixedDelay(new NotifyTaskQueueMonitorTask(notifySingleService), DELAY_SECONDS,
            DELAY_SECONDS, TimeUnit.SECONDS);

    }

    private static final long DELAY_SECONDS = 10;

    @Scheduled(cron = "0 0 0 * * ?")
    public void clear() {
        MetricsMonitor.getConfigMonitor().set(0);
        MetricsMonitor.getPublishMonitor().set(0);
    }
}

class PrintGetConfigResponeTask implements Runnable {
    @Override
    public void run() {
        memoryLog.info(ResponseMonitor.getStringForPrint());
    }
}

class PrintMemoryTask implements Runnable {

    @Override
    public void run() {
        int groupCount = ConfigService.groupCount();
        int subClientCount = ClientTrackService.subscribeClientCount();
        long subCount = ClientTrackService.subscriberCount();
        memoryLog.info("groupCount={}, subscriberClientCount={}, subscriberCount={}", groupCount, subClientCount,
            subCount);
        MetricsMonitor.getConfigCountMonitor().set(groupCount);
    }
}

class NotifyTaskQueueMonitorTask implements Runnable {
    final private AsyncNotifyService notifySingleService;

    NotifyTaskQueueMonitorTask(AsyncNotifyService notifySingleService) {
        this.notifySingleService = notifySingleService;
    }

    @Override
    public void run() {
        int size = ((ScheduledThreadPoolExecutor)notifySingleService.getExecutor()).getQueue().size();
        memoryLog.info("toNotifyTaskSize={}", size);
        MetricsMonitor.getNotifyTaskMonitor().set(size);
    }
}
