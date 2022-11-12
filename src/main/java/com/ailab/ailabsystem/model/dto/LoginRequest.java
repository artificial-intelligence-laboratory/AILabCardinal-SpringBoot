package com.ailab.ailabsystem.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/12 0:23
 */
@Data
public class LoginRequest {

    @NotBlank(message = "学号不能为空")
    private String studentNumber;

    @NotBlank(message = "密码不能为空")
    private String password;
}
