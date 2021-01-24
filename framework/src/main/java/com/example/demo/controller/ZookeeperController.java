package com.example.demo.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ZookeeperController {

    //定义资源  value:设置资源的名称   blockHandler：设置限流或降级的处理函数
    @SentinelResource(value = "my-demo", blockHandler = "exceptionHandler")
    @GetMapping("zookeeper")
    public String hello() {
        return "Hello Sentinel!";
    }

    //被限流或降级的处理函数
    public String exceptionHandler(BlockException e) {
        e.printStackTrace();
        return "系统繁忙，请稍候";
    }

    //定义资源  value:设置资源的名称   blockHandler：设置限流或降级的处理函数
    @SentinelResource(value = "my-demo", blockHandler = "exceptionHandler")
    @GetMapping("slow")
    public String slow() throws InterruptedException {
        Thread.sleep(1000);
        return "Hello Sentinel!";
    }

    //定义资源  value:设置资源的名称   blockHandler：设置限流或降级的处理函数
    @SentinelResource(value = "slow-demo", blockHandler = "exceptionHandler")
    @GetMapping("slowRandom")
    public String slowRandom() throws InterruptedException {
        double random = Math.random() * 1000;
        if (random > 800) {
            Thread.sleep(1000);
        }
        return "Hello Sentinel!";
    }

    //定义资源  value:设置资源的名称   blockHandler：设置限流或降级的处理函数
    @SentinelResource(value = "exception-demo", blockHandler = "exceptionHandler")
    @GetMapping("exception")
    public String exeception() throws InterruptedException {
        double random = Math.random() * 1000;
        if (random > 800) {
            log.error("random={}", random);
            throw new RuntimeException("异常啦！");
        }
        return "Hello Sentinel!";
    }

}
