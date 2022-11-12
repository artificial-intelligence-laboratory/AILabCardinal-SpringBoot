package com.ailab.ailabsystem.enums;

/**
 * @author xiaozhi
 * @description 用户角色枚举类
 * @create 2022-11-2022/11/12 17:56
 */
public enum UserRole {

    /**
     * 根用户
     */
    ROOT(0, "根用户"),

    /**
     * 管理员用户
     */
    ADMIN(1, "管理员用户"),

    /**
     * 普通用户
     */
    USER(2, "普通用户");

    private int code;
    private String roleName;

    UserRole(int code, String roleName) {
        this.code = code;
        this.roleName = roleName;
    }

    public int getCode() {
        return code;
    }

    public String getRoleName() {
        return roleName;
    }
}
