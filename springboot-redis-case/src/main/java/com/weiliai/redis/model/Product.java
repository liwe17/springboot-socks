package com.weiliai.redis.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Doug Li
 * @Date 2021/5/24
 * @Describe: 商品
 */
@Data
public class Product implements Serializable {

    private Long id;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品价格
     */
    private Integer price;

    /**
     * 产品详情
     */
    private String detail;
}
