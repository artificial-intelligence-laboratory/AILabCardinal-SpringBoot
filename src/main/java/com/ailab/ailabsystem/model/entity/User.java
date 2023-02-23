package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author xiaozhi
 */
@TableName(value = "user")
@Data
public class User implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 密码
     */
    private String password;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户权限（0代表管理员，1代表老师，2代表实验室负责人，3代表实验室成员，4代表非实验室成员）
     */
    private Integer userRight;

    /**
     * 最后一次上线时间
     */
    private LocalDateTime lastOnlineTime;

    /**
     * 最后一次上线的ip
     */
    private String lastOnlineIp;

    /**
     * 最后一次上线的ip
     */
    private String lastOnlineIpAddress;

    /**
     * 状态码（0代表账号被禁用，1代表正常，默认为1）
     */
    private Integer userStatus;

    /**
     * 用户信息
     */
    @TableField(exist = false)
    private UserInfo userInfo;
}
