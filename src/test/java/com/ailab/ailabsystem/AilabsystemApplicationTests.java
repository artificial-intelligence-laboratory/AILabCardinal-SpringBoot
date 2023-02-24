package com.ailab.ailabsystem;

import com.ailab.ailabsystem.mapper.*;
import com.ailab.ailabsystem.model.entity.Site;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.entity.UserInfo;
import com.ailab.ailabsystem.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class AilabsystemApplicationTests {

    // 继承了BaseMapper，所有方法来自父类
// 但也可以自己编写拓展方法
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SiteMapper siteMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectMemberMapper projectMemberMapper;

    @Autowired
    private AwardMapper awardMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    void contextLoads() {
        // 查询全部用户
        // selectList()的参数是一个条件构造器 Wrapper，这里先不用
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    void sitestest() {
        // 查询全部用户
        // selectList()的参数是一个条件构造器 Wrapper，这里先不用
        List<Site> sites = siteMapper.selectList(null);
        sites.forEach(System.out::println);
    }

    @Test
    void getIndexLabInfoTest() {
//        List<User> userList = userService.list();
//        userList = userList.stream().filter(user -> {
//            return user.getUserRight() == 2 || user.getUserRight() == 3;
//        }).collect(Collectors.toList());
        System.out.println(awardMapper.getLabAwardCount());
    }

    @Test
    void timeTest() {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUserInfoId, 1);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        Date enrollmentYear = userInfo.getEnrollmentYear();
        Date date = new Date();
        long l = date.getTime() - enrollmentYear.getTime();
        l = l / 1000 / 86400;
        System.out.println("输出信息" + l);
    }


}
