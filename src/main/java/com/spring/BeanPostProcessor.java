package com.spring;

/**
 * @Author suk_mit
 * @Date 2021/11/10 18:17
 * @Version 1.0
 */
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String beanName);

    Object postProcessAfterInitialization(Object bean, String beanName);
}
