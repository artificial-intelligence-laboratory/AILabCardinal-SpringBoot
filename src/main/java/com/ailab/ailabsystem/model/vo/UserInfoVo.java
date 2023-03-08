package com.ailab.ailabsystem.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/11 23:56
 */
@Data
@ApiModel
public class UserInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户信息id √
     */
    private Long userInfoId;

    /**
     * 用户id √
     */
    private Long userId;

    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 真实姓名 √
     */
    private String realName;

    /**
     * 用户头像URL
     */
    private String avatar;

    /**
     * 籍贯 √
     */
    private String nativePlace;

    /** √
     * 加入实验室时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date joinAilabTime;

    /**
     * 开发方向 √
     */
    private String developmentDirection;

    /**
     * 学院 √
     */
    private String college;

    /**
     * 专业班级
     */
    private String majorAndClassNumber;

    /**
     * github id √
     */
    private String githubId;

    /**
     * 入学年份
     */
    private String enrollmentYear;

    /**
     * 用户内网IP
     */
    private List<String> intranetIPs;

}