package com.contrue.webapp.web.servlet;

import com.contrue.Framework.IOC.AnnotationConfigApplicationContext;
import com.contrue.Framework.annotation.Autowired;
import com.contrue.Framework.annotation.DubboReference;
import com.contrue.Framework.dubboIOC.DubboApplicationContext;
import com.contrue.Framework.dubboIOC.DubboContextListener;
import com.contrue.webapp.entity.vo.Result;
import com.contrue.util.SystemLogger;
import com.contrue.Framework.IOC.SimpleIoCContextListener;
import com.google.gson.Gson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author confff
 */

public abstract class BaseServlet extends HttpServlet {

    private AnnotationConfigApplicationContext iocContext;
    private DubboApplicationContext dubboContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        System.out.println("Servlet初始化");

        iocContext = SimpleIoCContextListener.getIoCContext(getServletContext());
        dubboContext = DubboContextListener.getDubboContext(getServletContext());

        if (iocContext == null) {
            throw new ServletException("IoC容器未初始化");
        }

        injectDependencies();
        System.out.println("Servlet依赖注入完成");
    }

    /**
     * 为Servlet注入依赖
     */
    private void injectDependencies() throws ServletException {
        Class<?> clazz = this.getClass();

        //遍历子类和父类的所有字段
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    try {
                        Object dependency = iocContext.getBean(field.getType());
                        if(dependency == null) {
                            throw new ServletException("未找到类型为" + field.getType().getName() + "的依赖");
                        }
                        field.set(this, dependency);
                    } catch (Exception e) {
                        throw new ServletException("自动注入字段" + field.getName() + "失败", e);
                    }
                }
                if(field.isAnnotationPresent(DubboReference.class)&& dubboContext != null) {
                    try {
                        field.setAccessible(true);
                        DubboReference annotation = field.getAnnotation(DubboReference.class);
                        Object reference = dubboContext.getDubboReference(
                                field.getType(),
                                annotation.version(),
                                annotation.group(),
                                annotation.timeout(),
                                annotation.check());
                        if(reference == null) {
                            throw new ServletException("未找到类型为" + field.getType().getName() + "的引用");
                        }

                        field.set(this, reference);
                        System.out.println("成功将类型 " + reference.getClass().getName() + " 注入到字段 " + field.getName());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        //获取请求方法、url、上下文路径
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        //测试
        System.out.println("接受到路径请求"+method+uri);

        String path = uri.substring(contextPath.length());
        //解析url参数
        boolean hasParams = extractPathParams(request, path);
        //获取方法名
        String methodName = determineMethodName(method,path,hasParams);

        //测试
        System.out.println("获取的方法名为："+methodName);

        try{

            Method targetMethod = findMethod(methodName);
            if(targetMethod != null){
                targetMethod.invoke(this,request,response);
            }else{
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Result result = Result.error("找不到该接口");
                writeJson(response,result);
            }
        }catch (Exception e){
            SystemLogger.logError(e.getMessage(),e);
            System.out.println("出错了");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Result result = Result.error("响应过程中出错");
            writeJson(response,result);
        }
    }

    //确定方法名
    protected String determineMethodName(String httpMethod, String path, boolean hasParamsInPath) {
        String urlPattern = getServletRegistration();

        if(path.contains("?")){
            path = path.substring(0, path.indexOf("?"));
        }

        //获得url模式数组
        String[] patternParts = urlPattern.split("/");
        String[] urlParts = path.split("/");

        StringBuilder methodNameBuilder = new StringBuilder();
        methodNameBuilder.append(httpMethod.toLowerCase());

        //如果url中存在资源路径参数
        if(hasParamsInPath&&urlPattern.contains("{")) {

            //资源名称（url最后一个非数字字段）
            String resourceName = null;
            boolean hasParams = false;

            for(int i = patternParts.length-1; i >= 0; i--) {
                String patternPart = patternParts[i];
                if(i<=urlParts.length-1){
                    String urlPart = urlParts[i];
                    if(!urlPart.isEmpty()){
                        //检查末尾是否是参数
                        if(patternPart.startsWith("{")){
                            hasParams = true;
                        }else{
                            resourceName = urlPart;
                            break;
                        }
                    }
                }
            }

            if(resourceName != null) {
                methodNameBuilder.append(resourceName.substring(0,1).toUpperCase()).append(resourceName.substring(1));

                if(hasParams) {
                    methodNameBuilder.append("Id");
                }
                return methodNameBuilder.toString();
            }

        }

        String baseName = path.substring(path.lastIndexOf("/")+1);
        return methodNameBuilder.append(baseName.substring(0, 1).toUpperCase())
                .append(baseName.substring(1))
                .toString();
    }

    protected abstract String getServletRegistration();

    //提取路径中的参数放入请求
    private boolean extractPathParams(HttpServletRequest request, String path) {
        String urlPattern = getServletRegistration();
        boolean hasParams = false;

        if(urlPattern.contains("{")) {
            boolean endsWithWildcard = urlPattern.endsWith("/*");
            String regexPattern = urlPattern;

            //先去掉/*
            if (endsWithWildcard) {
                regexPattern = urlPattern.substring(0, urlPattern.length() - 2);
            }

            //替换路径参数为捕获组
            regexPattern = regexPattern.replaceAll("\\{([^/]+)\\}", "([^/]+)");

            //处理*
            if (endsWithWildcard) {
                // 添加可选的额外路径部分
                regexPattern += "(/.*)?";
            } else {
                //处理中间的*
                regexPattern = regexPattern.replaceAll("\\*", ".*");
            }

            Pattern pattern = Pattern.compile("^" + regexPattern + "$");
            Matcher matcher = pattern.matcher(path);


            if (matcher.matches()) {
                // 提取所有参数名称
                Pattern paramPattern = Pattern.compile("\\{([^/]+)\\}");
                Matcher paramMatcher = paramPattern.matcher(urlPattern);

                int index = 1;
                while (paramMatcher.find() && index <= matcher.groupCount()) {
                    hasParams = true;
                    String paramName = paramMatcher.group(1);
                    String paramValue = matcher.group(index++);
                    // 将参数值存入request属性
                    request.setAttribute(paramName, paramValue);
                }
            }
        }
        return hasParams;
    }
    //查找目标方法
    private Method findMethod(String methodName) {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)&&
                method.getParameterCount() == 2&&
                method.getParameterTypes()[0].equals(HttpServletRequest.class)&&
                method.getParameterTypes()[1].equals(HttpServletResponse.class)) {
                return method;
            }
        }
        return null;
    }

    protected void writeJson(HttpServletResponse response, Result result) throws IOException {
        String respJson = new Gson().toJson(result);
        response.getWriter().write(respJson);
    }
}

