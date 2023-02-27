package com.ailab.ailabsystem.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class ProjectVo implements Serializable {

    /**
     * 项目名
     */
    private String projectName;

    /**
     * 年度
     */
    private String year;

    /**
     * 奖项级别（0代表国家级、1代表省级、2代表校级）
     */
    private String level;

    /**
     * 项目成员角色
     */
    private String projectRole;

    /**
     * 项目状态（0代表未完成、1代表已完成）
     */
    private String status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
