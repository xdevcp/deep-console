package cc.devcp.project.provider.config;

import cc.devcp.project.common.exception.CommExceptionHandler;
import cc.devcp.project.common.utils.JacksonUtil;
import cc.devcp.project.core.filter.LoggerFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <pre>
 *     description:
 * </pre>
 *
 * @author deep.wu
 * @version 2020-04-04
 */
@Configuration
@ServletComponentScan
@Import({CommExceptionHandler.class, LoggerFilter.class})
public class ProviderWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false)
            .setUseTrailingSlashMatch(false);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JacksonUtil.objectMapper;
    }

}
