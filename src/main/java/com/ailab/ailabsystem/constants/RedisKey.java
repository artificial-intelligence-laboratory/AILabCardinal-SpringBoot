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
     * 用户的邮箱验证码
     */
    private static final String EMAIL_CODE = "user_emailCode";

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
     * 往届用户简单信息列表key
     */
    private static final String SIMPLE_USER_INFOS = "user:simpleUserInfos";


    /**
     * 用户个人信息
     */
    private static final String USER_INFO = "user:userInfo";

    /**
     * 用户的首页信息key
     */
    public static final String INDEX_USER_INFO = "user:indexInfo";

    /**
     * 用户的项目信息key
     */
    public static final String USER_PROJECT = "user:project";

    /**
     *实验室信息区key
     */
    public static final String INDEX_LAB_KEY = "lab:index:info";

    /**
     * 用户登录凭证
     * @param token
     * @return
     */
    public static final String LOGIN_UNIQUE_TOKEN = "login:token";

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
     * 用户个人信息
     */
    public static String getUserInfo(Long userId) {
        return USER_INFO + SPLIT + userId;
    }

    /**
     * 用户首页信息
     * @return
     */
    public static String getIndexUser(Long userId) {
        return INDEX_USER_INFO + SPLIT + userId;
    }

    /**
     * 实验室信息区
     */
    public static String getIndexAiLabInfo() {
        return INDEX_USER_INFO;
    }

    /**
     * 用户的项目信息
     * @return
     */
    public static String getUserProject(Long userId) {
        return USER_PROJECT + SPLIT + userId;
    }

    /**
     * 获取用户邮箱验证码key
     */
    public static String getEmailCode(Long userId) {
        return EMAIL_CODE + SPLIT + userId;
    }

    /**
     * 获取简单用户信息
     */
    public static String getPreSimpleUserInfos() {
        return SIMPLE_USER_INFOS;
    }

    /**
     * 获取用户登录凭证
     */
    public static String getUserLoginToken(String studentNumber) {
        return LOGIN_UNIQUE_TOKEN + SPLIT + studentNumber;
    }

}
