package com.ailab.ailabsystem.controller;

import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.service.NavigationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/navigation")
public class NavigationController {

    @Resource
    private NavigationService navigationService;

    @GetMapping("/siteTypeCode={siteTypeCode}")
    public R querySiteBySiteTypeCode(@PathVariable("siteTypeCode") int siteTypeCode){
        return navigationService.queryBySiteTypeCode(siteTypeCode);
    }
}
