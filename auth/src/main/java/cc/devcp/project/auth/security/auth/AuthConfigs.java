package cc.devcp.project.auth.security.auth;

import cc.devcp.project.core.env.ReloadableConfigs;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Auth related configurations
 *
 * @author nkorange
 * @since 1.2.0
 */
@Component
@Configuration
public class AuthConfigs {

    @Autowired
    private ReloadableConfigs reloadableConfigs;

    /**
     * secret key
     */
    @Value("${app.core.auth.default.token.secret.key:}")
    private String secretKey;

    /**
     * Token validity time(seconds)
     */
    @Value("${app.core.auth.default.token.expire.seconds:1800}")
    private long tokenValidityInSeconds;

    /**
     * Which auth system is in use
     */
    @Value("${app.core.auth.system.type:}")
    private String authSystemType;

    public String getSecretKey() {
        return secretKey;
    }

    public long getTokenValidityInSeconds() {
        return tokenValidityInSeconds;
    }

    public String getAuthSystemType() {
        return authSystemType;
    }

    public boolean isAuthEnabled() {
        return BooleanUtils.toBoolean(reloadableConfigs.getProperties()
            .getProperty("app.core.auth.enabled", "false"));
    }

    public boolean isCachingEnabled() {
        return BooleanUtils.toBoolean(reloadableConfigs.getProperties()
            .getProperty("app.core.auth.caching.enabled", "true"));
    }

    @Bean
    public FilterRegistrationBean authFilterRegistration() {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(authFilter());
        registration.addUrlPatterns("/*");
        registration.setName("authFilter");
        registration.setOrder(6);

        return registration;
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

}
