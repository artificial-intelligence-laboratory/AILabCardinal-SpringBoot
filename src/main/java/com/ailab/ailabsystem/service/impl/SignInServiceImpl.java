package com.ailab.ailabsystem.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.ailab.ailabsystem.mapper.InOutRegistrationMapper;
import com.ailab.ailabsystem.mapper.UserMapper;
import com.ailab.ailabsystem.model.entity.InOutRegistration;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.entity.UserInfo;
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
        // 生成随机时间
        List<InOutRegistrationVo> dateList =
                GenerateTimeUtil.getDateList(startTime, endTime, num);
        dateList.forEach(o -> {
            // 随机从用户列表中获取
            int index = RandomUtil.randomInt(userList.size());
            User user = userList.get(index);
            UserInfo userInfo = user.getUserInfo();
            o.setSignInUserClass(userInfo.getClassNumber());
            o.setStudentNumber(user.getStudentNumber());
            o.setSignInUserRealName(userInfo.getRealName());
        });
        return dateList;
    }
}
