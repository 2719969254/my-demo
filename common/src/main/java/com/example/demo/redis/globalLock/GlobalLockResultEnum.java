package com.example.demo.redis.globalLock;

/**
 * 标记全局锁是否抢占成功的枚举
 * callback如果抛exception了，就直接抛出去了，所以没有EXCEPTION
 * @author wangbin7
 */
public enum GlobalLockResultEnum {
    /**
     * 没有抢到锁
     */
    NOT_GET_LOCK,
    /**
     * 正确执行
     */
    SUCCESS
    ;
}
