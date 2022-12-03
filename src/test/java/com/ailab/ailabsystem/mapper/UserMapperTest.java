package com.ailab.ailabsystem.mapper;

import com.ailab.ailabsystem.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
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

    @Test
    public void testSelectAllNotGraduatedUser(){
        List<User> userList = userMapper.selectAllNotGraduatedUser();
        userList.forEach(System.out::println);
    }

}
