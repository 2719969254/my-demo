package com.example.demo.design.prototypePattern.deepCopy;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/02
 */
public class Test {


    public static void main(String[] args) throws CloneNotSupportedException {
        QiTianDaShen qiTianDaShen = new QiTianDaShen();
        QiTianDaShen clone = (QiTianDaShen) qiTianDaShen.clone();
        System.out.println(qiTianDaShen.jinGuBang == clone.jinGuBang);

        QiTianDaShen qiTianDaShen1 = qiTianDaShen.shallowClone(qiTianDaShen);
        System.out.println(qiTianDaShen1.jinGuBang == qiTianDaShen.jinGuBang);
    }
}
