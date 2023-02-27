package com.ailab.ailabsystem.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel
public class UserSimpleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 姓名称
     */
    private String realName;

    /**
     * 年级
     */
    private String grade;

    /**
     * 开发方向
     */
    private String developmentDirection;

    /**
     * 用户权限（0代表管理员，1代表老师，2代表实验室负责人，3代表实验室成员，4代表实验室合作伙伴，5代表非实验室成员）
     */
    private Integer userRight;


}
