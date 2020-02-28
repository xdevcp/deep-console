package cc.devcp.project.config.server.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Accumulate Stat Count
 *
 * @author Nacos
 */
public class AccumulateStatCount {

    final AtomicLong total = new AtomicLong(0);
    long lastStatValue = 0;

    public long increase() {
        return total.incrementAndGet();
    }

    public long stat() {
        long tmp = total.get() - lastStatValue;
        lastStatValue += tmp;
        return tmp;
    }
}
