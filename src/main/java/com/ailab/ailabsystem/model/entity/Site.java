package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "site")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Site {
    private int siteId;
    private int siteShowSerialNumber;
    private String siteName;
    private String siteUrl;
    private String siteIntro;
    private int siteTypeCode;
    private String siteInfoRemark;
    private int enterUserId;
    private int siteInfoStatus;
}
