package com.example.demo.design.prototypePattern.referenceCopy;

import java.util.ArrayList;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/02
 */
public class Test {
    /**
     * 浅克隆后所有的引用对象都仍指向原对象
      */
    public static void main(String[] args) {
        ConcretePrototypeA concretePrototypeA = new ConcretePrototypeA();
        concretePrototypeA.setName("tian");
        concretePrototypeA.setAge(18);
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("11");
        concretePrototypeA.setHobbies(objects);

        Client client = new Client(concretePrototypeA);
        ConcretePrototypeA concretePrototypeB = (ConcretePrototypeA) client.startClone(concretePrototypeA);
        System.out.println("concretePrototypeB = " + concretePrototypeB);
        System.out.println("原始对象的引用类型地址为："+concretePrototypeA.getHobbies());
        System.out.println("克隆对象的引用类型地址为："+concretePrototypeB.getHobbies());
    }
}
