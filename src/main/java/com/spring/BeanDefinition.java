package com.spring;

/**
 * @Author suk_mit
 * @Date 2021/11/10 17:23
 * @Version 1.0
 */
public class BeanDefinition {

    private Class clazz;
    private String scope;

    public BeanDefinition() {
    }

    public BeanDefinition(Class clazz, String scope) {
        this.clazz = clazz;
        this.scope = scope;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "clazz=" + clazz +
                ", scope='" + scope + '\'' +
                '}';
    }
}
