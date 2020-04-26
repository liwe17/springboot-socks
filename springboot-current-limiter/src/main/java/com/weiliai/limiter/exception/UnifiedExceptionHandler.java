package com.weiliai.limiter.exception;

import com.weiliai.limiter.vo.ResponseResult;
import com.weiliai.limiter.vo.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: Doug Li
 * @Date 2020/4/21
 * @Describe: 统一异常处理
 */
@Slf4j
@RestControllerAdvice
public class UnifiedExceptionHandler {


    /**
     * 未知异常
     * @param e 未知异常
     * @return ResponseResult
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {Exception.class})
    public ResponseResult unKnownException(Exception e){
        log.error("未知参数验证异常:信息:[{}]", e.getMessage(), e);
        return ResponseResult.resResult(ResultEnum.UNKNOWN_ERROR.getCode(),e.getMessage());
    }

}
