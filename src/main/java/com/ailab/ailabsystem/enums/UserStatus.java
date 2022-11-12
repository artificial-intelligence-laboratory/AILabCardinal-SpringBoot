package com.ailab.ailabsystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaozhi
 * @description 用户状态
 * @create 2022-11-2022/11/12 0:32
 */
@AllArgsConstructor
@Getter
public enum UserStatus {

    /**
     * 0：禁用
     */
    DISABLE(0, "禁用"),
    NORMAL(1, "正常");

    /**
     * 1：正常
     */

    private int code;

    private String status;

}
