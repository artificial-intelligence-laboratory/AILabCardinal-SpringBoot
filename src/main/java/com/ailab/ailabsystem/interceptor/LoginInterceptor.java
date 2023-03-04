package com.ailab.ailabsystem.interceptor;

import com.ailab.ailabsystem.enums.ResponseStatusEnum;
import com.ailab.ailabsystem.exception.CustomException;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.vo.UserVo;
import com.ailab.ailabsystem.service.UserService;
import com.ailab.ailabsystem.util.IPUtil;
import com.ailab.ailabsystem.util.RedisOperator;
import com.ailab.ailabsystem.util.UserHolder;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/12 1:23
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private RedisOperator redis;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (UserHolder.getUser() == null) {
            throw new CustomException(ResponseStatusEnum.NOT_LOGIN_ERROR);
        }
        return true;
    }

    @Transactional
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        UserVo userVo = UserHolder.getUser();
        // 记录用户最后现在时间和ip
        String ip = IPUtil.getIpAddress(request);
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.set("last_online_ip", ip);
        wrapper.set("last_online_time", new Date());
        wrapper.eq("user_id", userVo.getUserId());
        userService.update(wrapper);
    }
}