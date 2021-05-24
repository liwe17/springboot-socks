package com.weiliai.redis.service;

/**
 * @Author: Doug Li
 * @Date 2021/5/23
 * @Describe: redis字符串应用场景
 */
public interface RedisStringService {

    /**
     * 初始化数据
     */
    void init(String data,String prefix);

    /**
     * 按照ID查询
     */
    String findById(String id);

    /**
     * 按照ID更新
     */
    void updateById(String s, String data);
}
