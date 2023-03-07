package com.ailab.ailabsystem.controller;

import com.ailab.ailabsystem.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.mapping.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huanghuiyuan
 * @ClassName
 * @Description 用于返回版本号
 * @date 2023/3/5 16:15
 */
@Api(value = "版本控制接口", tags = "版本控制接口")
@RestController
public class VersionController {

    @Value("${ailabcardinal.version}")
    private String version;

    @ApiOperation(value = "用于返回版本号信息")
    @GetMapping("/version")
    public String getVersion() {
        return version;
    }


}
