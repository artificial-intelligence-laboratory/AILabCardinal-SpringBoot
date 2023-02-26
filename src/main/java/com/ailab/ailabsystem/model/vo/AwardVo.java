package com.ailab.ailabsystem.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AwardVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 比赛名
     */
    private String competitionName;

    /**
     * 比赛等级
     */
    private String level;

    /**
     * 获奖时间
     */
    private Date awardTime;
}
