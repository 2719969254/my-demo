package com.example.demo.design.proxyPattern.jdkProxy;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/03
 */
public class Customer implements Person{
    @Override
    public void getLove() {
        System.out.println("person");
    }
}
