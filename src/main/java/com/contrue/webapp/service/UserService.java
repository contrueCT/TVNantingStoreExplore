package com.contrue.webapp.service;

import com.contrue.webapp.entity.dto.SubscribeDTO;
import com.contrue.webapp.entity.po.Comment;
import com.contrue.webapp.entity.po.Like;
import com.contrue.webapp.entity.po.Permission;
import com.contrue.webapp.entity.po.User;
import com.contrue.webapp.entity.dto.AuthResult;

import java.sql.SQLException;
import java.util.List;

/**
 * @author confff
 */
public interface UserService {

    /**
     * 注册，此处的密码要加密
     * @param user 带有原始密码的user对象
     * @return 是否注册成功
     */
    boolean registerUser(User user) throws SQLException;

    /**
     * 登录，此处的密码不能加密
     *
     * @param user 用户名和原始密码
     * @return 如果user信息有误就返回null，否则返回该用户的id
     */
    AuthResult loginUser(User user) throws SQLException;

    List<Like> checkOwnLikesById(User user) throws SQLException;

    List<Comment> checkOwnCommentsById(User user) throws SQLException;

    User checkUserInfoById(User user) throws SQLException;

    List<Permission> checkUserPermissionsById(User user) throws SQLException;

    boolean subscribeOther(SubscribeDTO subscribeDTO) throws SQLException;

    boolean cancelSubscribe(SubscribeDTO subscribeDTO) throws SQLException;
}
