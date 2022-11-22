package com.ailab.ailabsystem.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/13 15:22
 */
@Data
public class SiteVo implements Serializable {

    private Long siteId;

    /**
     * 网站名
     */
    private String siteName;

    /**
     * 链接
     */
    private String siteUrl;

    /**
     * 简介
     */
    private String siteIntro;

}
