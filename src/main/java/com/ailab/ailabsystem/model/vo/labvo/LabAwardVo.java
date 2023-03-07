package com.ailab.ailabsystem.model.vo.labvo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huanghuiyuan
 * @ClassName
 * @Description 实验室官网成就奖项
 * @date 2023/3/7 13:12
 */
@Data
public class LabAwardVo implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 比赛名
     */
    private String competitionName;

    /**
     * 比赛等级
     */
    private String level;

    /**
     * 奖项名
     */
    private String awardName;

    /**
     * 角色（0代表小组负责人、1代表小组成员、2代表独狼）
     */
    private String role;
}
