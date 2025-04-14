package com.contrue.Framework.IOC;

import com.contrue.Framework.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        //调试
        System.out.println("尝试创建Bean: " + bd.getName() + " (" + clazz.getName() + ")");

        constructor.setAccessible(true);

        Object instance = constructor.newInstance();

        injectDependencies(instance);
        //调试
        System.out.println("成功创建Bean: " + bd.getName() + " (" + clazz.getName() + ")");
        return instance;
    }

    protected void injectDependencies(Object bean) throws Exception {
        Class<?> clazz = bean.getClass();
        System.out.println("开始为 " + bean.getClass().getName() + " 注入依赖");

        // 遍历字段，检查是否有需要注入的依赖
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                System.out.println("发现 @Autowired 字段: " + field.getName() + ", 类型: " + field.getType().getName());

                // 正确的方式：使用字段类型获取Bean
                Object dependency = getBean(field.getType());

                // 设置字段值
                field.set(bean, dependency);
                System.out.println("成功将类型 " + dependency.getClass().getName() + " 注入到字段 " + field.getName());
            }
        }
        System.out.println("完成为 " + bean.getClass().getName() + " 注入依赖");
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
        //调试
        System.out.println("尝试获取类型为 " + requiredType.getName() + " 的Bean");

        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition bd = beanDefinitionMap.get(beanName);
            if (requiredType.equals(bd.getType())) {
                //调试
                System.out.println("找到精确类型匹配: " + beanName);
                return requiredType.cast(getBean(beanName));
            }
        }

        //尝试接口/父类匹配
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition bd = beanDefinitionMap.get(beanName);
            if (requiredType.isAssignableFrom(bd.getType())) {
                //调试
                System.out.println("找到接口/父类匹配: " + beanName +
                        " (" + bd.getType().getName() + ")");
                return requiredType.cast(getBean(beanName));
            }
        }

        throw new Exception("没有找到类型为" + requiredType.getName() + "的Bean");
    }

    @Override
    public boolean containsBean(String name) {
        return beanDefinitionMap.containsKey(name);
    }
}
