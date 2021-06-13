package com.weiliai.redis.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: Doug Li
 * @Date 2021/5/24
 * @Describe: 模拟数据库
 */
public interface CommonDao {

    /**
     * 模拟数据库: key为tableName+id,value为record
     */
    ConcurrentMap<String, String> STRING_DATA_MAP = new ConcurrentHashMap<>();

    /**
     * 模拟数据库: key为tableName+id,value为record
     */
    ConcurrentMap<String, Map<String,Object>> HASH_DATA_MAP = new ConcurrentHashMap<>();
}
