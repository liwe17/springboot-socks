package com.weiliai.redis.controller;

import com.weiliai.redis.dao.CommonDao;
import com.weiliai.redis.model.Product;
import com.weiliai.redis.utils.JSONUtils;
import com.weiliai.redis.utils.ShortUrlGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: Doug Li
 * @Date 2021/5/24
 * @Describe: hash场景使用
 */
@RestController
@Api(value = "Redis hash应用")
@Slf4j
@RequestMapping("/hash")
public class RedisHashController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String SHORT_URL_KEY = "short:url";

    @Value("${server.port}")
    private int SERVER_PORT;

    @ApiOperation("插入新产品")
    @PostMapping(value = "/insertProduct")
    public void insertProduct(@RequestBody Product obj) {
        //创建商品,先把数据添加到数据库,再存入redis
        //将Object对象里面的属性和值转化成Map对象
        Map<String, Object> map = JSONUtils.object2Map(obj);
        String key = "PRODUCT" + obj.getId();
        CommonDao.HASH_DATA_MAP.put(key, map);
        //批量put操作 putAll 等于 hmset命令
        //String数据结构opsForValue  hash数据结构opsForHash
        redisTemplate.opsForHash().putAll(key, map);
        Object name = redisTemplate.opsForHash().get(key, "name");
        Object price = redisTemplate.opsForHash().get(key, "price");
        Object detail = redisTemplate.opsForHash().get(key, "detail");
        log.info("name:[{}},price:[{}},detail:[{}]", name, price, detail);
    }

    @ApiOperation("根据产品ID更新价格")
    @PostMapping(value = "/updatePriceById")
    public void updatePriceById(@RequestBody Product obj) {
        String key = "PRODUCT" + obj.getId();
        //商品价格涨价 increment等于hincrby命令,商品涨价的金额 obj.getPrice()
        redisTemplate.opsForHash().increment(key, "price", obj.getPrice());
        Object price2 = redisTemplate.opsForHash().get(key, "price");
        log.info("price:[{}]", price2);
    }

    //==========================================短链接=======================================
    /**
     * 长链接转换为短链接
     * 长链接转换为短加密串key,然后存储在redis的hash结构中。
     */
    @ApiOperation("生成短链接")
    @GetMapping(value = "/encodeURL")
    public String encodeURL(String url) {
        //一个长链接url转换为4个短加密串key
        final String shortUrl = ShortUrlGenerator.getShortUrl(url);
        //用hash存储，key=加密串，value=原始url
        this.redisTemplate.opsForHash().put(SHORT_URL_KEY,shortUrl,url);
        return String.format("http://127.0.0.1:%d/%s",SERVER_PORT,shortUrl);
    }

    /**
     * 重定向到原始的URL
     * 通过短加密串KEY到redis找出原始URL,然后重定向出去
     */
    @GetMapping(value = "/{key}")
    public void decodeURL(@PathVariable String key, HttpServletResponse response) {
        //到redis中把原始url找出来
        String url=(String) this.redisTemplate.opsForHash().get(SHORT_URL_KEY,key);
        try {
            //重定向到原始的url
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}
