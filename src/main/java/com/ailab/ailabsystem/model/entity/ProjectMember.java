package com.ailab.ailabsystem.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huiyuan
 * @TableName project_member
 */
@TableName(value ="project_member")
@Data
public class ProjectMember implements Serializable {
    /**
     * 项目与用户关系id
     */
    @TableId(type = IdType.AUTO)
    private Integer projectUserId;

    /**
     * 项目id
     */
    private Integer projectId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色（0代表负责人、1代表普通成员）
     */
    private Integer role;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}