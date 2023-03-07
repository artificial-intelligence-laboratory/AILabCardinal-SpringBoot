package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huiyuan
 * @TableName award 奖项表
 */
@TableName(value ="award")
@Data
public class Award implements Serializable {
    /**
     * 奖项id
     */
    @TableId(type = IdType.AUTO)
    private Integer awardId;

    /**
     * 比赛id
     */
    private Integer competitionId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 奖项级别（0代表国家级、1代表省级、2代表校级）
     */
    private Integer awardLevel;

    /**
     * 奖项名
     */
    private String awardName;

    /**
     * 获奖时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date awardTime;

    /**
     * 角色（0代表小组负责人、1代表小组成员、2代表独狼）
     */
    private Integer role;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}