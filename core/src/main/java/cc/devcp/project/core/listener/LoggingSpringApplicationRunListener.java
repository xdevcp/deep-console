package cc.devcp.project.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import static org.springframework.boot.context.logging.LoggingApplicationListener.CONFIG_PROPERTY;
import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;

/**
 * Logging {@link SpringApplicationRunListener} before {@link EventPublishingRunListener} execution
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 0.2.2
 */
public class LoggingSpringApplicationRunListener implements SpringApplicationRunListener, Ordered {

    private static final String DEFAULT_LOGBACK_LOCATION = CLASSPATH_URL_PREFIX + "META-INF/logback-default.xml";

    private static final Logger logger = LoggerFactory.getLogger(LoggingSpringApplicationRunListener.class);

    private final SpringApplication application;

    private final String[] args;

    public LoggingSpringApplicationRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public void starting() {
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        if (!environment.containsProperty(CONFIG_PROPERTY)) {
            System.setProperty(CONFIG_PROPERTY, DEFAULT_LOGBACK_LOCATION);
            if (logger.isInfoEnabled()) {
                logger.info("There is no property named \"{}\" in Spring Boot Environment, " +
                        "and whose value is {} will be set into System's Properties", CONFIG_PROPERTY,
                    DEFAULT_LOGBACK_LOCATION);
            }
        }
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

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
}
