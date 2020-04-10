package com.weiliai.task.common;

import com.weiliai.task.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author: Doug Li
 * @Date 2020/4/10
 * @Describe: Runnable接口实现类
 * 被定时任务线程池调用,用来执行指定bean里面的方法。
 */
public class ScheduledRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledRunnable.class);

    public final String beanName;

    public final String methodName;

    public final String params;

    public ScheduledRunnable(String beanName, String methodName) {
        this(beanName, methodName, null);
    }

    public ScheduledRunnable(String beanName, String methodName, String params) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
    }

    @Override
    public void run() {
        logger.info("定时任务开始执行-bean:[{}], 方法:[{}], 参数:[{}]", beanName, methodName, params);
        long startTime = System.currentTimeMillis();

        try {
            //获取执行的bean对象
            Object target = SpringContextUtils.getBean(beanName);
            //获取要执行的方法
            Method method = null;
            if (StringUtils.isEmpty(params)) {
                method = target.getClass().getMethod(methodName);
            } else {
                method = target.getClass().getMethod(methodName, String.class);
            }
            //调用方法,解除访问限制private
            ReflectionUtils.makeAccessible(method);
            if(StringUtils.isEmpty(params)){
                method.invoke(target);
            }else{
                method.invoke(target,params);
            }
        } catch (Exception ex) {
            logger.error("定时任务执行异常 -bean:[{}], 方法:[{}], 参数:[{}], 异常信息:[{}]", beanName, methodName, params,ex.getMessage(),ex);
        }

        long doTime = System.currentTimeMillis()-startTime;
        logger.info("定时任务执行完成-bean:[{}], 方法:[{}], 参数:[{}], 耗时:[{}]毫秒", beanName, methodName, params,doTime);
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ScheduledRunnable that = (ScheduledRunnable) obj;
        return Objects.equals(this.beanName,that.beanName) && Objects.equals(this.methodName,that.methodName) && Objects.equals(this.params,that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanName, methodName, params);
    }
}
