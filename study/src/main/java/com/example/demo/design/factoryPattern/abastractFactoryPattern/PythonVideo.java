package com.example.demo.design.factoryPattern.abastractFactoryPattern;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/02
 */
public class PythonVideo implements IVideo{
    @Override
    public void record() {
        System.out.println("python video");
    }
}
