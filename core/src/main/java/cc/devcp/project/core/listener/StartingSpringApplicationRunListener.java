package cc.devcp.project.core.listener;

import cc.devcp.project.core.utils.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static cc.devcp.project.core.utils.SystemUtils.FUNCTION_MODE;
import static cc.devcp.project.core.utils.SystemUtils.LOCAL_IP;
import static cc.devcp.project.core.utils.SystemUtils.APP_HOME;
import static cc.devcp.project.core.utils.SystemUtils.STANDALONE_MODE;
import static cc.devcp.project.core.utils.SystemUtils.readClusterConf;

/**
 * Logging starting message {@link SpringApplicationRunListener} before {@link EventPublishingRunListener} execution
 *
 * @author <a href="mailto:huangxiaoyu1018@gmail.com">hxy1991</a>
 * @since 0.5.0
 */
public class StartingSpringApplicationRunListener implements SpringApplicationRunListener, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartingSpringApplicationRunListener.class);

    private static final String MODE_PROPERTY_KEY_STAND_MODE = "startup.mode";

    private static final String MODE_PROPERTY_KEY_FUNCTION_MODE = "startup.function.mode";

    private static final String LOCAL_IP_PROPERTY_KEY = "local.ip";

    private ScheduledExecutorService scheduledExecutorService;

    private volatile boolean starting;

    public StartingSpringApplicationRunListener(SpringApplication application, String[] args) {

    }

    @Override
    public void starting() {
        starting = true;
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        if (STANDALONE_MODE) {
            System.setProperty(MODE_PROPERTY_KEY_STAND_MODE, "stand alone");
        } else {
            System.setProperty(MODE_PROPERTY_KEY_STAND_MODE, "cluster");
        }
        if (FUNCTION_MODE == null) {
           System.setProperty(MODE_PROPERTY_KEY_FUNCTION_MODE, "All");
        } else if(SystemUtils.FUNCTION_MODE_CONFIG.equals(FUNCTION_MODE)){
            System.setProperty(MODE_PROPERTY_KEY_FUNCTION_MODE, SystemUtils.FUNCTION_MODE_CONFIG);
        } else if(SystemUtils.FUNCTION_MODE_NAMING.equals(FUNCTION_MODE)) {
            System.setProperty(MODE_PROPERTY_KEY_FUNCTION_MODE, SystemUtils.FUNCTION_MODE_NAMING);
        }


        System.setProperty(LOCAL_IP_PROPERTY_KEY, LOCAL_IP);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        logClusterConf();

        logStarting();
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        starting = false;

        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
        }

        logFilePath();

        LOGGER.info("App started successfully in {} mode.", System.getProperty(MODE_PROPERTY_KEY_STAND_MODE));
    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        starting = false;

        logFilePath();

        LOGGER.error("App failed to start, please see {}/logs/all.log for more details.", APP_HOME);
    }

    /**
     * Before {@link EventPublishingRunListener}
     *
     * @return HIGHEST_PRECEDENCE
     */
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    private void logClusterConf() {
        if (!STANDALONE_MODE) {
            try {
                List<String> clusterConf = readClusterConf();
                LOGGER.info("The server IP list of Nacos is {}", clusterConf);
            } catch (IOException e) {
                LOGGER.error("read cluster conf fail", e);
            }
        }
    }

    private void logFilePath() {
        String[] dirNames = new String[]{"logs", "conf", "data"};
        for (String dirName: dirNames) {
            LOGGER.info("App Log files: {}{}{}{}", APP_HOME, File.separatorChar, dirName, File.separatorChar);
        }
    }

    private void logStarting() {
        if (!STANDALONE_MODE) {

            scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "app-starting");
                    thread.setDaemon(true);
                    return thread;
                }
            });

            scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    if (starting) {
                        LOGGER.info("App is starting...");
                    }
                }
            }, 1, 1, TimeUnit.SECONDS);
        }
    }
}
