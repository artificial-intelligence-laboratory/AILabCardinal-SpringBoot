package com.ailab.ailabsystem.constants;

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
     * 标签内容
     */
    private static final String LABEL_CONTENT = "label:content";

    /**
     * 用户信息列表key
     */
    private static final String USER_INFOS = "user:userInfos";



    /**
     * 用户个人信息
     */
    private static final String USER_INFO = "user:userInfo";

    /**
     * 用户的项目信息key
     */
    public static final String USER_PROJECT = "user:project";

    /**
     *实验室信息区key
     */
    public static final String INDEX_LAB_KEY = "lab:index:info";


    public static String getLoginUserKey(String token) {
        return LOGIN_USER + SPLIT + token;
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
     * 用户个人信息
     */
    public static String getUserInfo(Long userId) {
        return USER_INFO + SPLIT + userId;
    }

    /**
     * 用户的项目信息
     * @return
     */
    public static String getUserProject(Long userId) {
        return USER_PROJECT + SPLIT + userId;
    }



}
