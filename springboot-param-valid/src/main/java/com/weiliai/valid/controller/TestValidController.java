package com.weiliai.valid.controller;

import com.weiliai.valid.model.User;
import com.weiliai.valid.validenum.ResultEnum;
import com.weiliai.valid.vo.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author: Doug Li
 * @Date 2020/4/21
 * @Describe: 测试参数校验控制层
 */
@RestController
@RequestMapping("/testValid")
public class TestValidController {

    @PostMapping("/userValid")
    public ResponseResult userValid(@Valid @RequestBody User user){
        return ResponseResult.resResult(ResultEnum.SUCCESS);
    }

}
