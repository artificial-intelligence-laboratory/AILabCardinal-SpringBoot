package com.ailab.ailabsystem.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ailab.ailabsystem.common.CommonConstant;
import com.ailab.ailabsystem.constants.RedisKey;
import com.ailab.ailabsystem.enums.ResponseStatusEnum;
import com.ailab.ailabsystem.exception.CustomException;
import com.ailab.ailabsystem.model.vo.UserVo;
import com.ailab.ailabsystem.util.RedisOperator;
import com.ailab.ailabsystem.util.RequestUtil;
import com.ailab.ailabsystem.util.UserHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ailab.ailabsystem.constants.RedisConstants.LOGIN_UNIQUE_TOKEN;

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
        if (StrUtil.isBlank(userStr)) {
            return true;
        }
        // 4.将查询到的hash数据转为UserVo
        UserVo userVo = JSONUtil.toBean(userStr, UserVo.class);
        UserHolder.saveUser(userVo);
        String userLoginTokenKey = LOGIN_UNIQUE_TOKEN + userVo.getStudentNumber();
        //获取redis存放的token，与前端传进来的进行对比，看是否一致
        String loginToken = redis.get(userLoginTokenKey);
        //判断token是否一致
        if (!loginToken.equals(token)) {
            throw new CustomException(ResponseStatusEnum.PARAMS_ERROR);
        }
        // 5.刷新token有效期
        //如果一致则刷新token有效期,以及用户信息
        redis.set(userLoginTokenKey, loginToken);
        String str = JSONUtil.toJsonStr(userVo);
        redis.set(RedisKey.getLoginUserKey(token), str, CommonConstant.ONE_WEEK);
        // 6.放行
        return true;
    }


}
	
