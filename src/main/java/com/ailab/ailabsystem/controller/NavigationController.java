package com.ailab.ailabsystem.controller;

import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.model.entity.SiteType;
import com.ailab.ailabsystem.model.vo.SiteVo;
import com.ailab.ailabsystem.service.NavigationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 伟峰
 */
@Api(value = "网站链接接口", tags = "网站链接接口")
@RestController
@RequestMapping("/navigation")
public class NavigationController {

    @Resource
    private NavigationService navigationService;

    @ApiOperation(value = "获取标签数据接口", notes = "参数传标签的编号，接口返回该标签的数据，不传参数就是默认值")
    @GetMapping("/siteTypeCode")
    public R<List<SiteVo>> querySiteBySiteTypeCode(@RequestParam Integer code){
        Assert.notNull(code, "参数不能为空");
        return navigationService.queryBySiteTypeCode(code);
    }


    @ApiOperation(value = "获取标签类型列表", notes = "获取标签类型")
    @GetMapping("/getLabelTypeList")
    public R<List<SiteType>> getLabelTypeList(){
        List<SiteType> siteTypes = navigationService.queryLabelTypeList();
        return R.success(siteTypes);
    }
}
