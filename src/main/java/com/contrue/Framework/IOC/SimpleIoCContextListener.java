package com.contrue.Framework.IOC;

import com.contrue.util.SystemLogger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.lang.reflect.Field;

/**
 * @author confff
 */
@WebListener
public class SimpleIoCContextListener implements ServletContextListener {

    private static final String IOC_CONTEXT_ATTRIBUTE = "IOC_CONTEXT";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

            context.scan("com.contrue.webapp");

            //输出注册的Bean以便调试
            dumpRegisteredBeans(context);

            ServletContext servletContext = sce.getServletContext();
            servletContext.setAttribute(IOC_CONTEXT_ATTRIBUTE, context);
            servletContext.log("SimpleIoC容器已初始化并存储在ServletContext中");
        } catch (Exception e) {
            SystemLogger.logError("初始化SimpleIoC容器失败", e);
            throw new RuntimeException("初始化SimpleIoC容器失败", e);
        }
    }

    // 打印注册的Bean以便调试
    private void dumpRegisteredBeans(AnnotationConfigApplicationContext context) {
        try {
            Field beanDefinitionMapField = com.contrue.Framework.IOC.SimpleIOC.class.getDeclaredField("beanDefinitionMap");
            beanDefinitionMapField.setAccessible(true);
            Object map = beanDefinitionMapField.get(context);
            System.out.println("已注册的Bean: " + map);
        } catch (Exception e) {
            System.err.println("无法获取已注册的Bean列表: " + e.getMessage());
        }
    }

    public static AnnotationConfigApplicationContext getIoCContext(ServletContext servletContext) {
        return (AnnotationConfigApplicationContext) servletContext.getAttribute(IOC_CONTEXT_ATTRIBUTE);
    }
}
