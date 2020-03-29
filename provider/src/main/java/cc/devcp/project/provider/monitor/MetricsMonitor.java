package cc.devcp.project.provider.monitor;

import io.micrometer.core.instrument.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Metrics Monitor
 *
 * @author deep
 */
public class MetricsMonitor {
    private static AtomicInteger getConfig = new AtomicInteger();
    private static AtomicInteger publish = new AtomicInteger();
    private static AtomicInteger longPolling = new AtomicInteger();
    private static AtomicInteger configCount = new AtomicInteger();
    private static AtomicInteger notifyTask = new AtomicInteger();
    private static AtomicInteger dumpTask = new AtomicInteger();

    static {
        List<Tag> tags = new ArrayList<Tag>();
        tags.add(new ImmutableTag("module", "config"));
        tags.add(new ImmutableTag("name", "getConfig"));
        Metrics.gauge("nacos_monitor", tags, getConfig);

        tags = new ArrayList<Tag>();
        tags.add(new ImmutableTag("module", "config"));
        tags.add(new ImmutableTag("name", "publish"));
        Metrics.gauge("nacos_monitor", tags, publish);

        tags = new ArrayList<Tag>();
        tags.add(new ImmutableTag("module", "config"));
        tags.add(new ImmutableTag("name", "longPolling"));
        Metrics.gauge("nacos_monitor", tags, longPolling);

        tags = new ArrayList<Tag>();
        tags.add(new ImmutableTag("module", "config"));
        tags.add(new ImmutableTag("name", "configCount"));
        Metrics.gauge("nacos_monitor", tags, configCount);

        tags = new ArrayList<Tag>();
        tags.add(new ImmutableTag("module", "config"));
        tags.add(new ImmutableTag("name", "notifyTask"));
        Metrics.gauge("nacos_monitor", tags, notifyTask);

        tags = new ArrayList<Tag>();
        tags.add(new ImmutableTag("module", "config"));
        tags.add(new ImmutableTag("name", "dumpTask"));

        Metrics.gauge("nacos_monitor", tags, dumpTask);
    }

    public static AtomicInteger getConfigMonitor() {
        return getConfig;
    }

    public static AtomicInteger getPublishMonitor() {
        return publish;
    }

    public static AtomicInteger getLongPollingMonitor() {
        return longPolling;
    }

    public static AtomicInteger getConfigCountMonitor() {
        return configCount;
    }

    public static AtomicInteger getNotifyTaskMonitor() {
        return notifyTask;
    }

    public static AtomicInteger getDumpTaskMonitor() {
        return dumpTask;
    }

    public static Timer getNotifyRtTimer() {
        return Metrics.timer("nacos_timer",
            "module", "config", "name", "notifyRt");
    }

    public static Counter getIllegalArgumentException() {
        return Metrics.counter("nacos_exception",
            "module", "config", "name", "illegalArgument");
    }

    public static Counter getNacosException() {
        return Metrics.counter("nacos_exception",
            "module", "config", "name", "nacos");
    }

    public static Counter getDbException() {
        return Metrics.counter("nacos_exception",
            "module", "config", "name", "db");
    }

    public static Counter getConfigNotifyException() {
        return Metrics.counter("nacos_exception",
            "module", "config", "name", "configNotify");
    }

    public static Counter getUnhealthException() {
        return Metrics.counter("nacos_exception",
            "module", "config", "name", "unhealth");
    }

}
