package com.weiliai.valid.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Doug Li
 * @Date 2021/5/18
 * @Describe: Http工具类
 */
@Component
public class HttpUtils {

    @Resource
    private RestTemplate restTemplate;

    /**
     * 发送GET请求
     * @param url 请求URL
     * @param paramMap 参数集合
     * @param responseType 返回实体类型
     * @return 执行结果
     */
    public <T> T executeGet(String url, Map<String,String> paramMap,Class<T> responseType){
        if(CollectionUtils.isEmpty(paramMap))
            return restTemplate.getForObject(url,responseType);
        return restTemplate.getForObject(paramMap2String(url,paramMap),responseType);
    }

    /**
     * map参数按照get方式拼接
     * @param url 请求URL
     * @param paramMap 参数集合
     * @return 拼接URL
     *  例如:
     *      http://wwww.baidu.com?key1=key1&key2=key2&key3=key3&key4=key4
     */
    private static String paramMap2String(String url, Map<String, String> paramMap) {
        if(CollectionUtils.isEmpty(paramMap))
            return url;
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        paramMap.forEach((k,v)-> sb.append("&").append(k).append("=").append(v));
        return sb.toString().replace("?&","?");
    }

    public static void main(String[] args) {
        System.out.println(paramMap2String("http://wwww.baidu.com", new HashMap<String, String>() {{
            put("key1","key1");
            put("key2","key2");
            put("key3","key3");
            put("key4","key4");
        }}));
    }


}
