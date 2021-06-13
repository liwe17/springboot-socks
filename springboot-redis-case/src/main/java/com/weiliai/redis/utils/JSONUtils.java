package com.weiliai.redis.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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


    public static Map<String, Object> object2Map(Object obj) {
        if (Objects.isNull(obj)) return Collections.emptyMap();
        Map<String, Object> objMap = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            boolean accessible = field.isAccessible();
            if (!accessible) field.setAccessible(true);
            try {
                objMap.put(field.getName(),field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(accessible);
        }
        return objMap;
    }

}
