package com.weiliai.redis.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Doug Li
 * @Date 2021/6/19
 * @Describe: 微博排行榜数据初始化
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class WBTaskDemo {

    public static final String HOUR_KEY = "rank:hour:";

    public static final String DAY_KEY = "rank:day";

    public static final String WEEK_KEY = "rank:week";

    public static final String MONTH_KEY = "rank:month";

    @Resource
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        log.info("初始化最近一个月排行榜数据....");
        final long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        int totalHours = 24 * 30;
        for (int i = 1; i <= totalHours; i++) {
            //倒退过去30天
            String key = HOUR_KEY + (hour - i);
            initMember(key);
        }
        log.info("定时计算开始...");
        new Thread(() -> refreshData()).start();
        new Thread(() -> refreshDataHour()).start();
    }

    /**
     * 初始化某个小时的key
     */
    public void initMember(String key) {
        //采用26个英文字母来实现排行,随机为每个字母生成一个随机数作为score
        final Random random = new Random();
        for (int i = 0; i < 26; i++) {
            //97 代表小写a
            redisTemplate.opsForZSet().add(key, String.valueOf((char) (97 + i)), random.nextInt(10));
        }
    }

    /**
     * 采用26个英文字母来实现排行,随机为每个字母生成一个随机数作为score
     * 模拟人工点赞等操作
     */
    public void refreshHour() {
        //计算当前小时的key
        long currentHour = System.currentTimeMillis() / (1000 * 60 * 60);
        final Random random = new Random();
        //为26个英文字母来实现排行，随机为每个字母生成一个随机数作为score
        for (int i = 0; i < 26; i++) {
            //redis的ZINCRBY 新增这个积分值
            redisTemplate.opsForZSet().incrementScore(HOUR_KEY + currentHour, String.valueOf((char) (97 + i)), random.nextInt(10));
        }
    }

    /**
     * 刷新当天的统计数据
     */
    public void refreshDay() {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        List<String> otherKeys = new ArrayList<>(23); //保留最近23小时的key
        for (int i = 1; i < 24; i++) {
            otherKeys.add(HOUR_KEY + (hour - i));
        }
        //当前的时间key,并后推23个小时，共计近24小时，求出并集存入DAY_KEY中
        redisTemplate.opsForZSet().unionAndStore(HOUR_KEY + hour, otherKeys, DAY_KEY);
        //设置当天的key 40天过期，不然历史数据浪费内存
        for (int i = 0; i < 24; i++) {
            redisTemplate.expire(HOUR_KEY + (hour - i), 40, TimeUnit.DAYS);
        }
        log.info("天刷新完成......");
    }

    /**
     * 刷新7天的统计数据
     */
    public void refreshWeek() {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        int totalHour = 24 * 7;
        List<String> otherKeys = new ArrayList<>(totalHour - 1); //保留最近7天的key
        for (int i = 1; i < totalHour; i++) {
            otherKeys.add(HOUR_KEY + (hour - i));
        }
        redisTemplate.opsForZSet().unionAndStore(HOUR_KEY + hour, otherKeys, WEEK_KEY);
        log.info("周刷新完成......");
    }

    /**
     * 刷新30天的统计数据
     */
    public void refreshMonth() {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        int totalHour = 24 * 7 * 30;
        List<String> otherKeys = new ArrayList<>(totalHour - 1); //保留最近30天的key
        for (int i = 1; i < totalHour; i++) {
            otherKeys.add(HOUR_KEY + (hour - i));
        }
        redisTemplate.opsForZSet().unionAndStore(HOUR_KEY + hour, otherKeys, MONTH_KEY);
        log.info("月刷新完成......");
    }

    /**
     * 定时1小时合并统计天/周/月的排行榜。
     */
    public void refreshData() {
        while (true) {
            //刷新当天的统计数据
            this.refreshDay();
            //刷新7天的统计数据
            this.refreshWeek();
            //刷新30天的统计数据
            this.refreshMonth();
            try {
                TimeUnit.HOURS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 定时5秒钟,微博的热度刷新
     * 例如模拟点赞/收藏/评论的热度值更新
     */
    public void refreshDataHour() {
        while (true) {
            this.refreshHour();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
