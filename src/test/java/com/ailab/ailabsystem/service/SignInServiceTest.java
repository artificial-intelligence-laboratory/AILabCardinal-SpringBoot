package com.ailab.ailabsystem.service;

import cn.hutool.core.date.DateUtil;
import com.ailab.ailabsystem.model.vo.InOutRegistrationVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author xiaozhi
 */
@SpringBootTest
public class SignInServiceTest {

    @Resource
    private SignInService signInService;

    @Test
    public void test() {
        Date startTime = DateUtil.parse("2022-4-1");
        Date endTime = DateUtil.parse("2022-4-29");
        List<InOutRegistrationVo> inOutRegistrationVos = signInService.generateSignInRecord(startTime, endTime, 2);
        inOutRegistrationVos.forEach(System.out::println);
    }
}
