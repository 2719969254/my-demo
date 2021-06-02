package com.example.demo.design.factoryPattern.simpleFactoryPattern;

/**
 * @program: my-demo-parent
 * @description: 简单工厂类
 * 工厂类职责过重
 * @author: tianzuo
 * @created: 2021/06/02
 */
public class CourseFactory {
    public ICourse create(Class<? extends ICourse> clazz) throws InstantiationException, IllegalAccessException {
        if (clazz != null){
            return clazz.newInstance();
        }
        return null;
    }
}
