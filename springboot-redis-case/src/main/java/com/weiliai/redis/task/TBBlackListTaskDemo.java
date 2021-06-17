package com.weiliai.redis.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Doug Li
 * @Date 2021/6/17
 * @Describe: 淘宝黑名单任务筛选
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class TBBlackListTaskDemo {

    public  static final String BLACKLIST_KEY="blacklist";

    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init(){
        log.info("淘宝黑名单初始化....");
        redisTemplate.opsForSet().add(BLACKLIST_KEY,blackList());
    }

    public List<Integer> blackList(){
        ArrayList<Integer> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        return list;
    }

}
