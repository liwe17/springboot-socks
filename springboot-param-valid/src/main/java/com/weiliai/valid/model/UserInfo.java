package com.weiliai.valid.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

/**
 * @Author: Doug Li
 * @Date 2020/4/21
 * @Describe: 用户信息实体
 */
@Data
@Accessors(chain = true)
public class UserInfo {

    @NotBlank(message = "年龄不为空")
    @Max(value = 18,message = "不能超过18岁")
    private String age;

    @NotBlank(message = "性别不为空")
    private String gender;

}
