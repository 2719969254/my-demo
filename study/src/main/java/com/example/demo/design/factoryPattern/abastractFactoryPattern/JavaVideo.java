package com.example.demo.design.factoryPattern.abastractFactoryPattern;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/02
 */
public class JavaVideo implements IVideo{
    @Override
    public void record() {
        System.out.println("java video");
    }
}
