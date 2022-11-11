package com.ailab.ailabsystem.controller;

import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.service.LoginService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登陆
     * @param request 用于登陆成功后将用户数据存到session中
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R<User> userLogin(HttpServletRequest request,@RequestBody User user){

        // 1.将获取到的密码进行MD5加密处理(先写在这里，之后再考虑)
        String password = user.getPassword();
//        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,user.getUserName());
        User userLogin = loginService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if(userLogin == null){
            return R.error("账号不存在");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if(!userLogin.getPassword().equals(password)){
            return R.error("密码错误，登录失败");
        }

        //5、查看用户状态，如果为已禁用状态，则返回用户已禁用结果
        if(userLogin.getUserStatus() == 0){
            return R.error("账号已禁用");
        }

        //6、登录成功，将用户id存入Session并返回登录成功结果
        request.getSession().setAttribute("User",userLogin.getUserId());
        return R.success(userLogin);
    }

}