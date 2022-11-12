package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "site")
@Data
public class Site implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long siteId;

    /**
     * 显示序号
     */
    private Integer siteShowSerialNumber;

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

    /**
     * 网站类型代码
     */
    private Integer siteTypeCode;

    /**
     * 备注
     */
    private String siteInfoRemark;

    /**
     * 录入用户id
     */
    private Integer enterUserId;

    /**
     * 网站信息状态（0代表被删除，1代表正常）
     */
    private Integer siteInfoStatus;

    /**
     * 网站录入时间
     */
    private Date siteRecordTime;
}
