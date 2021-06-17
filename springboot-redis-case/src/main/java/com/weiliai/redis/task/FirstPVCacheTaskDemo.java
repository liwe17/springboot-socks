package com.weiliai.redis.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.weiliai.redis.task.PVTaskDemo.CACHE_PV_LIST;
import static com.weiliai.redis.task.PVTaskDemo.PV_MAP;
/**
 * @Author: Doug Li
 * @Date 2021/6/14
 * @Describe: 一级缓存
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class FirstPVCacheTaskDemo {

    @Resource
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        log.info("一级缓存PV 定时器启动....");
        new Thread(this::runCache).start();
    }

    public void runCache() {
        for (; ; ) {
            this.consumePV();
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("消费一级缓存,定时刷新...");
        }
    }

    public void consumePV() {
        long m5 = System.currentTimeMillis() / (1000 * 60 * 1); //1分钟一个时间块
        for (Map.Entry<Long, Map<Integer, Integer>> entry : PV_MAP.entrySet()) {
            if (m5 <= entry.getKey()) {
                continue;
            }
            //如果小于当前时间块,则先消费
            Map<Integer, Integer> m5Map = entry.getValue();
            redisTemplate.opsForList().leftPush(CACHE_PV_LIST, m5Map);
            //后移除
            PV_MAP.remove(m5);
        }
    }
}
