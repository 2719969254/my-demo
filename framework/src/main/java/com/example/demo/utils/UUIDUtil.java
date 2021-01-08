package com.example.demo.utils;

import java.util.UUID;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/01/08
 */
public class UUIDUtil {
    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
