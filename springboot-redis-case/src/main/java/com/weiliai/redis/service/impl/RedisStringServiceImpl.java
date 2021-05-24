package com.weiliai.redis.service.impl;

import com.weiliai.redis.dao.CommonDao;
import com.weiliai.redis.service.RedisStringService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Doug Li
 * @Date 2021/5/23
 * @Describe: 实现层
 */
@Service
public class RedisStringServiceImpl implements RedisStringService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public void init(String data, String prefix) {
        CommonDao.STRING_DATA_MAP.put(prefix, data);
        redisTemplate.opsForValue().set(prefix, data, 10, TimeUnit.MINUTES);
    }

    @Override
    public String findById(String id) {
        final String obj = redisTemplate.opsForValue().get(id);
        if (!Objects.isNull(obj))
            return obj;
        return CommonDao.STRING_DATA_MAP.get(id);
    }

    @Override
    public void updateById(String id, String data) {
        CommonDao.STRING_DATA_MAP.put(id, data);
        redisTemplate.opsForValue().set(id, data, 10, TimeUnit.MINUTES);
    }
}
