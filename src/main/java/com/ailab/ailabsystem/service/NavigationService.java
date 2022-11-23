package com.ailab.ailabsystem.service;

import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.model.entity.Site;
import com.ailab.ailabsystem.model.entity.SiteType;
import com.ailab.ailabsystem.model.vo.SiteVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 伟峰
 */
public interface NavigationService extends IService<Site> {

    /**
     * 通过链接类型code查找链接
     * @param siteTypeCode
     * @return
     */
    R<List<SiteVo>> queryBySiteTypeCode(Integer siteTypeCode);

    /**
     * 获取标签类型列表
     * @return
     */
    List<SiteType> queryLabelTypeList();
}
