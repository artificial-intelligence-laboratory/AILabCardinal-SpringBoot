package com.ailab.ailabsystem.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;


@Component
public class RedisStringUtil {

    private static final int DEFAULT_EXPIRE_TIME = 60 * 60; // 默认过期时间：1小时

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 设置 key-value
     * @param key 键
     * @param value 值
     */
    public void set(String key, String value) {
        jedisCluster.set(key, value);
    }

    /**
     * 设置 key-value，带过期时间
     * @param key 键
     * @param value 值
     * @param expireTime 过期时间，单位：秒
     */
    public void set(String key, String value, long expireTime) {
        jedisCluster.setex(key, expireTime, value);
    }

    /**
     * 获取 key 对应的 value
     * @param key 键
     * @return value
     */
    public String get(String key) {
        return jedisCluster.get(key);
    }

    /**
     * 删除 key 对应的 value
     * @param key 键
     */
    public void del(String key) {
        jedisCluster.del(key);
    }

    /**
     * 重载 set 方法，不带过期时间
     * @param key 键
     * @param value 值
     * @param <T> 值的类型
     */
}
