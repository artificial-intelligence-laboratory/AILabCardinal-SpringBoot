package com.ailab.ailabsystem.service.impl;

import com.ailab.ailabsystem.enums.ResponseStatusEnum;
import com.ailab.ailabsystem.exception.CustomException;
import com.ailab.ailabsystem.mapper.InOutRegistrationMapper;
import com.ailab.ailabsystem.mapper.UserMapper;
import com.ailab.ailabsystem.model.entity.InOutRegistration;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.vo.InOutRegistrationVo;
import com.ailab.ailabsystem.service.SignInService;
import com.ailab.ailabsystem.util.GenerateTimeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/12 19:47
 */
@Service
public class SignInServiceImpl extends ServiceImpl<InOutRegistrationMapper, InOutRegistration> implements SignInService {

    @Resource
    private UserMapper userMapper;

    @Override
    public List<InOutRegistrationVo> generateSignInRecord(Date startTime,
                                                          Date endTime,
                                                          Integer num) {
        // 获取所有未毕业的成员
        List<User> userList = userMapper.selectAllNotGraduatedUser();
        if (num > userList.size()) {
            throw new CustomException(ResponseStatusEnum.NUM_ERROR);
        }
        // 生成随机时间
        List<InOutRegistrationVo> dateList =
                GenerateTimeUtil.getDateList(startTime, endTime, num, userList);
        return dateList;
    }
}
