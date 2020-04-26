package com.weiliai.limiter.annotation;

import java.lang.annotation.*;

/**
 * @Author: Doug Li
 * @Date 2020/4/26
 * @Describe: redis限流注解,适用于lua脚本2注解
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Redis2Limit {

    /**
     * 当前限流的标识,可以是ip,或者在spring cloud系统中,可以是一个服务的serviceID
     */
    String tokensKey() default "tokensKey";

    /**
     *令牌桶刷新的时间戳,后面会被用来计算当前产生的令牌数
     */
    String timestampKey() default "timestampKey";

    /**
     *令牌生产的速率,如每秒产生10个令牌
     */
    int rate() default 1;

    /**
     *令牌桶的容积大小,比如最大100个,那么系统最大可承载100个并发请求
     */
    int capacity() default 1;

    /**
     *当前请求的令牌数量,Spring Cloud Gateway中默认是1,也就是当前请求
     */
    int requested() default 1;

}
