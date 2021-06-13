package com.weiliai.redis.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;

/**
 * @Author: Doug Li
 * @Date 2021/6/13
 * @Describe: 短链接工具类
 * 注: 不保证不重复,重复率较低
 */
public class ShortUrlGenerator {

    //26+26+10=62
    public static final String[] CHARS = new String[]{"a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};


    /**
     * 一个长链接URL转换为4个短KEY
     */
    public static String[] shortUrl(String url) {
        //对地址进行md5获取32位字符串
        String md5Hex = DigestUtils.md5Hex(url);
        System.out.println(md5Hex);
        String[] resUrl = new String[4];
        for (int i = 0; i < 4; i++) {
            //取出8位字符串,md5 32位,被切割为4组,每组8个字符
            String sTempSubString = md5Hex.substring(i * 8, i * 8 + 8);

            //先转换为16进制,然后用0x3FFFFFFF进行位与运算,目的是格式化截取30位
            // 0011 1111 1111 1111 1111 1111 1111 1111 
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            StringBuilder outChars = new StringBuilder();
            for (int j = 0; j < 6; j++) {
                //0x0000003D代表什么意思？他的10进制是61,61代表chars数组长度62的0到61的坐标.
                //0x0000003D & lHexLong进行位与运算,就是格式化为6位,即61内的数字
                //保证了index绝对是61以内的值
                long index = 0x0000003D & lHexLong; //获取下标值

                outChars.append(CHARS[(int) index]);
                //每次循环按位移5位,因为30位的二进制,分6次循环,即每次右移5位
                lHexLong = lHexLong >> 5;
            }

            // 把字符串存入对应索引的输出数组
            resUrl[i] = outChars.toString();
        }
        return resUrl;
    }

    /**
     * 获取随机URL
     */
    public static String getShortUrl(String url) {
        final Random random = new Random();
        return shortUrl(url)[random.nextInt(4)];
    }

    public static void main(String[] args) {
        // 长连接
        String longUrl = "https://detail.tmall.com/item.htm?id=597254411409";
        // 转换成的短链接后6位码,返回4个短链接
        String[] shortCodeArray = shortUrl(longUrl);

        for (String s : shortCodeArray) {
            // 任意一个都可以作为短链接码
            System.out.println(s);
        }
    }

}
