package com.weiliai.task.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: Doug Li
 * @Date 2020/4/10
 * @Describe: 任务表
 */
@Data
@TableName("sys_job")
public class SysJob {

    /**
     * 数据库自增主键
     */
    private Integer id;

    /**
     * 任务ID
     */
    private Integer jobId;
    /**
     * bean名称 , 如示例中的： demoTask
     */
    private String beanName;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 方法参数
     */
    private String methodParams;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 状态（1正常 0暂停）
     */
    private Integer jobStatus;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;

}
