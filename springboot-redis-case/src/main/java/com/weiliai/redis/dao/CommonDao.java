package com.weiliai.redis.dao;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: Doug Li
 * @Date 2021/5/24
 * @Describe: TODO
 */
public interface CommonDao {

    /**
     * 模拟数据库: key为tableName+id,value为record
     */
    ConcurrentMap<String, String> STRING_DATA_MAP = new ConcurrentHashMap<>();
}
