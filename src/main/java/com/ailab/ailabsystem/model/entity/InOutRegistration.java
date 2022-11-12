package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaozhi
 * @description 签到记录类
 * @create 2022-11-2022/11/11 23:15
 */
@TableName(value = "in_out_registration")
@Data
public class InOutRegistration implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long signInId;

    /**
     *  签到时间
     */
    private Date signInTime;

    /**
     * 签到者姓名
     */
    private String signInUserRealName;

    /**
     * 签到者班级
     */
    private Integer signInUserClass;

    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 任务
     */
    private String task;

    /**
     * 签到时间
     */
    private Date checkOutTime;

    /**
     * 备注
     */
    private String remark;

}
