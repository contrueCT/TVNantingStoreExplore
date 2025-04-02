package com.contrue.web.servlet;

import com.contrue.entity.po.Comment;
import com.contrue.entity.po.Like;
import com.contrue.entity.po.User;
import com.contrue.entity.vo.Result;
import com.contrue.service.Impl.UserServiceImpl;
import com.contrue.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
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
}
