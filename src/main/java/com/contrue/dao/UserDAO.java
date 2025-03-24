package com.contrue.dao;

import com.contrue.entity.po.Comment;
import com.contrue.entity.po.Like;
import com.contrue.entity.po.Permission;
import com.contrue.entity.po.User;

import java.util.List;

public interface UserDAO {
    boolean insertUser(User user);

    User findById(User user);

    User findByName(User user);

    List<Permission> getUserPermission(User user);

    List<Like> getUserLikes(User user);

    List<Comment> getUserComments(User user);

}
