package com.spring;

/**
 * @Author suk_mit
 * @Date 2021/11/10 18:10
 * @Version 1.0
 */
public interface InitializingBean{
    void afterPropertiesSet() throws Exception;
}
