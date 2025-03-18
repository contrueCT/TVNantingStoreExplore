package com.contrue.util.MyIOC;

import java.util.Set;

/**
 * @author confff
 */
public class AnnotationConfigApplicationContext {
    public AnnotationConfigApplicationContext(String pack) {
        Set<BeanDefinition> beanDefinitions = getBeanDefinitions(pack);
    }

    public Set<BeanDefinition> getBeanDefinitions(String pack) {

        return null;
    }
}
