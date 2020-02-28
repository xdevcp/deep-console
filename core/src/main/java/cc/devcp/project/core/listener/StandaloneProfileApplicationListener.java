package cc.devcp.project.core.listener;

import cc.devcp.project.core.utils.Constants;
import cc.devcp.project.core.utils.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;


/**
 * Standalone {@link Profile} {@link ApplicationListener} for {@link ApplicationEnvironmentPreparedEvent}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see ConfigurableEnvironment#addActiveProfile(String)
 * @since 0.2.2
 */
public class StandaloneProfileApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>,
        PriorityOrdered {

    private static final Logger logger = LoggerFactory.getLogger(StandaloneProfileApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {

        ConfigurableEnvironment environment = event.getEnvironment();

        if (environment.getProperty(Constants.STANDALONE_MODE_PROPERTY_NAME, boolean.class, true)) {
            environment.addActiveProfile(Constants.STANDALONE_SPRING_PROFILE);
        }

        if (logger.isInfoEnabled()) {
            logger.info("Spring Environment's active profiles : {} in standalone mode : {}",
                    Arrays.asList(environment.getActiveProfiles()),
                    SystemUtils.STANDALONE_MODE
            );
        }

    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
