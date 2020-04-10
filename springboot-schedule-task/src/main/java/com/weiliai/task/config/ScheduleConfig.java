package com.weiliai.task.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @Author: Doug Li
 * @Date 2020/4/10
 * @Describe: 定时任务线程池配置
 */
@Configuration
public class ScheduleConfig {

    @Value("${schedule.pool-size}")
    private int poolSize;

    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        //设置线程池配置
        taskScheduler.setPoolSize(poolSize);
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("TaskSchedulerThreadPool-");
        return taskScheduler;
    }


}
