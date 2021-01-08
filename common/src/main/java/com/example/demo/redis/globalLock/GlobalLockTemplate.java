package com.example.demo.redis.globalLock;

import com.example.demo.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
public class GlobalLockTemplate {

    @Resource
    private RedisUtil jedisClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 释放锁lua脚本
     */
    private static final String UNLOCK_LUA = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    /**
     * 执行
     * 做成这样，是为了保证客户程序员不会错误的操作redis，比如申请锁忘了释放，或者忘了在finally里释放
     *
     * @param <R>
     * @param key           全局锁key
     * @param expireSeconds 过期时间
     * @param callback      真实的业务操作
     * @return
     */
    public <R> GlobalLockResult<R> executeGlobalLock(String key, int expireSeconds, GlobalLockCallback<R> callback) {

        //锁的值是个随机数
        String value = UUID.randomUUID().toString();

        //获取锁
        boolean getLockFlag = jedisClient.set(key, value, expireSeconds);

        if (!getLockFlag) {
            log.info("执行任务" + key + ",没有拿到锁!");
            //没有拿到全局锁
            return GlobalLockResult.genResult(null, GlobalLockResultEnum.NOT_GET_LOCK);
        }
        //占住锁
        log.info("执行任务" + key + "，获取到了锁!");

        try {
            //具体业务操作
            R data = callback.execute();
            return GlobalLockResult.genResult(data, GlobalLockResultEnum.SUCCESS);
        } finally {
            //释放锁
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(UNLOCK_LUA, Long.class);
            Long result = stringRedisTemplate.execute(redisScript, Collections.singletonList(key), value);
            log.info("释放锁完成，result={}",result);

        }

    }
}
