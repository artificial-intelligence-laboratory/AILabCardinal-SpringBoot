package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName intranet
 */
@TableName(value ="intranet")
@Data
public class Intranet implements Serializable {
    /**
     * 内网IP的id
     */
    @TableId
    private Integer intranetId;

    /**
     * 内网所有者的id
     */
    private Integer userId;

    /**
     * 内网IP
     */
    private String intranetIp;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}