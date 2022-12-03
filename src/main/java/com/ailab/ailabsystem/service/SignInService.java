package com.ailab.ailabsystem.service;

import com.ailab.ailabsystem.model.entity.InOutRegistration;
import com.ailab.ailabsystem.model.vo.InOutRegistrationVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @author xiaozhi
 * @description 签到服务
 * @create 2022-11-2022/11/12 19:47
 */
public interface SignInService extends IService<InOutRegistration> {

    /**
     * 生成签到记录
     * @param startTime 开始时间
     * @param endTime   结束时间（包含）
     * @param num       人数
     * @return          返回一个 {@code InOutRegistrationVo} 类型的集合
     */
    List<InOutRegistrationVo> generateSignInRecord(Date startTime,
                                                   Date endTime,
                                                   Integer num);

}
