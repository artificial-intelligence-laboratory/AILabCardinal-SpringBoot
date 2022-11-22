package com.ailab.ailabsystem.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/11 23:56
 */
@Data
public class UserInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userInfoId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 开发方向
     */
    private String developmentDirection;

    /**
     * 班级
     */
    private Integer classNumber;

    /**
     * github url
     */
    private String githubUrl;

    /**
     * 年级
     */
    private String grade;
}