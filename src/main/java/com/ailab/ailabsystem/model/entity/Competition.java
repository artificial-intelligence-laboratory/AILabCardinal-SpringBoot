package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huiyuan
 * @TableName competition 比赛表
 */
@TableName(value ="competition")
@Data
public class Competition implements Serializable {
    /**
     * 比赛id
     */
    @TableId(type = IdType.AUTO)
    private Integer competitionId;

    /**
     * 比赛名
     */
    private String competitionName;

    /**
     * 年度
     */
    private String year;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}