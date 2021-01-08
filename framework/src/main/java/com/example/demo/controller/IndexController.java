package com.example.demo.controller;

import com.example.demo.utils.UUIDUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
