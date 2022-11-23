package com.ailab.ailabsystem.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.common.RedisKey;
import com.ailab.ailabsystem.mapper.SiteMapper;
import com.ailab.ailabsystem.mapper.SiteTypeMapper;
import com.ailab.ailabsystem.model.entity.Site;
import com.ailab.ailabsystem.model.entity.SiteType;
import com.ailab.ailabsystem.model.vo.SiteVo;
import com.ailab.ailabsystem.service.NavigationService;
import com.ailab.ailabsystem.util.RedisOperator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 伟峰
 */
@Service
public class NavigationServiceImpl extends ServiceImpl<SiteMapper, Site> implements NavigationService {

    @Resource
    private SiteMapper siteMapper;

    @Resource
    private SiteTypeMapper siteTypeMapper;

    @Resource
    private RedisOperator redis;

    /**
     * 根据网站代码id查询对应类型网站列表
     * @param siteTypeCode 网站类型id
     * @return
     */
    @Override
    public R<List<SiteVo>> queryBySiteTypeCode(@RequestBody Integer siteTypeCode) {
        Map<String, Object> map = new HashMap<>();
        List<SiteVo> siteVos = null;
        // 查缓存，没有查数据库
        String labelKey = RedisKey.getLabelContent(siteTypeCode);
        String siteJson = redis.get(labelKey);
        if (StringUtils.isBlank(siteJson)) {
            map.put("site_type_code", siteTypeCode);
            List<Site> sites = siteMapper.selectByMap(map);
            siteVos = BeanUtil.copyToList(sites, SiteVo.class);
            redis.set(labelKey, JSONUtil.toJsonStr(siteVos));
        } else {
            siteVos = JSONUtil.toList(siteJson, SiteVo.class);
        }
        return R.success(siteVos);
    }


    @Override
    public List<SiteType> queryLabelTypeList() {
        QueryWrapper<SiteType> wrapper = new QueryWrapper<>();
        // 根据显示序号排序
        wrapper.orderByAsc("site_type_show_serial_number");
        return siteTypeMapper.selectList(wrapper);
    }
}
