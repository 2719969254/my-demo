package com.example.demo.design.proxyPattern.jdkProxy;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/03
 */
public class Test {
    public static void main(String[] args) throws Exception {
        Person obj = (Person) new JDKMeipo().getInstance(new Customer());
        obj.getLove();
    }
}
