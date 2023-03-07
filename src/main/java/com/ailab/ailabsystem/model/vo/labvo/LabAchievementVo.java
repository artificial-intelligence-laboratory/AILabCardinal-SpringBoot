package com.ailab.ailabsystem.model.vo.labvo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huanghuiyuan
 * @ClassName
 * @Description 封装实验室的奖项以及项目信息
 * @date 2023/3/7 14:22
 */
@Data
public class LabAchievementVo implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 实验室奖项列表
     */
    private List<LabAwardVo> labAwardVoList;

    /**
     * 实验室项目列表
     */
    private List<LabProjectVo> labProjectVoList;
}
