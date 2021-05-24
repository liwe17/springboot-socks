package com.weiliai.redis.controller;

import com.weiliai.redis.model.Person;
import com.weiliai.redis.service.RedisStringService;
import com.weiliai.redis.utils.JSONUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: Doug Li
 * @Date 2021/5/23
 * @Describe: 字符串场景使用
 */
@RestController
@Api(value = "Redis字符串应用")
@Slf4j
public class RedisStringController {

    @Resource
    private RedisStringService redisStringService;

    @ApiOperation("初始化10条数据")
    @GetMapping(value = "/init")
    public void init() {
        for (int i = 0; i < 10; i++) {
            redisStringService.init(JSONUtils.writeValueAsString(new Person().setId(i).setUserName("user"+i).setAge(1)),"PERSON"+i);
        }
        log.info("初始化完成");
    }

    @ApiOperation("单个用户查询,按id查用户信息")
    @GetMapping(value = "/findById/{id}")
    public Person findById(@PathVariable String id) {
        return JSONUtils.readValue(redisStringService.findById("PERSON"+id),Person.class);
    }

    @ApiOperation("修改某条数据")
    @PostMapping(value = "/updateById")
    public void updateById(@RequestBody Person person) {
        redisStringService.updateById("PERSON"+person.getId(),JSONUtils.writeValueAsString(person));
    }

}
