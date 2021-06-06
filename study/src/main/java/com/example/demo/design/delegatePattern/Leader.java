package com.example.demo.design.delegatePattern;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: my-demo-parent
 * @description:
 * @author: tianzuo
 * @created: 2021/06/06
 */
public class Leader implements IEmployee{
    private Map<String,IEmployee> map = new HashMap<>();

    public Leader() {
        map.put("aaa",new IEmployeeA());
        map.put("bbb",new IEmployeeB());
    }

    @Override
    public void doing(String str) {
        map.get(str).doing(str);
    }
}
