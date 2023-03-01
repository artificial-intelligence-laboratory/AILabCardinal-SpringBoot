package com.ailab.ailabsystem.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 签到接口参数封装类
 *
 * @author xiaozhi
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
    private String checkOutTime;
}
