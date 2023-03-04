package com.ailab.ailabsystem.controller;

import com.ailab.ailabsystem.common.R;
import com.ailab.ailabsystem.constants.RedisKey;
import com.ailab.ailabsystem.model.dto.LoginRequest;
import com.ailab.ailabsystem.service.UserService;
import com.ailab.ailabsystem.util.CheckDataUtil;
import com.ailab.ailabsystem.util.RedisOperator;
import com.ailab.ailabsystem.util.RequestUtil;
import com.ailab.ailabsystem.util.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.ailab.ailabsystem.constants.RedisConstants.LOGIN_UNIQUE_TOKEN;
import static com.ailab.ailabsystem.constants.RedisConstants.USER_VO_KEY;

/**
 * @author xiaozhi
 */
@Api(value = "登录服务接口", tags = "登录服务接口")
@RestController
@RequestMapping("/passport")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redis;

    /**
     * 用户登陆
     * @param loginRequest      登录请求参数
     * @return
     */
    @ApiOperation(value = "登录", notes = "账号密码登录，返回token和用户信息，" +
            "<br>前端保存到cookie或localstorage中，" +
            "<br>请求其他接口时在Authorization请求头中携带token发送请求，没有token会判断为未登录")
    @PostMapping("/login")
    public R<Object> userLogin(HttpServletRequest request, @RequestBody LoginRequest loginRequest){
        CheckDataUtil.checkLoginRequest(loginRequest);
        return userService.login(request, loginRequest);
    }

    @ApiOperation(value = "退出登录", notes = "退出登录")
    @PostMapping("/logout")
    public R<Object> userLogin(HttpServletRequest request){
        String token = RequestUtil.getAuthorization(request);
        redis.del(USER_VO_KEY + token);
        redis.del(LOGIN_UNIQUE_TOKEN + UserHolder.getUser().getUserId());
        UserHolder.removeUser();
        return R.success();
    }
}