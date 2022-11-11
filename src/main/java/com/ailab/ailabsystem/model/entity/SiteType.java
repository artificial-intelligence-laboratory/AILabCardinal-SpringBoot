package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "site_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteType {
    private int siteTypeId;
    private int siteTypeShowSerialNumber;
    private int siteTypeCode;
    private String siteType;
}