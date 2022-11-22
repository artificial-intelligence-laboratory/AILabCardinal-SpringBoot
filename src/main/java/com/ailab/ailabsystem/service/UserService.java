package com.ailab.ailabsystem.service;

import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.model.dto.LoginRequest;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.entity.UserInfo;
import com.ailab.ailabsystem.model.vo.UserInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 用户服务
 * @author xiaozhi
 */
public interface UserService extends IService<User> {

    /**
     * 登录
     * @param loginRequest 登录参数
     * @return 用户信息和token
     */
    R<Object> login(LoginRequest loginRequest);

    /**
     * 获取所有学生信息列表
     * @return 本届和往届的学生列表
     */
    Map<String, List<UserInfoVo>> getUserInfoList();

    /**
     * 获取单个学生信息
     * @param userInfoId    用户信息表id
     * @return
     */
    UserInfo getUserInfo(Integer userInfoId);
}
