package com.contrue.web.filter;

import com.contrue.util.JWT.JWTUtil;
import com.contrue.util.SystemLogger;
import com.google.gson.Gson;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author confff
 */
@WebServlet("/api/")
public class AuthFilter implements Filter {

    private static final Set<String> EXCLUDE_PATHS = new HashSet<>(Arrays.asList(
            "/api/users/register",
            "/api/users/login",
            "/api/stores/register",
            "/api/stores/login",
            "/api/stores/^\\d+$"
            ));


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        if (EXCLUDE_PATHS.contains(url)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token != null&& token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try{
            if (token == null&&!JWTUtil.validateToken(token)) {
                handleUnauthorized(response);
                return;
            }

            String subjectId = JWTUtil.getSubjectId(token);
            String subjectName = JWTUtil.getSubjectName(token);
            String subjectType = JWTUtil.getSubjectType(token);
            request.setAttribute("subjectId", subjectId);
            request.setAttribute("subjectName", subjectName);
            request.setAttribute("subjectType", subjectType);
            filterChain.doFilter(request, response);
        }catch (Exception e){
            SystemLogger.logError("过滤失败",e);
            handleUnauthorized(response);
        }
    }

    private void handleUnauthorized(HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String,Object> map = new HashMap<>();
        map.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        map.put("message", "登录状态验证失败，请先登录");

        Gson gson = new Gson();
        PrintWriter writer = response.getWriter();
        writer.write(gson.toJson(map));
        writer.flush();
        writer.close();
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDE_PATHS.stream().anyMatch(excludePath->excludePath.equals(path)||path.matches(excludePath));
    }
}
