package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "user_info")
@Data
public class UserInfo implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long userInfoId;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 入学年份
     */
    private Date enrollmentYear;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 加入实验室时间
     */
    private String joinAilabTime;

    /**
     * 开发方向
     */
    private String developmentDirection;

    /**
     * 学院
     */
    private String college;

    /**
     * 专业
     */
    private String major;

    /**
     * github id
     */
    private String githubId;

    /**
     * 宿舍号
     */
    private String dormitoryNumber;
}