package com.example.demo.design.prototypePattern.deepCopy;

import java.io.*;
import java.util.Date;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/02
 */
public class QiTianDaShen extends Monkey implements Cloneable, Serializable {
    public JinGuBang jinGuBang;

    public QiTianDaShen() {
        this.birthday = new Date();
        this.jinGuBang = new JinGuBang();
    }

    public Object deepClone() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(this);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            QiTianDaShen o = (QiTianDaShen) objectInputStream.readObject();
            o.birthday = new Date();
            return o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public QiTianDaShen shallowClone(QiTianDaShen target){
        QiTianDaShen qiTianDaShen = new QiTianDaShen();
        qiTianDaShen.height = target.height;
        qiTianDaShen.weight = target.weight;
        qiTianDaShen.jinGuBang = target.jinGuBang;
        qiTianDaShen.birthday = new Date();
        return qiTianDaShen;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return this.deepClone();
    }
}
