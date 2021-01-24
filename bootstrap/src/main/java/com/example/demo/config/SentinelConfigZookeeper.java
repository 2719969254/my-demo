package com.example.demo.config;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.example.demo.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @program: my-demo-parent
 * @description: 这里配置，让 Sentinel 客户端到 zookeeper 中读配置规则
 * @author: tianzuo
 * @created: 2021/01/23
 */
@Slf4j
@Component
public class SentinelConfigZookeeper {

    @Value("${sentinel.zookeeper.address}")
    private String zkServer;

    @Value("${sentinel.zookeeper.path}")
    private String zkPath;

    @Value("${spring.application.name}")
    private String appName;

    /**
     * 这个 Bean 构造好了之后，马上就取 zookeeper 中读配置规则
     */
    @PostConstruct
    public void loadRules() {

        // 第一个泛型 String，就是应用的名字，orderApi，
        // 第二个泛型，就是这个应用对应的一组流量规则；
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource
                = new ZookeeperDataSource<>(zkServer,
                zkPath,
                // source 就是从 zookeeper 中读出来的字符串
                source -> JSON.parseArray(source, FlowRule.class));
        System.out.println("flowRuleDataSource.getProperty() = " + GsonUtils.toJson(flowRuleDataSource.getProperty()));
        // 把从 zookeeper 中读到的配置规则，写入内存
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }

}