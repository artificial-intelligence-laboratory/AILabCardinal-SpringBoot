package com.ailab.ailabsystem.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ailab.ailabsystem.common.CommonConstant;
import com.ailab.ailabsystem.common.RedisKey;
import com.ailab.ailabsystem.model.entity.User;
import com.ailab.ailabsystem.model.vo.UserVo;
import com.ailab.ailabsystem.util.RedisOperator;
import com.ailab.ailabsystem.util.RequestUtil;
import com.ailab.ailabsystem.util.UserHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {

    @Resource
    RedisOperator redis;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的token
        String token = RequestUtil.getAuthorization(request);
        if (StrUtil.isBlank(token)) {
            return true;
        }
        // 2.基于TOKEN获取redis中的用户
        String key  = RedisKey.getLoginUserKey(token);
        String userStr = redis.get(key);
        // 3.判断用户是否存在
        if (userStr == null) {
            return true;
        }
        // 4.将查询到的hash数据转为UserVo
        UserVo userVo = JSONUtil.toBean(userStr, UserVo.class);
        User user = BeanUtil.copyProperties(userVo, User.class);
        UserHolder.saveUser(user);
        // 5.刷新token有效期
        String str = JSONUtil.toJsonStr(userVo);
        redis.set(RedisKey.getLoginUserKey(token), str, CommonConstant.ONE_WEEK);
        // 6.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户
        UserHolder.removeUser();
    }
}
	
