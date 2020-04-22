package com.weiliai.daily.bloom;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @Author: Doug Li
 * @Date 2020/4/22
 * @Describe: 谷歌布隆过滤器的实现
 */
public class GuavaBloomFilterTest {

    //插入数据量
    private static final int INSERT_NUM = 100_0000;

    //期望误判率
    private static double FPP = 0.02;

    public static void main(String[] args) {
        //初始化一个存储String数据的布隆过滤器,默认误判率是0.03
        BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), INSERT_NUM, FPP);

        //用于存放所有实际存在的key,用于存在
        Set<String> set = Sets.newHashSetWithExpectedSize(INSERT_NUM);

        //用于存放所有实际存在的key,用于取出
        List<String> list = Lists.newArrayListWithCapacity(INSERT_NUM);

        //插入随机字符串
        String uuid;
        for (int i = 0; i < INSERT_NUM; i++) {
            uuid = UUID.randomUUID().toString();
            bloomFilter.put(uuid);
            set.add(uuid);
            list.add(uuid);
        }

        //记录正确与错误的数量
        int rightNum = 0;
        int wrongNum = 0;

        //进行判断
        String data;
        for (int i = 0; i < 1_0000; i++) {
            //0-10000之间,可以被100整除的数有100个
            data = i % 100 == 0?list.get(i / 100):UUID.randomUUID().toString();

            //这里用了might,看上去不是很自信,所以如果布隆过滤器判断存在了,我们还要去sets中实锤
            if(bloomFilter.mightContain(data)){
                if(set.contains(data)){
                    rightNum++;
                    continue;
                }
                wrongNum++;
            }
        }

        //计算失误率
        final BigDecimal percent = new BigDecimal(wrongNum).divide(new BigDecimal(9900), 2, RoundingMode.HALF_UP);
        final BigDecimal bingo = new BigDecimal(9900 - wrongNum).divide(new BigDecimal(9900), 2, RoundingMode.HALF_UP);

        System.out.println("在100万个元素中,判断100个实际存在的元素,布隆过滤器认为存在的且真正存的:"+rightNum);
        System.out.println("在100万个元素中,判断9900个不存在的元素,误认为存在的:"+wrongNum+",命中率:"+bingo+",误判率:"+percent);

    }

}
