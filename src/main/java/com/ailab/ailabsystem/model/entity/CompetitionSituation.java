package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName competition_situation
 */
@TableName(value ="competition_situation")
@Data
public class CompetitionSituation implements Serializable {
    /**
     * 比赛参加情况id
     */
    @TableId(type = IdType.AUTO)
    private Integer competitionSituationId;

    /**
     * 比赛id
     */
    private Integer competitionId;

    /**
     * 用户id
     */
    private Integer userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}