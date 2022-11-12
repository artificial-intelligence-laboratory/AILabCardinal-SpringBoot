package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName(value = "site_type")
@Data
public class SiteType implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long siteTypeId;

    /**
     * 网站类型显示序号
     */
    private Integer siteTypeShowSerialNumber;

    /**
     * 网站类型代码
     */
    private Integer siteTypeCode;

    /**
     * 网站类型代码
     */
    private String siteType;
}