package com.ailab.ailabsystem.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/12 19:52
 */
@Data
@ApiModel
public class SingInRequest {

    /**
     * 任务
     */
    private String task;

    /**
     * 备注
     */
    private String remark;

    /**
     * 签出时间
     */
    private Integer checkOutTime;
}
