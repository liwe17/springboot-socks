package com.weiliai.redis.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: Doug Li
 * @Date 2021/5/23
 * @Describe: Swagger配置类
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger2.enable",havingValue = "true")
public class SwaggerConfiguration {

    //swagger2的配置文件,这里可以配置swagger2的一些基本的内容,比如扫描的包等等
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.weiliai.redis.controller"))
                .build();
    }

    @Bean
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Redis应用实战")
                .contact(new Contact("Doug Li","https://github.com/liwe17","918870689@qq.com"))
                .version("V1.0.0")
                .description("API描述")
                .build();
    }

}
