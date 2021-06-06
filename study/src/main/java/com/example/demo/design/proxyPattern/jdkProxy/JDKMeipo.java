package com.example.demo.design.proxyPattern.jdkProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/03
 */
public class JDKMeipo implements InvocationHandler {
    // 被代理对象，需要先保存下来
    private Object target;

    public Object getInstance(Object target) throws Exception {
        this.target = target;
        Class<?> clazz = target.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        before();
        Object invoke = method.invoke(this.target, objects);
        after();
        return invoke;
    }

    private void before() {
        System.out.println("before");
    }

    private void after() {
        System.out.println("after");
    }
}
