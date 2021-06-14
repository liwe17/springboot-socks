package com.weiliai.redis.task;

import com.weiliai.redis.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Doug Li
 * @Date 2021/6/13
 * @Describe: 模拟jhs定时任务
 */
@Component
@SuppressWarnings("all")
@Slf4j
public class JhsTaskDemo {

    private static final String JHS_KEY = "jhs:key";

    private static final String JHS_KEY_A = "jhs:key:A";

    private static final String JHS_KEY_B = "jhs:key:B";

    @Resource
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init(){
        log.info("聚划算 定时器启动...");
//        new Thread(this::runSimpleJhs).start();
        new Thread(this::runJhs).start();
    }

    /**
     * 精简版,存在缓存击穿
     * 在删除缓存之后到设置新值之间将导致请求穿透到数据库
     */
    public void runSimpleJhs(){
        for(;;){
            List<Product> products = products();
            redisTemplate.delete(JHS_KEY);
            redisTemplate.opsForList().leftPushAll(products);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("runJhs定时刷新....");
        }
    }

    /**
     * 解决缓存击穿
     */
    public void runJhs(){
        for(;;){
            List<Product> products = products();
            redisTemplate.delete(JHS_KEY_A);
            redisTemplate.opsForList().leftPushAll(JHS_KEY_A,products);
            redisTemplate.delete(JHS_KEY_B);
            redisTemplate.opsForList().leftPushAll(JHS_KEY_B,products);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("runJhs定时刷新....");
        }
    }

    public List<Product> products(){
        List<Product> products = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            products.add(new Product().setId(Long.valueOf(String.valueOf(i))).setName(String.valueOf(i)).setPrice(i));
        }
        return products;
    }

}
