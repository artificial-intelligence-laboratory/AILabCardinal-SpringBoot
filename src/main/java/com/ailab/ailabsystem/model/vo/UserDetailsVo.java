package com.ailab.ailabsystem.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author huanghuiyuan
 * @ClassName
 * @Description 用来封装UserInfoVo、projectVo以及AwardVo
 * @date 2023/3/4 16:31
 */
@Data
public class UserDetailsVo {

    /**
     * 用户信息
     */
    private UserInfoVo userInfoVo;

    /**
     * 用户项目列表
     */
    private List<ProjectVo> projectVos;

    /**
     * 用户奖项列表
     */
    private List<AwardVo> awardVos;
}
