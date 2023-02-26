package com.ailab.ailabsystem.util;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * @author huiyuan
 */
public class PatternUtil {

    /**
     * 邮箱的正则表达式
     */
    public static final String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

    /**
     * 手机号码正则表达式
     */
    public static final String RULE_PHONE = "1[3-9]\\d{9}$";

    /**
     * 对邮箱格式进行校验
     * @param emailStr
     * @return
     */
    public static boolean matchEmailPattern(String emailStr) {
        return Pattern.compile(RULE_EMAIL).matcher(emailStr).matches();
    }

    /**
     * 对手机号码格式进行校验
     * @param phoneStr
     * @return
     */
    public static boolean matchPhonePattern(String phoneStr) {
        return Pattern.compile(RULE_PHONE).matcher(phoneStr).matches();
    }
}
