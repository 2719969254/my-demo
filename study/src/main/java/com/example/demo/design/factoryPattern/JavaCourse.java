package com.example.demo.design.factoryPattern;

import com.example.demo.design.factoryPattern.simpleFactoryPattern.ICourse;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/02
 */
public class JavaCourse implements ICourse {
    @Override
    public void record() {
        System.out.println("java course");
    }
}
