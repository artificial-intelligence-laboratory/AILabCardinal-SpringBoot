package com.ailab.ailabsystem.service;

import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.model.dto.LoginRequest;
import com.ailab.ailabsystem.model.dto.UserInfoDTO;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

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
    R<Object> login(HttpServletRequest request, LoginRequest loginRequest);

//    /**
//     * 获取所有学生信息列表
//     * @return 本届和往届的学生列表
//     */
//    Map<String, List<UserInfoVo>> getUserInfoList();

//    /**
//     * 获取单个学生信息
//     * @param userInfoId    用户信息表id
//     * @return
//     */
//    UserInfo getUserInfo(Integer userInfoId);

    UserInfo getUserInfoByUserId(Long userId);

    /**
     * 获取首页学生信息
     * @param loginUserKey redis存储用户信息的key
     * @return
     */
    R getIndexUserInfo(String loginUserKey);

    /**
     * 获取用户权限名
     * @param userRight 用户的数字型权限
     * @return
     */
    String getUserRightName(Integer userRight);

    R getInfoOfMe(String loginUserKey);

    R getInfoVoById(Long userId);

    R updateMyInfo(UserInfoDTO userInfoDTO, String token);

    R bindEmail(Long userId, String email, String token);

    R getSimpleUserInfoList();

    boolean currentOrPrevious(String enrollmentYear);

}
