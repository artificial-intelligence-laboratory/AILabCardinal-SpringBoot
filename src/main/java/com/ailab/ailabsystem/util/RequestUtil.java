package com.ailab.ailabsystem.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaozhi
 * @description 请求工具类
 * @create 2022-11-2022/11/12 1:19
 */
public class RequestUtil {

    /**
     * 获取请求token值
     */
    public static String getAuthorization(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

}
