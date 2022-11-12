package com.ailab.ailabsystem.mapper;

import com.ailab.ailabsystem.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/12 15:39
 */
@SpringBootTest
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void test(){
        User test = userMapper.selectByStuNumAndPwd("test", "1");
        System.out.println(test);
    }
}
