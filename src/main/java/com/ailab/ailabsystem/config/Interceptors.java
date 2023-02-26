package com.ailab.ailabsystem.config;

import com.ailab.ailabsystem.interceptor.LoginInterceptor;
import com.ailab.ailabsystem.interceptor.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author xiaozhi
 * @description 拦截器配置
 * @create 2022-11-2022/11/12 10:45
 */
@Configuration
public class Interceptors implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Resource
    private RefreshTokenInterceptor refreshTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(
                        "/user/**",
                        "/passport/**",
                        "/navigation/**")
                .excludePathPatterns(
                        "/passport/login"
                ).order(1);
        registry.addInterceptor(refreshTokenInterceptor)
                .addPathPatterns(
                        "/user/getIndexUserInfo",
                        "/lab/getLabInfo"
                ).order(0);
    }
}
