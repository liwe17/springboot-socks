package com.weiliai.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.weiliai.redis.task.WBTaskDemo.*;

/**
 * @Author: Doug Li
 * @Date 2021/6/19
 * @Describe: Redis ZSet应用
 */
@RestController
@Api(value = "Redis zset应用")
@Slf4j
@RequestMapping("/zset")
@SuppressWarnings("all")
public class RedisZSetController {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 模拟排行榜,数据初始化
     *
     * @see com.weiliai.redis.task.WBTaskDemo
     */
    @ApiOperation("获取小时热度榜")
    @GetMapping(value = "/getHour")
    public Set getHour() {
        long hour = System.currentTimeMillis() / (1000 * 60 * 60);
        //ZREVRANGE 返回有序集key中,指定区间内的成员,降序
        Set<ZSetOperations.TypedTuple<Integer>> rang = redisTemplate.opsForZSet().reverseRangeWithScores(HOUR_KEY + hour, 0, 30);
        return rang;
    }

    @ApiOperation("获取天热度榜")
    @GetMapping(value = "/getDay")
    public Set getDay() {
        Set<ZSetOperations.TypedTuple<Integer>> rang = redisTemplate.opsForZSet().reverseRangeWithScores(DAY_KEY, 0, 30);
        return rang;
    }

    @ApiOperation("获取周热度榜")
    @GetMapping(value = "/getWeek")
    public Set getWeek() {
        Set<ZSetOperations.TypedTuple<Integer>> rang = redisTemplate.opsForZSet().reverseRangeWithScores(WEEK_KEY, 0, 30);
        return rang;
    }

    @ApiOperation("获取月热度榜")
    @GetMapping(value = "/getMonth")
    public Set getMonth() {
        Set<ZSetOperations.TypedTuple<Integer>> rang = redisTemplate.opsForZSet().reverseRangeWithScores(MONTH_KEY, 0, 30);
        return rang;
    }


    public static final String HOTEL_KEY = "hotel";

    //======================附近的人/酒店==========================================
    @ApiOperation("数据初始化")
    @GetMapping(value = "/init")
    public void init() {
        Map<String, Point> map = new HashMap<>();
        map.put("世界之窗", new Point(113.9807127428, 22.5428248089));
        map.put("南山威尼斯酒店", new Point(113.9832042690, 22.5408496326));
        map.put("福田喜来登酒店", new Point(114.0684865267, 22.5412294122));
        map.put("大梅沙海景酒店", new Point(114.3135524539, 22.5999265998));
        map.put("南山新年酒店", new Point(113.9349465491, 22.5305488659));
        map.put("深圳华强广场酒店", new Point(114.0926367279, 22.5497917634));
        redisTemplate.opsForGeo().add(HOTEL_KEY, map);
    }

    @ApiOperation("获取成员的hash值")
    @GetMapping(value = "/hash/{member}")
    public String hash(@PathVariable("member") String member) {
        //geohash算法生成的base32编码值
        List<String> list= this.redisTemplate.opsForGeo().hash(HOTEL_KEY,member);
        return list.get(0);
    }

    @ApiOperation("获取成员的经纬度")
    @GetMapping(value = "/position/{member}")
    public Point position(@PathVariable("member") String member) {
        //geohash算法生成的base32编码值
        List<Point> list= this.redisTemplate.opsForGeo().position(HOTEL_KEY,member);
        return list.get(0);
    }

    @ApiOperation("获取两地间的距离")
    @GetMapping(value = "/distance/{member1}/{member2}")
    public Distance distance(@PathVariable("member1") String member1,@PathVariable("member2") String member2) {
        Distance distance= this.redisTemplate.opsForGeo().distance(HOTEL_KEY,member1,member2, RedisGeoCommands.DistanceUnit.KILOMETERS);
        return distance;
    }

    @ApiOperation("通过经度,纬度查找附近的")
    @GetMapping(value = "/radiusByxy")
    public GeoResults radiusByxy() {
        //这个坐标是腾讯大厦位置
        Circle circle = new Circle(113.9410499639, 22.5461508801, Metrics.KILOMETERS.getMultiplier());
        //返回腾讯大厦附近50条
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults= this.redisTemplate.opsForGeo().radius(HOTEL_KEY,circle, args);
        return geoResults;
    }

    @ApiOperation("通过地方查找附近")
    @GetMapping(value = "/radiusByMember")
    public GeoResults radiusByMember() {
        String member="世界之窗";
        //返回50条
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
        //半径10公里内
        Distance distance=new Distance(10, Metrics.KILOMETERS);
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults= this.redisTemplate.opsForGeo().radius(HOTEL_KEY,member, distance,args);
        return geoResults;
    }

}
