package com.ly61.service;

import com.ly61.service.UserService;
import com.spring.BeanPostProcessor;
import com.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author suk_mit
 * @Date 2021/11/10 18:20
 * @Version 1.0
 */
@Component("lyBeanPostProcessor")
public class LyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("初始化前");
        if ("userService".equals(beanName)) {
            System.out.println(beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("初始化后");
        if ("userService".equals(beanName)) {
            Object proxyInstance = Proxy.newProxyInstance(LyBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("代理逻辑");
                    return method.invoke(bean, args);
                }
            });
            System.out.println(proxyInstance);
        }
        return bean;
    }
}
