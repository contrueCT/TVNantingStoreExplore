package com.contrue.webapp.web.servlet;

import com.contrue.webapp.entity.dto.SubscribeDTO;
import com.contrue.webapp.entity.po.Comment;
import com.contrue.webapp.entity.po.Like;

import com.contrue.webapp.entity.po.User;
import com.contrue.webapp.entity.vo.Result;
import com.contrue.webapp.service.Impl.UserServiceImpl;
import com.contrue.webapp.service.UserService;
import com.contrue.util.JsonParser;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author confff
 */
@WebServlet("/api/users/*")
public class UserServlet extends BaseServlet {

    @Override
    protected String getServletRegistration() {
        return "/api/users/*";
    }

    UserService userService = UserServiceImpl.getInstance();
    //获取个人基本信息
    public void getMe(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String userId = request.getAttribute("subjectId").toString();
        User user = new User();
        user.setId(Integer.parseInt(userId));

        User CheckUser = userService.checkUserInfoById(user);
        writeJson(response,Result.success(CheckUser));
    }
    //获取评论
    public void getComments(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String userId = request.getAttribute("subjectId").toString();
        User user = new User();
        user.setId(Integer.parseInt(userId));

        List<Comment> comments = userService.checkOwnCommentsById(user);
        writeJson(response,Result.success(comments));
    }

    public void getLikes(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String userId = request.getAttribute("subjectId").toString();
        User user = new User();
        user.setId(Integer.parseInt(userId));

        List<Like> likes = userService.checkOwnLikesById(user);
        writeJson(response,Result.success(likes));
    }

    public void postSubscribe(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        SubscribeDTO subscribeDTO = JsonParser.parseJson(request, SubscribeDTO.class);
        if(userService.subscribeOther(subscribeDTO)) {
            writeJson(response, Result.success());
        } else {
            writeJson(response, Result.error("关注失败"));
        }
    }

    public void deleteSubscribe(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        SubscribeDTO subscribeDTO = JsonParser.parseJson(request, SubscribeDTO.class);
        if(userService.cancelSubscribe(subscribeDTO)) {
            writeJson(response, Result.success());
        } else {
            writeJson(response, Result.error("取消关注失败"));
        }
    }

}
