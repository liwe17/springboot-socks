package com.weiliai.task.common;

import org.springframework.stereotype.Component;

/**
 * @Author: Doug Li
 * @Date 2020/4/10
 * @Describe: 该类用于测试定时任务类
 */
@Component
public class DemoTask {

    public void taskWithParams(String params){
        System.err.println("执行有参实例任务:"+params);
    }

    public void taskNoParams(){
        System.err.println("执行无参实例任务.");
    }

}
