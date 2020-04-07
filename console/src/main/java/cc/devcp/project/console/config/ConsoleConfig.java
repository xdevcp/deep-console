package cc.devcp.project.console.config;

import cc.devcp.project.common.utils.JacksonUtil;
import cc.devcp.project.core.ControllerMethodsCache;
import cc.devcp.project.core.filter.LoggerFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;

/**
 * @author yshen
 * @author nkorange
 * @since 1.2.0
 */
@Component
@EnableScheduling
@PropertySource("/application.properties")
@Import({LoggerFilter.class})
public class ConsoleConfig {

    @Autowired
    private ControllerMethodsCache methodsCache;

    @PostConstruct
    public void init() {
        methodsCache.initClassMethod("cc.devcp.project.auth.controller");
        methodsCache.initClassMethod("cc.devcp.project.console.*.controller");
        methodsCache.initClassMethod("cc.devcp.project.provider.*.controller");
        methodsCache.initClassMethod("cc.devcp.project.upload.*.controller");
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.setMaxAge(18000L);
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JacksonUtil.objectMapper;
    }
}
