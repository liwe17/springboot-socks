package com.weiliai.daily.utils;

import com.weiliai.daily.bloom.RedisBloomHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Doug Li
 * @Date 2020/4/22
 * @Describe: redis布隆过滤器工具类
 */
@Component
public class RedisBloomUtils {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 根据给定的布隆过滤器添加值，在添加一个元素的时候使用
     *
     * @param bloomHelper 布隆过滤器对象
     * @param key         redis中的key
     * @param value       存入的值
     */
    public <T> void add(RedisBloomHelper<T> bloomHelper, String key, T value) {
        add(bloomHelper, key, value, 0, null);
    }

    /**
     * 根据给定的布隆过滤器添加值,在添加一批元素的时候使用,批量添加的性能好,
     * 使用pipeline方式(如果是集群下,请使用优化后RedisPipeline的操作)
     *
     * @param bloomHelper 布隆过滤器对象
     * @param key         redis中的key
     * @param values      存入的元素集合
     */
    public <T> void addList(RedisBloomHelper<T> bloomHelper, String key, List<T> values) {
        addList(bloomHelper, key, values, 0, null);
    }


    /**
     * 根据给定的布隆过滤器添加值，在添加一个元素的时候使用
     *
     * @param bloomHelper 布隆过滤器对象
     * @param key         redis中的key
     * @param value       存入的值
     * @param <T>         泛型,可以传入任何类型的value
     * @param timeout     有效时间
     * @param unit        时间单位
     */
    public <T> void add(RedisBloomHelper<T> bloomHelper, String key, T value, long timeout, TimeUnit unit) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value) || null==bloomHelper)
            throw new IllegalArgumentException("redis参数有误");
        int[] offset = bloomHelper.murmurHashOffset(value);
        for (int i : offset) {
            redisTemplate.opsForValue().setBit(key, i, true);
        }
        if (unit != null && timeout > 0) {
            redisTemplate.expire(key, timeout, unit);
        }
    }

    /**
     * 根据给定的布隆过滤器添加值,在添加一批元素的时候使用,批量添加的性能好,
     * 使用pipeline方式(如果是集群下,请使用优化后RedisPipeline的操作)
     *
     * @param bloomHelper 布隆过滤器对象
     * @param key         redis中的key
     * @param values      存入的集合
     * @param <T>         泛型,可以传入任何类型的value
     * @param timeout     有效时间
     * @param unit        时间单位
     */
    public <T> void addList(RedisBloomHelper<T> bloomHelper, String key, List<T> values, long timeout, TimeUnit unit) {
        if (StringUtils.isEmpty(key) || CollectionUtils.isEmpty(values) || null == bloomHelper)
            throw new IllegalArgumentException("redis参数有误");
        redisTemplate.executePipelined((RedisCallback<Long>) connection -> {
            connection.openPipeline();
            for (T value : values) {
                int[] offset = bloomHelper.murmurHashOffset(value);
                for (int i : offset) {
                    connection.setBit(key.getBytes(), i, true);
                }
            }
            return null;
        });
        if (unit != null && timeout > 0) {
            redisTemplate.expire(key, timeout, unit);
        }
    }

    /**
     * 检查元素在集合中是否（可能）存在
     * @param bloomHelper 布隆过滤器对象
     * @param key         redis中的key
     * @param value       存入的数据
     * @param <T>         泛型,可以传入任何类型的value
     * @return 判断结果
     */
    public <T> boolean mayExist(RedisBloomHelper<T> bloomHelper,String key, T value){
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value) || null == bloomHelper)
            throw new IllegalArgumentException("redis参数有误");
        int[] offset = bloomHelper.murmurHashOffset(value);
        for(int i : offset){
            if(!redisTemplate.opsForValue().getBit(key,i)){
                return false;
            }
        }
        return true;
    }

    /**
     * 删除缓存
     * @param key 键值
     */
    public void delete(String key){
        redisTemplate.delete(key);
    }

}