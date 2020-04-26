package com.weiliai.limiter.annotation;

import java.lang.annotation.*;

/**
 * @Author: Doug Li
 * @Date 2020/4/26
 * @Describe: 基于guava实现限流
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface GuavaLimit {

    /**
     * 最多访问限制次数
     */
    int limitNum() default 20;
}
