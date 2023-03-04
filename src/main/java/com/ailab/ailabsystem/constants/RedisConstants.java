package com.ailab.ailabsystem.constants;

public class RedisConstants {
    public static final Long CACHE_NULL_TTL = 2L;
    public static final String LOCK_SHOP_KEY = "lock:shop:";

    public static final Long USER_DETAILS__TTL = 30L * 60;
    public static final String USER_DETAILS_KEY = "user:details:";

    /**
     * 登录用户的信息
     */
    public static final Long USER_VO_TTL = 30L * 60;
    public static final String USER_VO_KEY = "login:user:";

    /**
     * 用户的邮箱验证码
     */
    public static final String EMAIL_CODE = "user_emailCode:";

    /**
     * 用户登录凭证
     * @param token
     * @return
     */
    public static final String LOGIN_UNIQUE_TOKEN = "login:token:";

    /**
     * 用户签到
     */
    public static final String USER_SIGN_IN = "user:signIn:";
    /**
     * 用户的首页信息key
     */
    public static final String INDEX_USER_INFO = "user:indexInfo:";

    /**
     * 用户简单信息列表key
     */
    public static final Long SIMPLE_USER_INFOS_TTL = 5L * 60;
    public static final String SIMPLE_USER_INFOS = "user:simpleUserInfos:";



}