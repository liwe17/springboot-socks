package com.weiliai.limiter.aop;

import com.google.common.collect.ImmutableList;
import com.weiliai.limiter.annotation.Redis2Limit;
import com.weiliai.limiter.annotation.RedisLimit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Doug Li
 * @Date 2020/4/26
 * @Describe: redis限流切面
 */
@Aspect
@Component
@Slf4j
public class RedisLimitAspect {

    //获取IP有可能获取到的常量
    private static final String UNKNOWN = "unknown";

    //待执行的lua脚本
    private final static String REDIS_SCRIPT = buildLuaScript();

    //待执行的lua2脚本
    private final static String REDIS_SCRIPT2 = buildLuaScript2();

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut(value = "@annotation(com.weiliai.limiter.annotation.RedisLimit)")
    public void redisLimitPointCut() {
    }

    //执行lua脚本1
    //@Before("redisLimitPointCut()")
    public void redisLimitExecutor(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedisLimit redisLimit = method.getAnnotation(RedisLimit.class);
        RedisLimit.LimitType limitType = redisLimit.limitType();
        int limitPeriod = redisLimit.period();
        int limitCount = redisLimit.count();

        String key;
        switch (limitType) {
            case CUSTOMER:
                key = redisLimit.key();
                break;
            case IP:
                key = getIpAddress();
                break;
            default:
                key = "";
                break;
        }
        //执行lua脚本1
        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(redisLimit.prefix(), key));
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(REDIS_SCRIPT, Long.class);
        Long count = (Long) redisTemplate.execute(redisScript, keys, limitCount, limitPeriod);
        log.info("当前并发数量为:[{}],限流数量为:[{}]",count,limitCount);
        if(count ==null || count.intValue()>limitCount){
            throw new RuntimeException("超过限流数量!");
        }

    }

    /**
     * 构建lua脚本
     * @return lua脚本
     */
    private static String buildLuaScript() {
        StringBuilder lua = new StringBuilder();
        lua.append("local c")
                .append("\nc = redis.call('get', KEYS[1])")
                // 调用超过最大值，则直接返回
                .append("\nif c and tonumber(c) > tonumber(ARGV[1]) then")
                .append("\nreturn c;")
                .append("\nend")
                // 执行计算器自加
                .append("\nc = redis.call('incr', KEYS[1])")
                .append("\nif tonumber(c) == 1 then")
                // 从第一次调用开始限流，设置对应键值的过期
                .append("\nredis.call('expire', KEYS[1], ARGV[2])")
                .append("\nend")
                .append("\nreturn c;");
        return lua.toString();
    }



    @Pointcut(value = "@annotation(com.weiliai.limiter.annotation.Redis2Limit)")
    public void redis2LimitPointCut() {
    }

    @Before("redis2LimitPointCut()")
    public void redisLimitExecutor2(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Redis2Limit redis2Limit = method.getAnnotation(Redis2Limit.class);
        DefaultRedisScript<List> redisScript2 = new DefaultRedisScript<>(REDIS_SCRIPT2, List.class);
        List<String> keys = Arrays.asList(redis2Limit.tokensKey(), redis2Limit.timestampKey());
        Object[] args = new Object[]{redis2Limit.rate(), redis2Limit.capacity(),
                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()),
                redis2Limit.requested()};
        List<Long> resultList = (List<Long>)redisTemplate.execute(redisScript2,keys,args);
        Assert.notNull(resultList,"resultList不能为null");
        if(resultList.get(0)==0){
            throw new RuntimeException("超过限流数量!");
        }else{
            log.info("令牌桶中剩余数量为:[{}]",resultList.get(1));
        }
    }

    /**
     * local tokens_key = KEYS[1] 当前限流的标识,可以是ip,或者在spring cloud系统中,可以是一个服务的serviceID
     * local timestamp_key = KEYS[2] 令牌桶刷新的时间戳,后面会被用来计算当前产生的令牌数
     *
     * local rate = tonumber(ARGV[1]) 令牌生产的速率,如每秒产生50个令牌
     * local capacity = tonumber(ARGV[2]) 令牌桶的容积大小,比如最大100个,那么系统最大可承载100个并发请求
     * local now = tonumber(ARGV[3]) 当前时间戳
     * local requested = tonumber(ARGV[4]) 当前请求的令牌数量,Spring Cloud Gateway中默认是1,也就是当前请求
     *
     * -- 计算填满桶需要多长时间
     * local fill_time = capacity/rate
     * -- 得到填满桶的2倍时间作为redis中key时效的时间,避免冗余太多无用的key
     * local ttl = math.floor(fill_time*2) 令牌的有效期
     *
     *
     * -- 获取桶中剩余的令牌，如果桶是空的，就将他填满
     * local last_tokens = tonumber(redis.call("get", tokens_key))
     * if last_tokens == nil then
     *   last_tokens = capacity
     * end
     *
     * -- 获取当前令牌桶最后的刷新时间,如果为空,则设置为0
     * local last_refreshed = tonumber(redis.call("get", timestamp_key))
     * if last_refreshed == nil then
     *   last_refreshed = 0
     * end
     *
     * -- 计算最后一次刷新令牌到当前时间的时间差
     * local delta = math.max(0, now-last_refreshed)
     *
     * -- 计算当前令牌数量，这个地方是最关键的地方，通过剩余令牌数 + 时间差内产生的令牌得到当前总令牌数量
     * local filled_tokens = math.min(capacity, last_tokens+(delta*rate))
     *
     * --设置标识allowad接收当前令牌桶中的令牌数是否大于请求的令牌结果
     * local allowed = filled_tokens >= requested
     *
     * --设置当前令牌数量
     * local new_tokens = filled_tokens
     *
     * --如果allowed为true,则将当前令牌数量重置为桶中的令牌数-请求的令牌数,并且设置allowed_num标识为1
     * local allowed_num = 0
     * if allowed then
     *   new_tokens = filled_tokens - requested
     *   allowed_num = 1
     * end
     *
     *--将当前令牌数量写回到redis中，并重置令牌桶的最后刷新时间
     * redis.call("setex", tokens_key, ttl, new_tokens)
     * redis.call("setex", timestamp_key, ttl, now)
     *
     * --返回当前是否申请到了令牌，以及当前桶中剩余多少令牌
     * return { allowed_num, new_tokens }
     *
     * https://www.cnblogs.com/myseries/p/12634560.html
     * Spring网关中是基于令牌桶+redis实现的网关分布式限流，具体的实现见下面两个代码：
     * lua脚本地址：resources/META-INF/scripts/request_rate_limiter.lua
     * RedisRateLimiter：gateway/filter/ratelimit/RedisRateLimiter.java
     *
     * 通过返回值【0】是否等于1来判断本次流量是否通过，返回值【1】为令牌桶中剩余的令牌数。就上面这段代码没有看到任何令牌桶算法的
     * 影子对吧，所有的精华实现都在request_rate_limiter.lua脚本里面
     * https://gist.github.com/ptarjan/e38f45f2dfe601419ca3af937fff574d
     */
    private static String buildLuaScript2(){
        StringBuilder lua = new StringBuilder();
        lua.append("\nlocal tokens_key = KEYS[1]")
                .append("\nlocal timestamp_key = KEYS[2]")
                .append("\nlocal rate = tonumber(ARGV[1])")
                .append("\nlocal capacity = tonumber(ARGV[2])")
                .append("\nlocal now = tonumber(ARGV[3])")
                .append("\nlocal requested = tonumber(ARGV[4])")
                .append("\nlocal fill_time = capacity/rate")
                .append("\nlocal ttl = math.floor(fill_time*2)")
                .append("\nlocal last_tokens = tonumber(redis.call(\"get\", tokens_key))")
                .append("\nif last_tokens == nil then")
                .append("\nlast_tokens = capacity")
                .append("\nend")
                .append("\nlocal last_refreshed = tonumber(redis.call(\"get\", timestamp_key))")
                .append("\nif last_refreshed == nil then")
                .append("\nlast_refreshed = 0")
                .append("\nend")
                .append("\nlocal delta = math.max(0, now-last_refreshed)")
                .append("\nlocal filled_tokens = math.min(capacity, last_tokens+(delta*rate))")
                .append("\nlocal allowed = filled_tokens >= requested")
                .append("\nlocal new_tokens = filled_tokens")
                .append("\nlocal allowed_num = 0")
                .append("\nif allowed then")
                .append("\nnew_tokens = filled_tokens - requested")
                .append("\nallowed_num = 1")
                .append("\nend")
                .append("\nredis.call(\"setex\", tokens_key, ttl, new_tokens)")
                .append("\nredis.call(\"setex\", timestamp_key, ttl, now)")
                .append("\nreturn { allowed_num, new_tokens }");
        return lua.toString();
    }



    public String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
