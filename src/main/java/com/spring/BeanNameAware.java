package com.spring;

/**
 * @Author suk_mit
 * @Date 2021/11/10 18:01
 * @Version 1.0
 */
public interface BeanNameAware {
    void setBeanName(String beanName);
    String beanName();
}
