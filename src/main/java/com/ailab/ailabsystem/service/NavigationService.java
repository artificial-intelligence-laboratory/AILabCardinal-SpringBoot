package com.ailab.ailabsystem.service;

import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.model.entity.Site;
import com.baomidou.mybatisplus.extension.service.IService;

public interface NavigationService extends IService<Site> {

    R queryBySiteTypeCode(int siteTypeCode);
}
