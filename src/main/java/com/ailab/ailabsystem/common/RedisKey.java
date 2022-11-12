package com.ailab.ailabsystem.common;

/**
 * @author xiaozhi
 * @description redis键名
 * @create 2022-11-2022/11/12 0:37
 */
public class RedisKey {

    private static final String SPLIT = ":";

    private static final String LOGIN_USER = "login:user";


    public static String getLoginUserKey(String token) {
        return LOGIN_USER + SPLIT + token;
    }
}
