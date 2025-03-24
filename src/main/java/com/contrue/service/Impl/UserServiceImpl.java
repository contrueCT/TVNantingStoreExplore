package com.contrue.service.Impl;

import com.contrue.dao.UserDAO;
import com.contrue.entity.po.Comment;
import com.contrue.entity.po.Like;
import com.contrue.entity.po.Permission;
import com.contrue.entity.po.User;
import com.contrue.service.UserService;

import java.util.Collections;
import java.util.List;

/**
 * @author confff
 */
public class UserServiceImpl implements UserService {
    UserDAO userDAO;

    @Override
    public boolean registerUser(User user) {
        if(user==null){
            return false;
        }
        if(user.getUsername()==null||user.getPassword()==null||user.getPhone()==null||user.getRoles()==null){
            return false;
        }
        User existThisUser = userDAO.findByName(user);
        if(existThisUser!=null){
            return false;
        }
        return userDAO.insertUser(user);
    }

    @Override
    public boolean loginUser(User user) {
        //非空校验
        if(user==null){
            return false;
        }
        if(user.getUsername()==null||user.getPassword()==null){
            return false;
        }
        //获取数据库中的用户数据
        User checkUser = userDAO.findByName(user);
        if(checkUser==null){
            return false;
        }
        return checkUser.getPassword().equals(user.getPassword());
    }

    @Override
    public List<Like> checkOwnLikes(User user) {
        //非空校验
        if(user==null){
            return Collections.emptyList();
        }
        List<Like> likes = userDAO.getUserLikes(user);
        if(likes==null){
            return Collections.emptyList();
        }

        return likes;
    }

    @Override
    public List<Comment> checkOwnComments(User user) {
        //非空校验
        if(user==null){
            return Collections.emptyList();
        }
        List<Comment> comments = userDAO.getUserComments(user);
        if(comments==null){
            return Collections.emptyList();
        }

        return comments;
    }

    @Override
    public User checkUserInfo(User user) {
        //非空校验
        if(user==null){
            return null;
        }
        User checkUser = userDAO.findById(user);
        if(checkUser==null){
            return null;
        }

        return checkUser;
    }

    @Override
    public List<Permission> checkUserPermissions(User user) {
        if(user==null){
            return Collections.emptyList();
        }
        List<Permission> permissions = userDAO.getUserPermission(user);
        if(permissions==null){
            return Collections.emptyList();
        }
        return permissions;
    }
}
