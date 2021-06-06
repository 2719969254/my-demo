package com.example.demo.design.delegatePattern;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/06
 */
public class IEmployeeB implements IEmployee {
    @Override
    public void doing(String str) {
        System.out.println("strBBB = " + str);
    }
}
