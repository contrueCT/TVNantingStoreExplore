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

    boolean loginUser(User user) throws SQLException;

    List<Like> checkOwnLikes(User user) throws SQLException;

    List<Comment> checkOwnComments(User user) throws SQLException;

    User checkUserInfo(User user) throws SQLException;

    List<Permission> checkUserPermissions(User user) throws SQLException;
}
