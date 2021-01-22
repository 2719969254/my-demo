package com.example.demo.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.example.demo.utils.UUIDUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/01/08
 */
@RestController
public class IndexController {
    @GetMapping("/")
    public String csrf() {
        return UUIDUtil.getUuid();
    }

    @RequestMapping("sentinel")
    public String sentinel() {
        initFlowRules();
        String resourceName = "testSentinel";
        Entry entry = null;
        String retVal;
        try {
            entry = SphU.entry(resourceName, EntryType.IN);
            retVal = "passed";
        }catch(Exception e) {
            retVal = "blocked";
        }finally {
            if(entry != null) {
                entry.exit();
            }
        }

        return retVal;

    }

    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("testSentinel");// 资源名称
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS); //qps 降级
        rule.setCount(2); // 每秒 2次，超过2次就失败
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

}
