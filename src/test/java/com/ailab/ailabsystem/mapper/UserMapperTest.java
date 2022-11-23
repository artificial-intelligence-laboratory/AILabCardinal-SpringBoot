package com.ailab.ailabsystem.mapper;

import com.ailab.ailabsystem.model.entity.InOutRegistration;
import com.ailab.ailabsystem.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public void testSelectByStuNumAndPwd(){
        User test = userMapper.selectByStuNumAndPwd("test", "1");
        System.out.println(test);
    }

    @Test
    public void testInsert(){
        User user = new User();
        user.setAvatar("aa");
        user.setEmail("aaa@qq.com");
        user.setStudentNumber("aaa");
        user.setPassword("123");
        userMapper.insert(user);
        System.out.println(user.getUserId());
    }

    @Resource
    private InOutRegistrationMapper in;

    @Test
    public void test(){
        InOutRegistration registration = new InOutRegistration();
        List<InOutRegistration> objects = new ArrayList<>();
        registration.setSignInTime(new Date());
        registration.setSignInUserRealName("小智");
        registration.setSignInUserClass("2005");
        registration.setStudentNumber("20200445346");
        registration.setTask("搞事情");
        registration.setCheckOutTime(new Date());
        registration.setRemark("哈哈");
        for (int i = 0; i < 100000; i++) {
            in.insert(registration);
        }

    }

}
