package com.weiliai.task.serivce;

import com.weiliai.task.common.CronTaskRegistrar;
import com.weiliai.task.common.ScheduledRunnable;
import com.weiliai.task.constant.TaskConstant;
import com.weiliai.task.mapper.SysJobMapper;
import com.weiliai.task.model.SysJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: Doug Li
 * @Date 2020/4/10
 * @Describe: 实现CommandLineRunner接口,在项目启动时,运行run方法
 */
@Service
public class SysJobRunner implements CommandLineRunner {


    private static final Logger logger = LoggerFactory.getLogger(SysJobRunner.class);

    @Autowired
    private SysJobMapper sysJobMapper;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    //@Override
    public void run(String... args) throws Exception {
        //初始加载数据库中状态为为正常的定时任务
        List<SysJob> sysJobs =  sysJobMapper.findSysJobsByState(TaskConstant.NORMAL);
        ScheduledRunnable task;
        if(!CollectionUtils.isEmpty(sysJobs)){
            for (SysJob sysJob : sysJobs) {
                task = new ScheduledRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
                cronTaskRegistrar.addCronTask(task,sysJob.getCronExpression());
            }
            logger.info("定时任务已经加载完毕...");
        }
        logger.info("本次启动,未发现定时任务...");
    }
}
