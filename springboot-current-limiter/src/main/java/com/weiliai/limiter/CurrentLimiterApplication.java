package com.weiliai.limiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Doug Li
 * @Date 2020/4/21
 * @Describe: 限流应用启动器
 */
@SpringBootApplication
public class CurrentLimiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrentLimiterApplication.class,args);
    }

}
