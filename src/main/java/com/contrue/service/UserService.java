package com.contrue.service;

import com.contrue.entity.po.Comment;
import com.contrue.entity.po.Like;
import com.contrue.entity.po.Permission;
import com.contrue.entity.po.User;

import java.sql.SQLException;
import java.util.List;

/**
 * @author confff
 */
public interface UserService {

    boolean registerUser(User user) throws SQLException;

    /**
     * 登录
     * @param user 用户名和密码
     * @return 如果user信息有误就返回null，否则返回该用户的id
     * @throws SQLException
     */
    Integer loginUser(User user) throws SQLException;

    List<Like> checkOwnLikesById(User user) throws SQLException;

    List<Comment> checkOwnCommentsById(User user) throws SQLException;

    User checkUserInfoById(User user) throws SQLException;

    List<Permission> checkUserPermissionsById(User user) throws SQLException;
}
