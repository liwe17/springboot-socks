package com.weiliai.task.exception;

import com.weiliai.task.vo.TaskResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: Doug Li
 * @Date 2020/4/10
 * @Describe: 全局异常拦截器
 */
@RestControllerAdvice
public class ExceptHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptHandler.class);

    @ExceptionHandler(value = {Exception.class})
    public TaskResult exHandler(Exception ex){
        logger.error("控制层拦截到异常,信息:[{}]",ex.getMessage(),ex);
        return TaskResult.fail(ex.getMessage());
    }

}
