package com.ailab.ailabsystem.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/12 0:23
 */
@Data
@ApiModel
public class SaveSiteRequest {

    @NotBlank(message = "学号不能为空")
    private String siteName;

    @NotBlank(message = "url不能为空")
    private String siteUrl;

    @NotBlank(message = "链接信息")
    private String siteIntro;

}
