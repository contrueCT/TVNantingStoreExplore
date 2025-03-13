package com.contrue.NantingStoreExplore.servlet;

import com.contrue.NantingStoreExplore.util.SystemLogger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;



/**
 * @author confff
 */
public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("json/application");
        try{
            String methodName = request.getParameter("action");
            if(methodName == null){
                SystemLogger.logError("缺少参数请求",new RuntimeException());
                throw new RuntimeException("缺少参数请求");
            }
            //获取方法
            Method method = this.getClass().getMethod(methodName,HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this,request,response);

        } catch (Exception e) {
            SystemLogger.logError(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }
}

