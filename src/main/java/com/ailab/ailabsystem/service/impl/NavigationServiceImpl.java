package com.ailab.ailabsystem.service.impl;

import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.mapper.SiteMapper;
import com.ailab.ailabsystem.model.entity.Site;
import com.ailab.ailabsystem.service.NavigationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class NavigationServiceImpl extends ServiceImpl<SiteMapper, Site> implements NavigationService {

    @Resource
    private SiteMapper siteMapper;

    /**
     * 根据网站代码id查询对应类型网站列表
     * @param siteTypeCode 网站类型id
     * @return
     */
    @Override
    public R queryBySiteTypeCode(@RequestBody int siteTypeCode) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("site_type_code",siteTypeCode);
        List<Site> sites = siteMapper.selectByMap(map);
        System.out.println(sites);

        if (sites.size() == 0){
            return R.error("数据库中未查询到信息");
        }

        return R.success(sites);
    }

}
