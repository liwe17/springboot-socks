package com.weiliai.redis.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @Author: Doug Li
 * @Date 2021/5/24
 * @Describe: JSON序列化工具类
 */
public class JSONUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(); //线程安全


    public static String writeValueAsString(Object object) {
        String jsonStr = null;
        try {
            jsonStr = OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return OBJECT_MAPPER.readValue(content, valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
