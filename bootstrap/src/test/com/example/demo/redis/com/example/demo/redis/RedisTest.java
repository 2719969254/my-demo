package com.example.demo.redis.com.example.demo.redis;

import com.example.demo.redis.RedisUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/01/08
 */
public class RedisTest extends BaseTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void testSet() {
        stringRedisTemplate.opsForValue().set("test-string-value", "Hello Redis");
    }

    @Test
    public void testGet() {
        System.out.println(stringRedisTemplate.opsForValue().get("123"));
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

    @Test
    public void testLua() {
        String str = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        String lockKey = "123";
        String uuid = "12345";
        System.out.println("1--------- " + redisTemplate.opsForValue().get("123"));
        boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, uuid, 3, TimeUnit.MINUTES);
        if (!success) {
            System.out.println("锁已存在");
        }
        System.out.println("2--------- " + redisTemplate.opsForValue().get("123"));
        redisUtil.execute(str, Long.class, Collections.singletonList("123"), 12345);
        System.out.println("3--------- " + redisTemplate.opsForValue().get("123"));
    }

}
