package com.weiliai.valid.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: Doug Li
 * @Date 2020/4/21
 * @Describe: 用户实体类
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {

    @NotBlank(message = "姓名不为空")
    private String userName;

    @NotBlank(message = "密码不为空")
    private String password;

    //嵌套必须加@Valid,否则嵌套中的验证不生效
    @Valid
    @NotNull(message = "用户信息不能为空")
    private UserInfo userInfo;



}
