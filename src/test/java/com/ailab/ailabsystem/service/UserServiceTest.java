package com.ailab.ailabsystem.service;

import com.ailab.ailabsystem.model.vo.UserInfoVo;
import com.ailab.ailabsystem.util.RedisOperator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/21 18:19
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Resource
    private RedisOperator redis;

    @Test
    public void testGetUserInfo(){
        Map<String, List<UserInfoVo>> userInfo = userService.getUserInfoList();
        for (Map.Entry<String, List<UserInfoVo>> entry : userInfo.entrySet()) {
            System.out.println(entry.getKey() + "：" + entry.getValue());
        }
    }

}
