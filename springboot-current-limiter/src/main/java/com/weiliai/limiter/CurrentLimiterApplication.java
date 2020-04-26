package com.weiliai.limiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Author: Doug Li
 * @Date 2020/4/21
 * @Describe: 限流应用启动器
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class CurrentLimiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrentLimiterApplication.class,args);
    }

}
