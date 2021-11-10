package com.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author suk_mit
 * @Date 2021/11/7 14:30
 * @Version 1.0
 */
public class Ly61ApplicationContext {

    private Class configClass;

    private ConcurrentHashMap<String, Object> singletonObject = new ConcurrentHashMap<>(); //单例池
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(); //所有bean的定义
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public Ly61ApplicationContext(Class configClass) {
        this.configClass = configClass;
        scan(configClass);

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            System.out.println(beanDefinition);
            if (beanDefinition.getScope().equals("singleton")) {
                Object bean = createBean(beanName,beanDefinition);
                singletonObject.put(beanName,bean);
            }
        }
    }

    public Object createBean(String beanName,BeanDefinition beanDefinition) {

        try {
            Class clazz = beanDefinition.getClazz();
            Object instance = clazz.getDeclaredConstructor().newInstance();

            //依赖注入
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(AutoWired.class)) {

                    Object bean = getBean(field.getName());
                    field.setAccessible(true);
                    field.set(instance, bean);
                }
            }

            //aware回调
            if (instance instanceof BeanNameAware) {
                ((BeanNameAware) instance).setBeanName(beanName);
            }

            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
            }

            //初始化
            if (instance instanceof InitializingBean) {
                ((InitializingBean) instance).afterPropertiesSet();
            }

            //beanPostProcessor
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);
            }

            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void scan(Class configClass) {
        ComponentScan annotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        String path = annotation.value();
        path = path.replace(".","/");
        ClassLoader classLoader = Ly61ApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        File[] files = file.listFiles();
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File listFile : listFiles) {
                String fileName = listFile.getAbsolutePath();
                String className = fileName.substring(fileName.indexOf("com"),fileName.indexOf(".class"));
                className = className.replace("\\", ".");
                try {
                    Class<?> aClass = classLoader.loadClass(className);
                    if (aClass.isAnnotationPresent(Component.class)) {
                        if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
                            BeanPostProcessor instance = (BeanPostProcessor) aClass.getDeclaredConstructor().newInstance();
                            beanPostProcessorList.add(instance);
                        }
                        Component component = aClass.getDeclaredAnnotation(Component.class);
                        String beanName = component.value();
                        BeanDefinition beanDefinition = new BeanDefinition();
                        if (aClass.isAnnotationPresent(Scope.class)) {
                            Scope scopeAnnotation = aClass.getDeclaredAnnotation(Scope.class);
                            beanDefinition.setScope(scopeAnnotation.value());
                            beanDefinition.setClazz(aClass);
                        } else {
                            beanDefinition.setScope("singleton");
                            beanDefinition.setClazz(aClass);
                        }
                        beanDefinitionMap.put(beanName,beanDefinition);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public Object getBean(String beanName) {
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                Object o = singletonObject.get(beanName);
                return o;
            } else {
                Object o = createBean(beanName,beanDefinition);
                return o;
            }
        } else {
            throw new NullPointerException();
        }
    }

}
