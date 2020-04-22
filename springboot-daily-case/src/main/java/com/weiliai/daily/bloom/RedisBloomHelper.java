package com.weiliai.daily.bloom;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import com.google.common.primitives.Longs;
import lombok.Getter;

/**
 * @Author: Doug Li
 * @Date 2020/4/22
 * @Describe: redis布隆过滤器核心类
 */
@Getter
public class RedisBloomHelper<T> {

    //元素预计数量
    private final int numApproxElements;

    //误差率
    private final double fpp;

    //哈希函数个数
    private int numHashFunctions;

    //bit图长度
    private int bitmapLength;

    private Funnel<T> funnel;

    /**
     * 构造布隆过滤器.注意:在同一业务场景下,三个参数务必相同
     *
     * @param numApproxElements 预估元素数量
     * @param fpp               误差率
     */
    public RedisBloomHelper(int numApproxElements, double fpp) {
        this.numApproxElements = numApproxElements;
        this.fpp = fpp;
        this.bitmapLength = optimalNumOfBits(numApproxElements, fpp);
        this.numHashFunctions = optimalNumOfHashFunctions(numApproxElements, bitmapLength);
        this.funnel = (Funnel<T>) Funnels.stringFunnel(Charsets.UTF_8);
    }

    /**
     * 构造布隆过滤器.注意:在同一业务场景下,三个参数务必相同
     *
     * @param numApproxElements 预估元素数量
     * @param fpp               误差率
     * @param funnel            指定存储的存储类型
     */
    public RedisBloomHelper(int numApproxElements, double fpp, Funnel<T> funnel) {
        this.numApproxElements = numApproxElements;
        this.fpp = fpp;
        this.bitmapLength = optimalNumOfBits(numApproxElements, fpp);
        this.numHashFunctions = optimalNumOfHashFunctions(numApproxElements, bitmapLength);
        this.funnel = funnel;
    }

    //计算最优bit数组长度 方法来自guava
    public int optimalNumOfBits(int numApproxElements, double fpp) {
        if (fpp == 0) {
            fpp = Double.MIN_VALUE;
        }
        return (int) (-numApproxElements * Math.log(fpp) / (Math.log(2) * Math.log(2)));
    }

    //计算最优hash函数个数 方法来自guava
    public int optimalNumOfHashFunctions(int numApproxElements, int bitmapLength) {
        return Math.max(1, (int) Math.round((double) bitmapLength / numApproxElements * Math.log(2)));
    }

    /**
     * 计算一个元素值哈希后映射到Bitmap的哪些bit上
     * Guava的BloomFilterStrategies.32
     */
    public int[] murmurHashOffset(T value) {
        int[] offset = new int[numHashFunctions];

        long hash64 = Hashing.murmur3_128().hashObject(value, funnel).asLong();
        int hash1 = (int) hash64;
        int hash2 = (int) (hash64 >>> 32);
        for (int i = 1; i <= numHashFunctions; i++) {
            int nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            offset[i - 1] = nextHash % bitmapLength;
        }

        return offset;
    }

    /**
     * 计算一个元素值哈希后映射到Bitmap的哪些bit上
     * Guava的BloomFilterStrategies.64
     * @param element 元素值
     * @return bit下标的数组
     */
    public long[] getBitIndices(T element) {
        long[] indices = new long[numHashFunctions];

        byte[] bytes = Hashing.murmur3_128()
                .hashObject(element,funnel).asBytes();
        long hash1 = Longs.fromBytes(
                bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]
        );
        long hash2 = Longs.fromBytes(
                bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]
        );

        long combinedHash = hash1;
        for (int i = 0; i < numHashFunctions; i++) {
            indices[i] = (combinedHash & Long.MAX_VALUE) % bitmapLength;
            combinedHash += hash2;
        }
        return indices;
    }
}
