package com.weiliai.task.common;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Doug Li
 * @Date 2020/4/10
 * @Describe: 可参考spring提供的定时任务注册类
 * {@link org.springframework.scheduling.config.ScheduledTaskRegistrar}
 */
@Component
public class CronTaskRegistrar implements DisposableBean {

    /**
     * 缓存
     */
    private final Map<Runnable,ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(16);

    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * 添加一个任务
     * @param task 要执行的任务
     * @param cronExpression 定时cron的表达式
     */
    public void addCronTask(Runnable task,String cronExpression){
        addCronTask(new CronTask(task,cronExpression));
    }

    /**
     * 相同的任务替换,并保存到集合中
     * @param cronTask 定时任务
     */
    private void addCronTask(CronTask cronTask) {
        Assert.notNull(cronTask,"定时任务不能为空");
        Runnable task = cronTask.getRunnable();
        if(this.scheduledTasks.containsKey(task)){
            scheduledTasks.remove(task);
        }
        this.scheduledTasks.put(task,scheduleCronTask(cronTask));

    }

    public ScheduledTask scheduleCronTask(CronTask cronTask) {
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return scheduledTask;
    }

    /**
     * 移除任务
     * @param task
     */
    public void removeCronTask(Runnable task) {
        ScheduledTask scheduledTask = this.scheduledTasks.remove(task);
        if (scheduledTask != null)
            scheduledTask.cancel();
    }

    @Override
    public void destroy() throws Exception {
        for (ScheduledTask scheduledTask : scheduledTasks.values()) {
            scheduledTask.cancel();
        }
        this.scheduledTasks.clear();
    }
}
