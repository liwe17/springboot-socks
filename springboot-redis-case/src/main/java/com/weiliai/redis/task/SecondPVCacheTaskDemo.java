package com.weiliai.redis.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.weiliai.redis.task.PVTaskDemo.CACHE_PV_LIST;
import static com.weiliai.redis.task.PVTaskDemo.CACHE_ARTICLE;

/**
 * @Author: Doug Li
 * @Date 2021/6/14
 * @Describe: 二级缓存
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class SecondPVCacheTaskDemo {

    @Resource
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        log.info("二级缓存PV 定时器启动....");
        new Thread(this::runCache).start();
    }

    public void runCache() {
        for (; ; ) {
            while (pop()){}
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            log.info("消费二级缓存,定时刷新...");
        }
    }

    public boolean pop() {
        //从redis的list获取一级缓存的记录
        ListOperations<String, Map<Integer, Integer>> listOperations = redisTemplate.opsForList();
        Map<Integer, Integer> map = listOperations.rightPop(CACHE_PV_LIST);
        log.info("弹出pop[{}]", map);
        if (CollectionUtils.isEmpty(map)) return false;
        //先存入数据库,此处省略
        //同步redis的缓存
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            redisTemplate.opsForValue().increment(CACHE_ARTICLE + entry.getKey(), entry.getValue());
        }
        return true;
    }
}
