package com.ailab.ailabsystem.controller;

import cn.hutool.core.util.ObjectUtil;
import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.service.NavigationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value = "网站链接接口", tags = "网站链接接口")
@RestController
@RequestMapping("/navigation")
public class NavigationController {

    @Resource
    private NavigationService navigationService;

    @ApiOperation(value = "获取标签数据接口", notes = "参数传标签的编号，接口返回该标签的数据，不传参数就是默认值")
    @GetMapping("/siteTypeCode")
    public R querySiteBySiteTypeCode(@RequestParam(required = false) Integer siteTypeCode){
        // 默认值为0
        siteTypeCode = ObjectUtil.defaultIfNull(siteTypeCode, 0);
        return navigationService.queryBySiteTypeCode(siteTypeCode);
    }

    @PostMapping("/saveSite")
    public R saveSite(@RequestParam(required = false) Integer siteTypeCode){
        // 默认值为0
        siteTypeCode = ObjectUtil.defaultIfNull(siteTypeCode, 0);
        return navigationService.queryBySiteTypeCode(siteTypeCode);
    }
}
