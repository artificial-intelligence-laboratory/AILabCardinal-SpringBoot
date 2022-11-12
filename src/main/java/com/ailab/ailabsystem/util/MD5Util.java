package com.ailab.ailabsystem.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiaozhi
 * @description MD5加密
 * @create 2022-10-2022/10/25 17:21
 */
public class MD5Util {

    /**
     * 随机盐生成MD5
     * @param password
     * @return
     */
    public static String encodeByMD5(String password) {
        // 生成随机盐
        String salt = RandomStringUtils.randomAscii(12);
        return encodeByMD5(salt, password);
    }

    /**
     * 指定盐生成MD5
     * @param salt
     * @param password
     * @return
     */
    public static String encodeByMD5(String salt, String password) {
        return DigestUtils.md5DigestAsHex((salt + password).getBytes());
    }

    /**
     * 生成文件MD5
     */
    public static String filenameByMD5(InputStream inputStream) {
        try {
            return DigestUtils.md5DigestAsHex(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String md5 = encodeByMD5("sdf234gse342", "123");
        System.out.println(md5);
    }
}
