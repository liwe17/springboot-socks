package com.weiliai.valid.vo;

import com.weiliai.valid.constant.CommonConstant;
import com.weiliai.valid.validenum.ResultEnum;
import lombok.*;

/**
 * @Author: Doug Li
 * @Date 2020/4/21
 * @Describe: 响应结果集
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseResult<T> {

    private Integer code;
    private String message;
    private T data;

    public static <T> ResponseResult<T> resResult (ResultEnum resultEnum){
        return new ResponseResult<T>(resultEnum.getCode(),resultEnum.getMessage(),null);
    }

    public static <T> ResponseResult<T> resResult (ResultEnum resultEnum,T t){
        return new ResponseResult<T>(resultEnum.getCode(),resultEnum.getMessage(),t);
    }

    public static <T> ResponseResult<T> resResult (Integer code,String message){
        return new ResponseResult<T>(code,message,null);
    }

    public static <T> ResponseResult<T> resResult (Integer code,String message,T t){
        return new ResponseResult<T>(code,message,t);
    }
}
