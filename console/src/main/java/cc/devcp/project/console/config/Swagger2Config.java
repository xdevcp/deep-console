package cc.devcp.project.console.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
@Profile(value = {"local", "dev", "test","prod"})
public class Swagger2Config {

    @Bean
    public SecurityScheme securityScheme() {
        return new ApiKey(HttpHeaders.AUTHORIZATION, HttpHeaders.AUTHORIZATION, "header");
    }

    @Bean
    public SecurityContext securityContext(){
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{new AuthorizationScope("master", "Master Scope")};
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference(HttpHeaders.AUTHORIZATION, authorizationScopes)))
                .forPaths(PathSelectors.any())
                .build();
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

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CPD-MASTER 接口文档")
                //服务条款网址
                .version("1.0.0")
                .contact(new Contact("DataValuable", "http://WWW.DataValuable.COM", "zhiyuan.li@datavaluable.com"))
                .build();
    }

}
