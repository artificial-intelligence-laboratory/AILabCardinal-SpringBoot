package com.ailab.ailabsystem.UtilsTest;

import com.ailab.ailabsystem.util.RedisOperator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author xiaozhi
 * @description
 * @create 2022-11-2022/11/12 0:13
 */
@SpringBootTest
public class TestRedisOperator {

    @Resource
    private RedisOperator redis;

    @Test
    public void test(){
        redis.set("test", "小智");
    }
}
