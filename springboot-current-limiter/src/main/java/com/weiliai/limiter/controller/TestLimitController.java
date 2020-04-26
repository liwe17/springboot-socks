package com.weiliai.limiter.controller;

import com.weiliai.limiter.annotation.GuavaLimit;
import com.weiliai.limiter.annotation.Redis2Limit;
import com.weiliai.limiter.annotation.RedisLimit;
import com.weiliai.limiter.vo.ResponseResult;
import com.weiliai.limiter.vo.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Doug Li
 * @Date 2020/4/26
 * @Describe: 测试限流
 */
@Slf4j
@RestController
@RequestMapping("/testLimit")
public class TestLimitController {

    private static AtomicInteger atoInt = new AtomicInteger(0);

    @GetMapping("/testRedisLimit")
    @RedisLimit(key = "test",period = 60,count = 10)
    public ResponseResult testRedisLimit(){
        log.info("当前请求数量为:[{}]",atoInt.incrementAndGet());
        return ResponseResult.resResult(ResultEnum.SUCCESS,atoInt.get());
    }

    @GetMapping("/testRedisLimit2")
    @Redis2Limit
    public ResponseResult testRedisLimit2(){
        log.info("当前请求数量为:[{}]",atoInt.incrementAndGet());
        return ResponseResult.resResult(ResultEnum.SUCCESS,atoInt.get());
    }

    @GetMapping("/testGuavaLimit")
    @GuavaLimit(limitNum = 2)
    public ResponseResult testGuavaLimit(){
        log.info("当前请求数量为:[{}]",atoInt.incrementAndGet());
        return ResponseResult.resResult(ResultEnum.SUCCESS,atoInt.get());
    }
}
