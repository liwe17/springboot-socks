package com.weiliai.limiter.annotation;

import java.lang.annotation.*;

/**
 * @Author: Doug Li
 * @Date 2020/4/26
 * @Describe: redis限流注解,适用于lua脚本1注解
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedisLimit {

    /**
     * 资源名称
     */
    String name() default "";


    /**
     * 资源的key
     */
    String key() default "";

    /**
     * 资源的前缀
     */
    String prefix() default "";

    /**
     * 给定时间段
     */
    int period();

    /**
     * 最多访问限制次数
     */
    int count();

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.CUSTOMER;

    enum LimitType {

        /**
         * 自定义key
         */
        CUSTOMER,

        /**
         * 根据IP
         */
        IP;
    }

}
