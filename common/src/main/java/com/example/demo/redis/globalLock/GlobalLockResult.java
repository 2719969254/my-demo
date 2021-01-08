package com.example.demo.redis.globalLock;

public class GlobalLockResult<R> {
    private final R data;
    private final GlobalLockResultEnum resultEnum;

    private GlobalLockResult(R data, GlobalLockResultEnum globalLockResultEnum) {
        this.data = data;
        this.resultEnum = globalLockResultEnum;
    }

    /**
     * 只能同包调用
     * @param <R>
     * @param data
     * @param resultEnum
     * @return
     */
    protected static <R> GlobalLockResult<R> genResult(R data, GlobalLockResultEnum resultEnum) {
        return new GlobalLockResult<R>(data,resultEnum);
    }
    public boolean isSuccess(){
        return GlobalLockResultEnum.SUCCESS==resultEnum;
    }
}
