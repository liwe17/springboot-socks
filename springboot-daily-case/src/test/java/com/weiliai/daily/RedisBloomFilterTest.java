package com.weiliai.daily;

import com.google.common.collect.Lists;
import com.weiliai.daily.bloom.RedisBloomHelper;
import com.weiliai.daily.utils.RedisBloomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: Doug Li
 * @Date 2020/4/22
 * @Describe: 测试启动类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisBloomFilterTest {

    @Autowired
    private RedisBloomUtils redisBloomUtils;

    @Test
    public void testRedisBloom() {
        String key = "bloom";
        //删除缓存的key
        redisBloomUtils.delete(key);
        //预计元素个数
        int expectedInsertions = 1000;
        //误差率
        double fpp = 0.2;
        //记录误判的个数
        int j = 0;
        RedisBloomHelper<String> bloomHelper = new RedisBloomHelper<>(expectedInsertions,fpp);
        //存储记录的元素
        List<String> list = Lists.newArrayListWithCapacity(expectedInsertions);
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i));
        }
        long beginTime = System.currentTimeMillis();
        redisBloomUtils.addList(bloomHelper,key,list);
        long costTime = System.currentTimeMillis()-beginTime;
        System.err.println("布隆过滤器添加100个值,耗时:"+costTime+"毫秒!");
        for (int i = 0; i < expectedInsertions; i++) {
            if(redisBloomUtils.mayExist(bloomHelper,key,String.valueOf(i))){
                //记录误判个数
                if(!list.contains(String.valueOf(i))){
                    j++;
                }
            }
        }
        System.err.println("布隆过滤器,误判了"+j+"个,验证结果耗时:"+(System.currentTimeMillis()-beginTime)+"毫秒!");

    }

}
