package com.ailab.ailabsystem.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/11 23:56
 */
@Data
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户权限（0为根用户，1为管理员，2为普通用户）
     */
    private Integer userRight;
}
