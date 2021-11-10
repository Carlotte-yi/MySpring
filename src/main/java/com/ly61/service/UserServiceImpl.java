package com.ly61.service;

import com.spring.AutoWired;
import com.spring.BeanNameAware;
import com.spring.Component;
import com.spring.InitializingBean;

/**
 * @Author suk_mit
 * @Date 2021/11/7 14:34
 * @Version 1.0
 */
@Component("userService")
public class UserServiceImpl implements UserService {

    @AutoWired
    OrderService orderService;

    private String beanName;
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public void test() {
        System.out.println(orderService);
    }
}
