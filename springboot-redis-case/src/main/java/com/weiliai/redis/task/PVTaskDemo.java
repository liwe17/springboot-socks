package com.weiliai.redis.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Doug Li
 * @Date 2021/6/14
 * @Describe: 模拟大量PV请求
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class PVTaskDemo {

    public static final String CACHE_PV_LIST = "pv:list";

    public static final String CACHE_ARTICLE = "article:";

    /**
     * Map<时间块，Map<文章Id,访问量>>
     * =Map<2020-01-12 15:30:00到 15:59:00，Map<文章Id,访问量>>
     * =Map<438560，Map<文章Id,访问量>>
     */
    public static final Map<Long, Map<Integer, Integer>> PV_MAP = new ConcurrentHashMap();

    @Resource
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        log.info("模拟大量PV请求 定时器启动....");
        new Thread(this::runArticlePV).start();
    }


    /**
     * 模拟请求
     */
    public void runArticlePV() {
        for (; ; ) {
            batchAddArticle();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对1000篇文章模拟请求
     */
    public void batchAddArticle() {
        for (int i = 0; i < 1000; i++) {
            addPV(i);
        }
    }

    /**
     * 那如何切割时间块呢?如何把当前的时间切入时间块中?
     * 例如,我们要计算"小时块"先把当前的时间转换为为毫秒的时间戳,然后除以一个小时,
     * 即当前时间T/1000*60*60=小时key,然后用这个小时序号作为key.
     * 即当前时间T/1000*60*5=5分钟key,然后用这个序号作为key.
     * <p>
     * <p>
     * 每一次PV操作时,先计算当前时间是那个时间块,然后存储Map中。
     */
    public void addPV(Integer id) {
        long m5 = System.currentTimeMillis() / (1000 * 60 * 1); //1分钟一个时间块
        Map<Integer, Integer> m5Map = PV_MAP.get(m5);
        if (CollectionUtils.isEmpty(m5Map)) {
            m5Map = new ConcurrentHashMap<>();
            m5Map.put(id,1);
            //<5分钟的时间块,Map<文章Id,访问量>>
            PV_MAP.put(m5,m5Map);
        } else {
            //通过文章id 取出浏览量,并访问次数加1
            m5Map.merge(id, 1, Integer::sum);
        }

    }
}
