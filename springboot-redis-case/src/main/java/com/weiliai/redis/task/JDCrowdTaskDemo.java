package com.weiliai.redis.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Doug Li
 * @Date 2021/6/17
 * @Describe: 京豆抽奖初始化
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class JDCrowdTaskDemo {

    public static final String PRIZE_KEY="jd:prize";

    @Resource
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        log.info("京豆抽奖初始化.......");
        if(redisTemplate.hasKey(PRIZE_KEY)) return;
        redisTemplate.opsForSet().add(PRIZE_KEY,prize());
    }


    public List<String> prize() {
        final ArrayList<String> list = new ArrayList<>(100);
        //10个京豆,概率10%
        //5个京豆,概率20%
        //1个京豆,概率60%
        //0个京豆,改了10%
        for (int i = 0; i < 10; i++) {
            list.add("10-" + i);
        }
        for (int i = 0; i < 20; i++) {
            list.add("5-" + i);
        }
        for (int i = 0; i < 60; i++) {
            list.add("1-" + i);
        }
        for (int i = 0; i < 10; i++) {
            list.add("1-" + i);
        }
        return list;
    }
}
