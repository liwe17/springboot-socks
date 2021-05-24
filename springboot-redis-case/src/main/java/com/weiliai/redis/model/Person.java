package com.weiliai.redis.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: Doug Li
 * @Date 2021/5/23
 * @Describe: 实体
 */
@Data
@Accessors(chain = true)
public class Person implements Serializable {

    private Integer id;

    private String userName;

    private Integer age;
}
