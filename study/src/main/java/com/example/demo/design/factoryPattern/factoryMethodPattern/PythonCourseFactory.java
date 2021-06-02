package com.example.demo.design.factoryPattern.factoryMethodPattern;

import com.example.demo.design.factoryPattern.PythonCourse;
import com.example.demo.design.factoryPattern.simpleFactoryPattern.ICourse;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/02
 */
public class PythonCourseFactory implements ICourseFactory{
    @Override
    public ICourse create() {
        return new PythonCourse();
    }
}
