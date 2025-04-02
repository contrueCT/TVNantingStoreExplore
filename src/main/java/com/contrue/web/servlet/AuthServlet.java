package com.contrue.web.servlet;

import com.contrue.entity.dto.AuthResult;
import com.contrue.entity.po.Store;
import com.contrue.entity.po.User;
import com.contrue.entity.vo.AuthToken;
import com.contrue.entity.vo.Result;
import com.contrue.service.Impl.StoreServiceImpl;
import com.contrue.service.Impl.UserServiceImpl;
import com.contrue.service.StoreService;
import com.contrue.service.UserService;
import com.contrue.util.JWT.JWTUtil;
import com.contrue.util.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author confff
 */
@WebServlet("/api/auth/*")
public class AuthServlet extends BaseServlet
{
    @Override
    protected String getServletRegistration() {
        return "/api/auth/*";
    }

    UserService userService = UserServiceImpl.getInstance();
    StoreService storeService = StoreServiceImpl.getInstance();

    //用户注册
    public void putUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        User user = JsonParser.parseJson(request, User.class);
        if(userService.registerUser(user)){
            writeJson(response, Result.success());
        }else{
            writeJson(response, Result.error("注册失败"));
        }
    }

    //商铺注册
    public void putStore(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        Store store = JsonParser.parseJson(request, Store.class);
        if(storeService.addStore(store)){
            writeJson(response, Result.success());
        }else{
            writeJson(response, Result.error("商铺注册失败"));
        }
    }

    //商铺登录
    public void postStore(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        Store store = JsonParser.parseJson(request, Store.class);
        //权限验证
        AuthResult authResult = storeService.loginStore(store);

        AuthToken authToken = new AuthToken(authResult.getAccessToken(), authResult.getRefreshToken());
        Result result = Result.success(authToken);
        result.setCode(authResult.getCode());
        result.setMassage(authResult.getMsg());
        //写入响应
        writeJson(response, result);
    }

    //用户登录
    public void postUser(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        User user = JsonParser.parseJson(request, User.class);
        //权限验证
        AuthResult authResult = userService.loginUser(user);

        AuthToken authToken = new AuthToken(authResult.getAccessToken(), authResult.getRefreshToken());
        Result result = Result.success(authToken);
        result.setCode(authResult.getCode());
        result.setMassage(authResult.getMsg());
        //写入响应
        writeJson(response, result);
    }

    public void postRefresh(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        JsonObject jsonObject = new Gson().fromJson(sb.toString(), JsonObject.class);

        // 提取refreshToken字段
        if (!jsonObject.has("refreshToken") || jsonObject.get("refreshToken").isJsonNull()) {
            response.setStatus(400);
            writeJson(response,Result.error("刷新令牌为空"));
            return;
        }

        String refreshToken = jsonObject.get("refreshToken").getAsString();
        String subjectId = JWTUtil.getSubjectId(refreshToken);
        String subjectName = JWTUtil.getSubjectName(refreshToken);
        String subjectType = JWTUtil.getSubjectType(refreshToken);
        String accessToken = JWTUtil.generateAccessToken(subjectId, subjectName, subjectType);
        AuthToken authToken = new AuthToken(accessToken, refreshToken);
        if (accessToken != null) {
            writeJson(response, Result.success(authToken));
        }else{
            //测试
            System.out.println("访问令牌获取失败");

            writeJson(response,Result.error("访问令牌获取失败"));
        }
    }
}
