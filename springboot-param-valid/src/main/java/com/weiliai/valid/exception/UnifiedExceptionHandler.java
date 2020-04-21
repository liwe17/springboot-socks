package com.weiliai.valid.exception;

import com.weiliai.valid.validenum.ResultEnum;
import com.weiliai.valid.vo.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;
import javax.validation.ValidationException;
import java.util.List;

/**
 * @Author: Doug Li
 * @Date 2020/4/21
 * @Describe: 统一异常处理
 */
@Slf4j
@RestControllerAdvice
public class UnifiedExceptionHandler {


    /**
     * 异常处理器
     * @param e 忽略参数异常,缺少请求体异常
     * @return ResponseResult
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public ResponseResult paramOrBodyMissExceptionHandler(Exception e) {
        log.error("参数或请求体异常:信息:[{}]", e.getMessage(), e);
        return ResponseResult.resResult(ResultEnum.PARAM_ERROR);
    }

    /**
     * 缺省参数验证异常
     * @param e 参数验证异常
     * @return ResponseResult
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ValidationException.class,MethodArgumentNotValidException.class})
    public ResponseResult defaultParamExceptionHandler(MethodArgumentNotValidException e) {
        log.error("缺省参数验证异常:信息:[{}]", e.getMessage(), e);
        //获取异常信息
        BindingResult exceptions = e.getBindingResult();
        if (exceptions.hasErrors()) {
            //判断异常中是否有错误信息,如果存在就是用异常中的信息,没有使用默认信息
            List<ObjectError> allErrors = exceptions.getAllErrors();
            if(!allErrors.isEmpty()){
                //这里列出了全部错误参数,按正常逻辑只需要第一条即可
                FieldError fieldError = (FieldError) allErrors.get(0);
                return ResponseResult.resResult(ResultEnum.PARAM_ERROR.getCode(),fieldError.getDefaultMessage());
            }
        }
        return ResponseResult.resResult(ResultEnum.PARAM_ERROR);
    }


    /**
     * 自定义参数验证异常
     * @param e 参数验证异常
     * @return ResponseResult
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ParamException.class})
    public ResponseResult paramException(ParamException e){
        log.error("自定义参数验证异常:信息:[{}]", e.getMessage(), e);
        //判断错误中是否有异常信息,如果存在就是用异常中的消息,否则使用默认信息
        if(StringUtils.isEmpty(e.getMessage())){
            return ResponseResult.resResult(ResultEnum.PARAM_ERROR);
        }
        return ResponseResult.resResult(ResultEnum.PARAM_ERROR.getCode(),e.getMessage());
    }

    /**
     * 未知异常
     * @param e 未知异常
     * @return ResponseResult
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {Exception.class})
    public ResponseResult unKnownException(Exception e){
        log.error("未知参数验证异常:信息:[{}]", e.getMessage(), e);
        //判断错误中是否有异常信息,如果存在就是用异常中的消息,否则使用默认信息
        if(StringUtils.isEmpty(e.getMessage())){
            return ResponseResult.resResult(ResultEnum.UNKNOWN_ERROR);
        }
        return ResponseResult.resResult(ResultEnum.UNKNOWN_ERROR.getCode(),e.getMessage());
    }

}
