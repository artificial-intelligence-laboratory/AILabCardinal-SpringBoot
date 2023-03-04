package com.ailab.ailabsystem.util;

import cn.hutool.core.util.ObjectUtil;
import com.ailab.ailabsystem.enums.ResponseStatusEnum;
import com.ailab.ailabsystem.exception.CustomException;
import com.ailab.ailabsystem.model.dto.LoginRequest;
import com.ailab.ailabsystem.model.dto.UserInfoDTO;
import com.ailab.ailabsystem.model.entity.UserInfo;
import com.ailab.ailabsystem.model.vo.UserInfoVo;

public class CheckDataUtil {

    public static void checkLoginRequest(LoginRequest loginUser) {
        if (ObjectUtil.isNull(loginUser.getStudentNumber()) || ObjectUtil.isNull(loginUser.getPassword())) {
            throw new CustomException(ResponseStatusEnum.STUDENT_NUMBER_OR_PASSWORD_NULL);
        }
    }

    public static void checkUserInfo(UserInfoVo userInfoVo) {
        if (ObjectUtil.isNull(userInfoVo.getUserInfoId())) {
            throw new CustomException(ResponseStatusEnum.PARAMS_ERROR);
        }
    }

    public static void checkId(Long id) {
        if (ObjectUtil.isNull(id)) {
            throw new CustomException(ResponseStatusEnum.PARAMS_ERROR);
        }
    }

    public static void checkUserInfoDTO(UserInfoDTO userInfoDTO) {
        if (ObjectUtil.isNull(userInfoDTO.getUserInfoId())) {
            throw new CustomException(ResponseStatusEnum.PARAMS_ERROR);
        }
        String enrollmentYear = userInfoDTO.getEnrollmentYear();
        if (enrollmentYear != null) {
            Integer value = Integer.valueOf(enrollmentYear);
            if(value <  TimeUtil.getPrefixEnrollmentYear() || value > TimeUtil.getSysYear()) {
                throw new CustomException(ResponseStatusEnum.ENROLLMENTYEAR_ERROR);
            }
        }
    }
}
