package com.example.demo.redis.globalLock;

/**
 * 全局锁callback接口，excute执行自己的业务逻辑
 * @author wangbin7
 */
public interface GlobalLockCallback<R> {
    R execute();
}
