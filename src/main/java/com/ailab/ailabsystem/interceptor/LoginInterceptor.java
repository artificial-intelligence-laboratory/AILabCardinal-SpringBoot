package com.ailab.ailabsystem.interceptor;

import cn.hutool.json.JSONUtil;
import com.ailab.ailabsystem.common.RedisKey;
import com.ailab.ailabsystem.enums.ResponseStatusEnum;
import com.ailab.ailabsystem.exception.CustomException;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.vo.UserVo;
import com.ailab.ailabsystem.service.UserService;
import com.ailab.ailabsystem.util.RedisOperator;
import com.ailab.ailabsystem.util.RequestUtil;
import com.ailab.ailabsystem.util.UserHolder;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
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
        String token = RequestUtil.getAuthorization(request);
        // 未登录
        if (StringUtils.isBlank(token)) {
            throw new CustomException(ResponseStatusEnum.NOT_LOGIN_ERROR);
        }
        String userJson = redis.get(RedisKey.getLoginUserKey(token));
        // 会话过期
        if (StringUtils.isBlank(userJson)) {
            throw new CustomException(ResponseStatusEnum.SESSION_EXPIRE);
        }
        UserVo userVo = JSONUtil.toBean(userJson, UserVo.class);
        // 本次请求持有用户
        UserHolder.saveUser(userVo);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        UserVo user = UserHolder.getUser();
        // 记录用户最后现在时间和ip
        String ipAddress = RequestUtil.getIpAddress(request);
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.set("last_online_ip_address", ipAddress);
        wrapper.set("last_online_time", new Date());
        wrapper.eq("user_id", user.getUserId());
        userService.update(wrapper);
        // 移除用户
        UserHolder.removeUser();
    }
}
