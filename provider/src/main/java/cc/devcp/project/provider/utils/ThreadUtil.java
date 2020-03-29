package cc.devcp.project.provider.utils;

/**
 * Thread util
 *
 * @author Nacos
 */
public class ThreadUtil {

    /**
     * 通过内核数，算出合适的线程数；1.5-2倍cpu内核数
     *
     * @return thread count
     */
    public static int getSuitableThreadCount() {
        final int coreCount = Runtime.getRuntime().availableProcessors();
        int workerCount = 1;
        while (workerCount < coreCount * THREAD_MULTIPLER) {
            workerCount <<= 1;
        }
        return workerCount;
    }

    private final static int THREAD_MULTIPLER = 2;
}
