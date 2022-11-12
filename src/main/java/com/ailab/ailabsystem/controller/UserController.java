package com.ailab.ailabsystem.controller;

import com.ailab.ailabsystem.common.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/12 10:35
 */
@Api(value = "用户服务接口", tags = "用户服务接口")
@RequestMapping("/user")
@RestController
public class UserController {

    @GetMapping("/test")
    public R hello() {
        return R.success("你好");
    }
}
