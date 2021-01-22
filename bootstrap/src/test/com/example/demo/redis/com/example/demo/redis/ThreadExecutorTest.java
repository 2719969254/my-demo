package com.example.demo.redis.com.example.demo.redis;

import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/01/09
 */
public class ThreadExecutorTest extends BaseTest{
    ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(20),
            new ThreadPoolExecutor.AbortPolicy());

    @Test
    public void threadPoolExecutor() throws InterruptedException {
        for (int i = 0; i < 20; i++) {
            int i1 = i;
            System.out.println("--------"+i);
            executor.execute(()->{
                if (i1 % 2 == 0){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        Thread.sleep(30*1000L);
    }

}
