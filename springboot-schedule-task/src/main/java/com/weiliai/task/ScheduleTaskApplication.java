package com.weiliai.task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: Doug Li
 * @Date 2020/4/10
 * @Describe: 定时任务项目启动类
 */
@EnableScheduling //启动定时任务
@MapperScan("com.weiliai.task.mapper") //扫描mapper文件
@SpringBootApplication
public class ScheduleTaskApplication {


    public static void main(String[] args) {
        SpringApplication.run(ScheduleTaskApplication.class,args);
    }

}
