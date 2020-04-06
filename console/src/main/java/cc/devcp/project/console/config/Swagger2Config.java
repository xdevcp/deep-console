package cc.devcp.project.console.config;

import cc.devcp.project.common.enums.ModuleEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * <pre>
 *     description:
 * </pre>
 *
 * @author deep.wu
 * @version 2020-04-04
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public SecurityScheme securityScheme() {
        return new ApiKey(HttpHeaders.AUTHORIZATION, HttpHeaders.AUTHORIZATION, "header");
    }

    @Bean
    public Docket all() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("all")
            .apiInfo(apiInfo())
            .useDefaultResponseMessages(false)
            .select()
            //自行修改
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(Collections.singletonList(securityScheme()))
            .securityContexts(Collections.singletonList(securityContext()));
    }

    @Bean
    public SecurityContext securityContext() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{
            new AuthorizationScope(ModuleEnum.CONSOLE.name(), ModuleEnum.CONSOLE.name() + " Scope")};
        return SecurityContext.builder()
            .securityReferences(Collections.singletonList(new SecurityReference(HttpHeaders.AUTHORIZATION, authorizationScopes)))
            .forPaths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title(String.format("API-%s 接口文档", ModuleEnum.CONSOLE.name()))
            //服务条款网址
            .version("1.0.0")
            .contact(new Contact("deep-api", "http://devcp.cc", "wupands@163.com"))
            .build();
    }

}
