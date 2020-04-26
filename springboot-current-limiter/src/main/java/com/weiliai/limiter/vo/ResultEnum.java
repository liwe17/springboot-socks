package com.weiliai.limiter.vo;

import lombok.Getter;

/**
 * @Author: Doug Li
 * @Date 2020/4/21
 * @Describe: 自定义枚举
 */
@Getter
public enum ResultEnum {

    SUCCESS(200,"执行成功"),
    PARAM_ERROR(400,"请求参数有误!"),
    UNKNOWN_ERROR(500,"未知错误!");

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
