package com.weiliai.redis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.List;

import static com.weiliai.redis.task.JDCrowdTaskDemo.PRIZE_KEY;
import static com.weiliai.redis.task.TBBlackListTaskDemo.BLACKLIST_KEY;
import static com.weiliai.redis.task.YearCrowdTaskDemo.YEAR_PRIZE;

/**
 * @Author: Doug Li
 * @Date 2021/6/17
 * @Describe: Redis Set应用
 */
@RestController
@Api(value = "Redis list应用")
@Slf4j
@RequestMapping("/set")
@SuppressWarnings("all")
public class RedisSetController {

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation("黑名单校验")
    @GetMapping("/isBlacklist/{userId}")
    public boolean isBlacklist(@PathVariable("userId") Integer userId) {
        return redisTemplate.opsForSet().isMember(BLACKLIST_KEY,userId);
    }

    @ApiOperation("京豆抽奖")
    @GetMapping("/jdPrize")
    public String jdPrize() {
        String member = String.valueOf(redisTemplate.opsForSet().randomMember(PRIZE_KEY));
        if("null".equals(member)){
            return "谢谢参与";
        }
        String result = member.split("-")[0];
        if("0".equals(result)){
            return "谢谢参与";
        }
        return String.format("获得%s个京豆",result);
    }

    @ApiOperation("年会抽奖")
    @GetMapping("/yearPrize/{count}")
    public List<Integer> yearPrize(@PathVariable("count") Integer count) {
        return redisTemplate.opsForSet().pop(YEAR_PRIZE, count);
    }
}
