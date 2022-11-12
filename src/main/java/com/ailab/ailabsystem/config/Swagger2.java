package com.ailab.ailabsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author xiaozhi
 * @description
 * @create 2022-10-2022/10/12 15:02
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    //    http://localhost:8080/swagger-ui.html     原路径
    //    http://localhost:8080/doc.html            新路径

    @Value("${custom.swagger.enable}")
    private boolean enableSwagger;

    // 配置swagger2核心配置 docket
    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)  // 指定api类型为swagger2
                .apiInfo(apiInfo())                 // 用于定义api文档汇总信息
                // 是否开启，生成环境关闭
                .enable(enableSwagger)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ailab.ailabsystem.controller"))
                // 所有请求路径
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("枢纽API")                       // 文档页标题
                .contact(new Contact("ailab-system",
                        "http://www.ailab-system.org",
                        "123@qq.com"))                   // 联系人信息
                .description("为枢纽系统提供的API")      // 详细信息
                .version("1.0.0")                               // 文档版本号
                .termsOfServiceUrl("http://www.ailab-system.org")     // 网站地址
                .build();
    }
}
