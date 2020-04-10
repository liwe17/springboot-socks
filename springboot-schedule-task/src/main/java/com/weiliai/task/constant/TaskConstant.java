package com.weiliai.task.constant;

/**
 * @Author: Doug Li
 * @Date 2020/4/10
 * @Describe: 定时任务中使用的常量
 */
public final class TaskConstant {

    //任务状态（1正常 0暂停）
    public static final int NORMAL = 1;

    public static final int PAUSE = 0;

    //任务执行成功0,失败1
    public static final String SUCCESS = "200";

    public static final String FAIL = "501";

    public static final String SUCCESS_MSG ="执行成功!";

    public static final String FAIL_MSG ="执行失败!";

}
