package com.ailab.ailabsystem.model.vo;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

@Data
public class IndexUserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 权限名
     */
    private String roleName;

    /**
     * 项目数量
     */
    private Integer projectCount;

    /**
     * 比赛数量
     */
    private Integer competitionCount;

    /**
     * 奖项数量
     */
    private Integer awardCount;

    /**
     * 专利数量;
     */
    private Integer patentNumber;
}
