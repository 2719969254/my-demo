package com.example.demo.design.factoryPattern.simpleFactoryPattern;

import com.example.demo.design.factoryPattern.JavaCourse;
import com.example.demo.design.factoryPattern.PythonCourse;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/02
 */
public class Test {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
       /* JavaCourse javaCourse = new JavaCourse();
        javaCourse.record();*/

        CourseFactory courseFactory = new CourseFactory();
        ICourse iCourse = courseFactory.create(JavaCourse.class);
        iCourse.record();
        iCourse = courseFactory.create(PythonCourse.class);
        iCourse.record();
    }
}
