package com.contrue.service;

import com.contrue.entity.po.Comment;
import com.contrue.entity.po.Like;
import com.contrue.entity.po.Permission;
import com.contrue.entity.po.User;

import java.util.List;

/**
 * @author confff
 */
public interface UserService {

    boolean registerUser(User user);

    boolean loginUser(User user);

    List<Like> checkOwnLikes(User user);

    List<Comment> checkOwnComments(User user);

    User checkUserInfo(User user);

    List<Permission> checkUserPermissions(User user);
}
