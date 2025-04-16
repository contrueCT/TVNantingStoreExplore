package com.contrue.Framework.dubboIOC;

import com.contrue.Framework.dubboIOC.DubboApplicationContext;
import com.contrue.Framework.IOC.AnnotationConfigApplicationContext;
import com.contrue.Framework.IOC.SimpleIoCContextListener;
import com.contrue.util.SystemLogger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author confff
 */
public class DubboContextListener implements ServletContextListener {

    private static final String DUBBO_CONTEXT_ATTRIBUTE = "DUBBO_CONTEXT";
    private DubboApplicationContext dubboContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext servletContext = sce.getServletContext();

            String applicationName = servletContext.getInitParameter("dubboApplicationName");
            String registryAddress = servletContext.getInitParameter("dubboRegistryAddress");
            String port = servletContext.getInitParameter("dubboPort");

            if(applicationName == null || registryAddress == null || port == null) {
                throw new IllegalArgumentException("请在web.xml中配置dubboApplicationName、dubboRegistryAddress和dubboPort");
            }

            dubboContext = new DubboApplicationContext();
            dubboContext.initDubbo(applicationName, registryAddress, Integer.parseInt(port));

            AnnotationConfigApplicationContext iocContext = new SimpleIoCContextListener().getIoCContext(servletContext);

            if(iocContext == null) {
                throw new IllegalStateException("IoC容器未初始化");
            }

            dubboContext.publishDubboServices(iocContext);

            dubboContext.start();

            servletContext.setAttribute(DUBBO_CONTEXT_ATTRIBUTE, dubboContext);
            servletContext.log("Dubbo容器已初始化并启动");

        } catch (Exception e) {
            SystemLogger.logError("初始化Dubbo容器失败", e);
            throw new RuntimeException("初始化Dubbo容器失败", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (dubboContext != null) {
            try {
                dubboContext.close();
            } catch (Exception e) {
                SystemLogger.logError("关闭Dubbo容器失败", e);
            }

        }
    }

    public static DubboApplicationContext getDubboContext(ServletContext servletContext) {
        return (DubboApplicationContext) servletContext.getAttribute(DUBBO_CONTEXT_ATTRIBUTE);
    }

}
