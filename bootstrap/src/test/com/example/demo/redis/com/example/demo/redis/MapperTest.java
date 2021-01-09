package com.example.demo.redis.com.example.demo.redis;

import com.example.demo.mapper.UserMapper;
import com.example.demo.pagehelper.PageHelperCallback;
import com.example.demo.pagehelper.PageHelperTemplate;
import com.example.demo.pojo.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MapperTest extends BaseTest{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PageHelperTemplate pageHelperTemplate;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

    @Test
    public void testPageHelper() {
        pageHelperTemplate.executePageHelper(2,3, () -> {
            System.out.println(("----- selectAll method test ------"));
            List<User> userList = userMapper.selectList(null);
            userList.forEach(System.out::println);
            return userList;
        });

    }
}
