package com.contrue.Framework.dubboIOC;

import com.contrue.Framework.IOC.AnnotationConfigApplicationContext;
import com.contrue.Framework.IOC.BeanDefinition;
import com.contrue.Framework.annotation.DubboReference;
import com.contrue.Framework.annotation.DubboService;
import org.apache.dubbo.config.*;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author confff
 */
public class DubboApplicationContext extends AnnotationConfigApplicationContext {

    private ApplicationConfig applicationConfig;
    private RegistryConfig registryConfig;
    private ProtocolConfig protocolConfig;

    private DubboBootstrap dubboBootstrap;

    private final Map<String, ServiceConfig<?>> serviceConfigMap = new ConcurrentHashMap<>();
    private final Map<String, ReferenceConfig<?>> referenceConfigMap = new ConcurrentHashMap<>();

    public void initDubbo(String applicationName, String registryAddress, int port) {
        applicationConfig = new ApplicationConfig(applicationName);
        registryConfig = new RegistryConfig(registryAddress);
        protocolConfig = new ProtocolConfig("dubbo", port);

        dubboBootstrap = DubboBootstrap.getInstance();
        dubboBootstrap.application(applicationConfig)
                .registry(registryConfig)
                .protocol(protocolConfig);
    }

    public void start() {
        dubboBootstrap.start();
    }

    @Override
    public void scan(String basePackage) throws Exception{
        super.scan(basePackage);

        publishDubboServices();
    }

    private void publishDubboServices() throws Exception {
        for (String beanName : beanDefinitionMap.keySet()) {
            Object bean = getBean(beanName);
            Class<?> beanClass = bean.getClass();

            if (beanClass.isAnnotationPresent(DubboService.class)) {
                DubboService annotation = beanClass.getAnnotation(DubboService.class);

                Class<?> interfaceClass;
                if (annotation.interfaceClass() != null) {
                    interfaceClass = Class.forName(annotation.interfaceClass());
                } else if (beanClass.getInterfaces().length > 0) {
                    interfaceClass = beanClass.getInterfaces()[0];
                }else{
                    throw new IllegalArgumentException("没有指定接口类");
                }

                ServiceConfig<Object> serviceConfig = new ServiceConfig<>();
                serviceConfig.setInterface(interfaceClass);
                serviceConfig.setRef(bean);

                if (annotation.version() != null && !annotation.version().isEmpty()) {
                    serviceConfig.setVersion(annotation.version());
                }

                if (annotation.group() != null && !annotation.group().isEmpty()) {
                    serviceConfig.setGroup(annotation.group());
                }

                if (annotation.timeout() > 0) {
                    serviceConfig.setTimeout(annotation.timeout());
                }

                serviceConfig.setRetries(annotation.retries());

                String serviceKey = generateServiceKey(interfaceClass.getName(), annotation.version(), annotation.group());

                serviceConfigMap.put(serviceKey, serviceConfig);

                dubboBootstrap.service(serviceConfig);

                System.out.println("注册dubbo服务:"+serviceKey);

            }
        }
    }

    protected void publishDubboServices(AnnotationConfigApplicationContext iocContext) throws Exception {
        Field beanDefinitionMapField = AnnotationConfigApplicationContext.class.getDeclaredField("beanDefinitionMap");
        beanDefinitionMapField.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, BeanDefinition> beanDefinitionMap = (Map<String, BeanDefinition>) beanDefinitionMapField.get(iocContext);

        for (String beanName : beanDefinitionMap.keySet()) {
            Object bean = getBean(beanName);
            Class<?> beanClass = bean.getClass();

            if (beanClass.isAnnotationPresent(DubboService.class)) {
                DubboService annotation = beanClass.getAnnotation(DubboService.class);

                Class<?> interfaceClass;
                if (annotation.interfaceClass() != null) {
                    interfaceClass = Class.forName(annotation.interfaceClass());
                } else if (beanClass.getInterfaces().length > 0) {
                    interfaceClass = beanClass.getInterfaces()[0];
                }else{
                    throw new IllegalArgumentException("没有指定接口类");
                }

                ServiceConfig<Object> serviceConfig = new ServiceConfig<>();
                serviceConfig.setInterface(interfaceClass);
                serviceConfig.setRef(bean);

                if (annotation.version() != null && !annotation.version().isEmpty()) {
                    serviceConfig.setVersion(annotation.version());
                }

                if (annotation.group() != null && !annotation.group().isEmpty()) {
                    serviceConfig.setGroup(annotation.group());
                }

                if (annotation.timeout() > 0) {
                    serviceConfig.setTimeout(annotation.timeout());
                }

                serviceConfig.setRetries(annotation.retries());

                String serviceKey = generateServiceKey(interfaceClass.getName(), annotation.version(), annotation.group());

                serviceConfigMap.put(serviceKey, serviceConfig);

                dubboBootstrap.service(serviceConfig);

                System.out.println("注册dubbo服务:"+serviceKey);

            }
        }
    }

    private String generateServiceKey(String interfaceName, String version, String group) {
        StringBuilder sb = new StringBuilder(interfaceName);

        if (version != null && !version.isEmpty()) {
            sb.append(":");
            sb.append(version);
        }

        if (group != null && !group.isEmpty()) {
            sb.append(":");
            sb.append(group);
        }

        return sb.toString();
    }

    @Override
    protected void injectDependencies(Object bean) throws Exception{
        super.injectDependencies(bean);

        Class<?> clazz = bean.getClass();
        for(Field field : clazz.getDeclaredFields()){
            if(field.isAnnotationPresent(DubboReference.class)){
                field.setAccessible(true);
                DubboReference annotation = field.getAnnotation(DubboReference.class);

                Class<?> referenceType = field.getType();

                Object reference = getDubboReference(referenceType, annotation.version(), annotation.group(), annotation.timeout(), annotation.check());

                if(reference == null){
                    throw new IllegalArgumentException("未找到类型为" + referenceType.getName() + "的引用");
                }
                field.set(bean, reference);
                System.out.println("成功将类型 " + reference.getClass().getName() + " 注入到字段 " + field.getName());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getDubboReference(Class<T> referenceType, String version, String group,
                                   int timeout, boolean check){
        String referenceKey = generateServiceKey(referenceType.getName(), version, group);

        ReferenceConfig<T> referenceConfig = (ReferenceConfig<T>) referenceConfigMap.get(referenceKey);
        if(referenceConfig != null){
            return referenceConfig.get();
        }

        referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(referenceType);

        if (version != null && !version.isEmpty()) {
            referenceConfig.setVersion(version);
        }

        if (group != null && !group.isEmpty()) {
            referenceConfig.setGroup(group);
        }

        if (timeout > 0) {
            referenceConfig.setTimeout(timeout);
        }

        referenceConfig.setCheck(check);

        referenceConfigMap.put(referenceKey, referenceConfig);

        return referenceConfig.get();
    }

    public void close(){
        if(dubboBootstrap != null){
            dubboBootstrap.stop();
        }
    }
}
