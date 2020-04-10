package com.weiliai.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weiliai.task.model.SysJob;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Doug Li
 * @Date 2020/4/10
 * @Describe: 系统任务操作
 */
public interface SysJobMapper extends BaseMapper<SysJob> {


    List<SysJob> findSysJobsByState(@Param("jobStatus") Integer jobStatus);

    @Delete("delete sys_job where job_id = #{jobId}")
    int deleteSysJobsByJobId(@Param("jobId") Integer jobId);

    SysJob findSysJobsByJobId(@Param("jobId") Integer jobId);
}
