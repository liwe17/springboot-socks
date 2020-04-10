package com.weiliai.task.controller;

import com.weiliai.task.common.CronTaskRegistrar;
import com.weiliai.task.common.ScheduledRunnable;
import com.weiliai.task.constant.TaskConstant;
import com.weiliai.task.mapper.SysJobMapper;
import com.weiliai.task.model.SysJob;
import com.weiliai.task.vo.TaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @Author: Doug Li
 * @Date 2020/4/10
 * @Describe: 任务控制层
 */
@RestController
public class TaskController {

    @Autowired
    private SysJobMapper sysJobMapper;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;


    /**
     * 添加一个定时任务,如果为任务为正常状态,则加入到定时任务中
     *
     * @param sysJob 新增任务实体
     * @return 处理结果
     */
    @PostMapping("/addTask")
    public TaskResult addCronTask(@RequestBody SysJob sysJob) {
        if (sysJobMapper.insert(sysJob) != 1) return TaskResult.fail();
        if (Objects.equals(sysJob.getJobStatus(), TaskConstant.NORMAL)) {
            ScheduledRunnable task = new ScheduledRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
            //注册定时任务
            cronTaskRegistrar.addCronTask(task, sysJob.getCronExpression());
        }
        return TaskResult.ok();
    }

    /**
     * 删除一个定时任务
     *
     * @param jobId 任务id
     * @return 处理结果
     */
    @DeleteMapping("/delTask/{id}")
    public TaskResult delCronTask(@PathVariable("id") Integer jobId) {
        //获取待删除任务信息
        SysJob sysJob = sysJobMapper.findSysJobsByJobId(jobId);
        Assert.notNull(sysJob, "不存在的任务");
        sysJobMapper.deleteSysJobsByJobId(jobId);
        ScheduledRunnable task = new ScheduledRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
        cronTaskRegistrar.removeCronTask(task);
        return TaskResult.ok(jobId);
    }

    /**
     * 更新任务的定时器
     *
     * @param sysJob 具体任务
     * @return 执行结果
     */
    @PutMapping("/updateTask/{id}")
    public TaskResult updateCronTask(@PathVariable("id") Integer jobId, @RequestBody SysJob sysJob) {
        //获取待更新任务信息
        SysJob dbSysJob = sysJobMapper.findSysJobsByJobId(jobId);
        Assert.notNull(dbSysJob, "不存在的任务[" + jobId + "]");
        //更新指定字段,例如只是定时器
        dbSysJob.setCronExpression(sysJob.getCronExpression());
        sysJobMapper.updateById(dbSysJob);
        //删除任务
        ScheduledRunnable oldTask = new ScheduledRunnable(dbSysJob.getBeanName(), dbSysJob.getMethodName(), dbSysJob.getMethodParams());
        cronTaskRegistrar.removeCronTask(oldTask);
        //重新添加
        ScheduledRunnable newTask = new ScheduledRunnable(dbSysJob.getBeanName(), dbSysJob.getMethodName(), dbSysJob.getMethodParams());
        cronTaskRegistrar.addCronTask(newTask, dbSysJob.getCronExpression());
        return TaskResult.ok(jobId);
    }

    /**
     * 暂停定时任务
     *
     * @param jobId 具体任务id
     * @return 执行结果
     */
    @PutMapping("triggerTask/${id}")
    public TaskResult triggerCronTask(@PathVariable("id") Integer jobId){
        //获取待更新任务信息
        SysJob dbSysJob = sysJobMapper.findSysJobsByJobId(jobId);
        Assert.notNull(dbSysJob, "不存在的任务[" + jobId + "]");
        //更新指定字段,例如只是定时器
        dbSysJob.setJobStatus(TaskConstant.PAUSE);
        sysJobMapper.updateById(dbSysJob);
        //删除任务
        ScheduledRunnable oldTask = new ScheduledRunnable(dbSysJob.getBeanName(), dbSysJob.getMethodName(), dbSysJob.getMethodParams());
        cronTaskRegistrar.removeCronTask(oldTask);
        return TaskResult.ok(jobId);
    }


}
