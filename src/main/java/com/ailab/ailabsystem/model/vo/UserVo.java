package com.ailab.ailabsystem.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/11 23:56
 */
@Data
@ApiModel
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 手机号
     */
    private String phone;

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
     * 用户权限（0代表管理员，1代表老师，2代表实验室负责人，3代表实验室成员，4代表实验室合作伙伴，5代表非实验室成员）
     */
    private Integer userRight;

    /**
     * github url
     */
    private String githubUrl;

}
