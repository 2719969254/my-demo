package com.example.demo.redis.com.example.demo.redis;

import com.example.demo.redis.RedisUtil;
import com.example.demo.redis.globalLock.GlobalLockCallback;
import com.example.demo.redis.globalLock.GlobalLockResult;
import com.example.demo.redis.globalLock.GlobalLockTemplate;
import com.example.demo.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/01/08
 */

public class RedisTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(RedisTest.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GlobalLockTemplate globalLockTemplate;

    @Test
    public void testSet() {
        stringRedisTemplate.opsForValue().set("test-string-value", "Hello Redis");
    }

    @Test
    public void testGet() {
        System.out.println(stringRedisTemplate.opsForValue().get("123"));
    }

    @Test
    public void testGlobalLock() {
        log.error("111");
        GlobalLockResult<Long> lockResult = globalLockTemplate.executeGlobalLock("111", 60, (GlobalLockCallback<Long>) () -> {
            log.info("添加缓存");
            return null;
        });
        log.info(GsonUtils.toJson(lockResult));
    }

    @Test
    public void testGet1() {
        String str = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        String lockKey = "123";
        String UUID = "12345";
        System.out.println("1--------- " + redisTemplate.opsForValue().get("123"));
        boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, UUID, 3, TimeUnit.MINUTES);
        System.out.println("2--------- " + redisTemplate.opsForValue().get("123"));
        if (!success) {
            System.out.println("锁已存在");
        }
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(str, Long.class);
        Long result = (Long) redisTemplate.execute(redisScript, Collections.singletonList(lockKey), UUID);
        System.out.println(redisScript.toString());
        System.out.println("3--------- " + redisTemplate.opsForValue().get("123"));
        System.out.println(result);
    }

}
