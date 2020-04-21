package com.weiliai.valid.exception;

/**
 * @Author: Doug Li
 * @Date 2020/4/21
 * @Describe: 自定义参数异常
 */
public class ParamException extends RuntimeException {

    public ParamException() {
    }

    public ParamException(String message) {
        super(message);
    }
}
