package com.example.demo.design.factoryPattern.abastractFactoryPattern;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/02
 */
public class Test {
    public static void main(String[] args) {
        CourseFactory courseFactory = new JavaCourseFactory();
        courseFactory.createVideo().record();
        courseFactory.createNote().edit();
        courseFactory = new PythonCourseFactory();
        courseFactory.createNote().edit();
        courseFactory.createVideo().record();
    }
}
