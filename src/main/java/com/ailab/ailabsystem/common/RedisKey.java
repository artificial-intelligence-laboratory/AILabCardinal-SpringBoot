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

    /**
     * 标签内容
     */
    private static final String LABEL_CONTENT = "label:content";

    /**
     * 用户信息列表key
     */
    private static final String USER_INFOS = "user:userInfos";

    /**
     * 用户的首页信息key
     */
    public static final String INDEX_USER_INFO = "user:indexInfo";

    public static String getLoginUserKey(String token) {
        return LOGIN_USER + SPLIT + token;
    }

    /**
     * 根据用户ID生成签到用户key
     */
    public static String getUserSignIn(Long userId) {
        return USER_SIGN_IN + SPLIT + userId;
    }

    /**
     * 获取标签内容key
     */
    public static String getLabelContent(Integer siteTypeCode) {
        return LABEL_CONTENT + SPLIT + siteTypeCode;
    }

    /**
     * 用户信息列表key
     */
    public static String getUserInfos() {
        return USER_INFOS;
    }

    /**
     * 用户首页信息
     * @return
     */
    public static String getIndexUser(Long userId) {
        return INDEX_USER_INFO + SPLIT + userId;
    }
}
