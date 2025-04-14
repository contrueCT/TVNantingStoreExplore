package com.contrue.Framework.IOC;

import com.contrue.Framework.annotation.Component;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author confff
 */
public class AnnotationConfigApplicationContext extends SimpleIOC {

    public void scan(String basePackage) throws Exception {
        Set<Class<?>> classes = scanPackage(basePackage);
        for (Class<?> clazz : classes) {

            if (clazz.isAnnotationPresent(Component.class)) {
                Component component = clazz.getAnnotation(Component.class);
                String beanName = component.value();
                if (beanName.isEmpty()) {
                    beanName = clazz.getSimpleName();
                    beanName = Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1);
                }
                registerBeanDefinition(beanName, new BeanDefinition(beanName, clazz));
            }
        }

        preInstanceSingletons();
    }

    private Set<Class<?>> scanPackage(String basePackage) throws Exception {
        Set<Class<?>> classes = new HashSet<>();

        // 获取包的物理路径
        String basePath = basePackage.replace('.', '/');
        URL url = Thread.currentThread().getContextClassLoader().getResource(basePath);
        if (url != null) {
            File directory = new File(url.getFile());
            if (directory.exists()) {
                for (File file : directory.listFiles()) {
                    if (file.isDirectory()) {
                        //递归扫描子目录
                        classes.addAll(scanPackage(basePackage + "." + file.getName()));
                    } else if (file.getName().endsWith(".class")) {
                        //加载类
                        String className = basePackage + '.' + file.getName().substring(0, file.getName().length() - 6);
                        classes.add(Class.forName(className));
                    }
                }
            }
        }

        return classes;
    }
}
