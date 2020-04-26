package com.weiliai.limiter.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @Author: Doug Li
 * @Date 2020/4/26
 * @Describe: redis配置类
 */
@Configuration
public class RedisConfig {


    /**
     * 设置redis序列化
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        //使用Jackson2JsonRedisSerialize 替换默认序列化
        ObjectMapper objectMapper = new ObjectMapper();
        //设置访问属性
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        redisSerializer.setObjectMapper(objectMapper);
        // 设置序列化
        redisTemplate.setDefaultSerializer(redisSerializer);
        return redisTemplate;
    }
}
