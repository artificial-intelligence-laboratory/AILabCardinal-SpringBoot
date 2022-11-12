package com.ailab.ailabsystem.common;

/**
 * @author xiaozhi
 * @description redis键名
 * @create 2022-11-2022/11/12 0:37
 */
public class RedisKey {

    /**
     * 分隔符
     */
    private static final String SPLIT = ":";

    /**
     * 登录用户
     */
    private static final String LOGIN_USER = "login:user";

    /**
     * 用户签到
     */
    private static final String USER_SIGN_IN = "user:signIn";


    public static String getLoginUserKey(String token) {
        return LOGIN_USER + SPLIT + token;
    }

    /**
     * 根据用户ID生成签到用户key
     */
    public static String getUserSignIn(Long userId) {
        return USER_SIGN_IN + SPLIT + userId;
    }
}
