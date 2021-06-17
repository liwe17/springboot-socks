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
 * @Describe: 类似年会抽奖
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class YearCrowdTaskDemo {

    public static final String YEAR_PRIZE = "year:prize";

    @Resource
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init(){
        log.info("年会抽奖初始化....");
        if(redisTemplate.hasKey(YEAR_PRIZE)) return;
        redisTemplate.opsForSet().add(YEAR_PRIZE,prize());
    }

    /**
     * 模拟100个用户来抽奖list存放的是用户id
     * 例如支付宝参与抽奖,就把用户id加入set集合中
     * 例如公司抽奖,把公司所有的员工,工号都加入到set集合中
     */
    public List<Integer> prize() {
        List<Integer> list=new ArrayList<>(100);
        for(int i=0;i<100;i++){
            list.add(i);
        }
        return list;
    }
}
