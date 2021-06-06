package com.example.demo.design.delegatePattern;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/06
 */
public class Boss {
    public void command(String command, Leader leader){
        leader.doing(command);
    }
}
