package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huiyuan
 * @TableName project
 */
@TableName(value ="project")
@Data
public class Project implements Serializable {
    /**
     * 项目id
     */
    @TableId(type = IdType.AUTO)
    private Integer projectId;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 奖项级别（0代表国家级、1代表省级、2代表校级）
     */
    private Integer level;

    /**
     * 年度
     */
    private String year;

    /**
     * 指导老师
     */
    private String teacher;

    /**
     * 项目状态（0代表未完成、1代表已完成）
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}