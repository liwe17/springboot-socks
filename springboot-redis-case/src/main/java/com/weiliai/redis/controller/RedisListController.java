package com.weiliai.redis.controller;

import com.weiliai.redis.model.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import static com.weiliai.redis.task.JHSTaskDemo.*;
import static com.weiliai.redis.task.PVTaskDemo.CACHE_ARTICLE;

/**
 * @Author: Doug Li
 * @Date 2021/6/13
 * @Describe: Redis List应用
 */
@RestController
@Api(value = "Redis list应用")
@Slf4j
@RequestMapping("/list")
@SuppressWarnings("all")
public class RedisListController {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 模拟定时任务刷新首页数据
     * com.weiliai.redis.task.JhsTaskDemo
     */
    @ApiOperation("聚划算简单版本")
    @GetMapping("/findSimple/{page}/{size}")
    public List<Product> findSimple(@PathVariable("page") int page, @PathVariable("size") int size) {
        List<Product> products;
        long start = (page - 1) * size;
        long end = page * size - 1;
        products = redisTemplate.opsForList().range(JHS_KEY, start, end);
        //查询数据库,此处默认返回空
        if (CollectionUtils.isEmpty(products)) products = Collections.emptyList();
        return products;
    }

    @ApiOperation("聚划算版本-解决缓存击穿")
    @GetMapping("/find/{page}/{size}")
    public List<Product> find(@PathVariable("page") int page, @PathVariable("size") int size) {
        List<Product> products;
        long start = (page - 1) * size;
        long end = page * size - 1;
        products = redisTemplate.opsForList().range(JHS_KEY_A, start, end);
        //查询数据库,此处默认返回空
        if (CollectionUtils.isEmpty(products)) products = redisTemplate.opsForList().range(JHS_KEY_B, start, end);
        return products;
    }

    //==========================微信PV====================================

    @ApiOperation("PV查询当前阅读量")
    @GetMapping(value = "/view/{id}")
    public String view(@PathVariable("id") Integer id) {
        //调用redis的get命令
        String n = stringRedisTemplate.opsForValue().get(CACHE_ARTICLE + id);
        log.info("key[{}],阅读量为[{}]", id, n);
        return n;
    }


}
