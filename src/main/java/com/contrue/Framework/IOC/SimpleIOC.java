package com.contrue.Framework.IOC;

import com.contrue.Framework.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author confff
 */
public class SimpleIOC implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        if(beanDefinitionMap.containsKey(beanName)) {
            throw new IllegalArgumentException("该Bean已经存在: " + beanName);
        }
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    public void registerBeanDefinition(Class<?> clazz) throws Exception {
        String beanName = clazz.getSimpleName();
        beanName = Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1);
        beanDefinitionMap.put(beanName, new BeanDefinition(beanName, clazz));
    }

    public void preInstanceSingletons() throws Exception {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if(beanDefinition.isSingleton()) {
                getBean(beanName);
            }
        }
    }

    @Override
    public Object getBean(String name) throws Exception {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new IllegalArgumentException("该bean'" + name + "'没有被定义");
        }

        if(beanDefinition.isSingleton()) {
            Object bean = singletonObjects.get(name);
            if (bean == null) {
                bean = createBean(beanDefinition);
                singletonObjects.put(name, bean);
            }
            return bean;
        } else {
            return createBean(beanDefinition);
        }
    }

    private Object createBean(BeanDefinition bd) throws Exception{
        Class<?> clazz = bd.getType();
        Object instance = clazz.getDeclaredConstructor().newInstance();

        injectDependencies(instance);
        return instance;
    }

    protected void injectDependencies(Object bean) throws Exception {
        Class<?> clazz = bean.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                Autowired autowired = field.getAnnotation(Autowired.class);
                try {
                    Object dependency = getBean(field.getType());
                    field.set(bean, dependency);
                } catch (Exception e) {
                    if(autowired.required()) {
                        throw new RuntimeException("Failed to inject dependency: " + field.getName(), e);
                    } else {
                        System.err.println("未找到这个类（不必要）: " + field.getName());
                    }

                }
            }
        }
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws Exception {
        Object bean = getBean(name);
        if(requiredType.isInstance(bean)) {
            return requiredType.cast(bean);
        } else {
            throw new IllegalArgumentException("Bean'" + name + "'不匹配" + requiredType.getName());
        }
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws Exception {
        for(String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition bd = beanDefinitionMap.get(beanName);
            if(requiredType.isAssignableFrom(bd.getType())) {
                return requiredType.cast(getBean(beanName));
            }
        }
        throw new IllegalArgumentException("没有找到类型为" + requiredType.getName() + "的bean");
    }

    @Override
    public boolean containsBean(String name) {
        return beanDefinitionMap.containsKey(name);
    }
}
