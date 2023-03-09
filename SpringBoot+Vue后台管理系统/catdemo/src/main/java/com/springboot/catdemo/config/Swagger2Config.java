package com.springboot.catdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置类
 * http://localhost:9090/swagger-ui.html
 * 3.0 http://localhost:9090/swagger-ui/index.html
 */
@Configuration
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(true)
                .forCodeGeneration(false)
                .select()
                // 扫描指定的包
                .apis(RequestHandlerSelectors.basePackage("com.springboot.catdemo.controller"))
                // 对所有的路径监控
                .paths(PathSelectors.any())
                .build();
//                .securitySchemes(securitySchemes())
//                .securityContexts(securityContexts());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("猫寄养后台接口文档")
                .description("猫猫后台管理description")
                .contact(new Contact("can","http://localhost:9090/user","1045857717@qq.com"))
                .version("1.0")
                .build();

    }

//    private List<SecurityScheme> securitySchemes() {
//        // 设置请求头信息
//        List<SecurityScheme> result = new ArrayList<>();
//        ApiKey apiKey = new ApiKey("Authorization","Authorization","Header");
//        result.add(apiKey);
//        return result;
//    }
//
//
//
//    private List<SecurityContext> securityContexts() {
//        // 设置需要登录认证的路径
//        List<SecurityContext> result = new ArrayList<>();
//        result.add(getContextByPath("/hello/.*"));
//        return result;
//    }

//    private SecurityContext getContextByPath(String pathRegex) {
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .forPaths(PathSelectors.regex(pathRegex))
//                .build();
//    }

//    private List<SecurityReference> defaultAuth() {
//        List<SecurityReference> result = new ArrayList<>();
//        AuthorizationScope authorizationScope = new AuthorizationScope("global","accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        result.add(new SecurityReference("Authorization", authorizationScopes));
//        return result;
//    }
}
