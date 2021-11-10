package com.ly61;

import com.ly61.service.UserService;
import com.spring.Ly61ApplicationContext;

/**
 * @Author suk_mit
 * @Date 2021/11/7 14:32
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
        Ly61ApplicationContext ly61ApplicationContext = new Ly61ApplicationContext(AppConfig.class);
        UserService userService = (UserService) ly61ApplicationContext.getBean("userService");
        userService.test();
    }
}
