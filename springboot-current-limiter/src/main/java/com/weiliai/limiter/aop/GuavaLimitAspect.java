package com.weiliai.limiter.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.weiliai.limiter.annotation.GuavaLimit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Doug Li
 * @Date 2020/4/26
 * @Describe: 基于guava限流切面
 */
@Aspect
//@Component
@Slf4j
public class GuavaLimitAspect {

    //用来存放不同接口的RateLimiter,key为接口名字,value为RateLimiter
    private ConcurrentHashMap<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap();

    @Pointcut(value = "@annotation(com.weiliai.limiter.annotation.GuavaLimit)")
    public void guavaLimitPointCut() {
    }

    @Before("guavaLimitPointCut()")
    public void guavaLimitExecutor(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取注解参数
        GuavaLimit guavaLimit = method.getAnnotation(GuavaLimit.class);
        int limitNum = guavaLimit.limitNum();
        String methodName = method.getName();

        //限流判断
        if(null == rateLimiterMap.get(methodName)){
            rateLimiterMap.putIfAbsent(methodName,RateLimiter.create(limitNum));
        }
        //获取令牌
        if(!rateLimiterMap.get(methodName).tryAcquire()){

            throw new RuntimeException("超过限流数量!");
        }
    }
}
