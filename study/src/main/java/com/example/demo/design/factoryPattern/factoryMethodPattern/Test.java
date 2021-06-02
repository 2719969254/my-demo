package com.example.demo.design.factoryPattern.factoryMethodPattern;

import com.example.demo.design.factoryPattern.simpleFactoryPattern.ICourse;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/02
 */
public class Test {
    public static void main(String[] args) {
        ICourseFactory iCourseFactory = new JavaCourseFactory();
        ICourse iCourse = iCourseFactory.create();
        iCourse.record();

        iCourseFactory = new PythonCourseFactory();
        iCourseFactory.create();
        iCourse.record();
    }
}
