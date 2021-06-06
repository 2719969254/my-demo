package com.example.demo.design.delegatePattern;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/06
 */
public class Test {
    public static void main(String[] args) {
        new Boss().command("aaa",new Leader());
        new Boss().command("aaa",new Leader());
        new Boss().command("bbb",new Leader());
    }
}
