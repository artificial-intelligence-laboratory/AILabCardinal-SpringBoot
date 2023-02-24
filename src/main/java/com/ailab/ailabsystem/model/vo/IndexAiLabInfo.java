package com.ailab.ailabsystem.model.vo;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

@Data
public class IndexAiLabInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成员数量
     */
    private Integer memberCount;

    /**
     * 项目数量
     */
    private Integer projectCount;

    /**
     * 奖项数量
     */
    private Integer AwardCount;
}
