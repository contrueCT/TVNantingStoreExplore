package com.contrue.web.servlet;

import com.contrue.entity.vo.Result;
import com.contrue.util.SystemLogger;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author confff
 */

public abstract class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("json/application;charset=utf-8");
        Gson gson = new Gson();

        //获取请求方法、url、上下文路径
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        //测试
        System.out.println("接受到路径请求"+method+uri);

        String path = uri.substring(contextPath.length());

        String methodName = determineMethodName(method,path);

        try{
            extractPathParams(request,path);

            Method targetMethod = findMethod(methodName);
            if(targetMethod != null){
                targetMethod.invoke(this,request,response);
            }else{
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Result result = Result.error("找不到该接口");
                String respJson = gson.toJson(result);
                response.getWriter().write(respJson);
            }
        }catch (Exception e){
            SystemLogger.logError(e.getMessage(),e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Result result = Result.error("响应过程中出错");
            String respJson = gson.toJson(result);
            response.getWriter().write(respJson);
        }
    }

    //确定方法名
    protected String determineMethodName(String httpMethod, String path) {
        String urlPattern = getServletRegistration();

        if(path.contains("?")){
            path = path.substring(0, path.indexOf("?"));
        }

        if(urlPattern.endsWith("/*")) {
            urlPattern = urlPattern.substring(0, urlPattern.length()-2);
        }
        //获得url模式数组
        String[] patternParts = urlPattern.split("/");

        StringBuilder methodNameBuilder = new StringBuilder();
        methodNameBuilder.append(httpMethod.toLowerCase());

        //如果url中存在资源路径参数
        if(urlPattern.contains("{")) {

            //资源名称（url最后一个非数字字段）
            String resourceName = null;
            boolean hasParams = false;

            for(String patternPart : patternParts) {
                if(!patternPart.isEmpty()){
                    //检查末尾是否是参数
                    if(patternPart.startsWith("{")){
                        hasParams = true;
                    }else{
                        resourceName = patternPart;
                        break;
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
    private void extractPathParams(HttpServletRequest request, String path) {
        String urlPattern = getServletRegistration();

        if(urlPattern.contains("{")) {
            String regex = urlPattern.replaceAll("\\{([^/]+)\\}", "([^/]+)");
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(path);


            if (matcher.matches()) {
                // 提取所有参数名称
                Pattern paramPattern = Pattern.compile("\\{([^/]+)\\}");
                Matcher paramMatcher = paramPattern.matcher(urlPattern);

                int index = 1;
                while (paramMatcher.find() && index <= matcher.groupCount()) {
                    String paramName = paramMatcher.group(1);
                    String paramValue = matcher.group(index++);
                    // 将参数值存入request属性
                    request.setAttribute(paramName, paramValue);
                }
            }
        }
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

