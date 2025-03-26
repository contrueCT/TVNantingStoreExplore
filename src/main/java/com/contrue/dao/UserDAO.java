package com.contrue.dao;

import com.contrue.entity.po.Comment;
import com.contrue.entity.po.Like;
import com.contrue.entity.po.Permission;
import com.contrue.entity.po.User;

import java.sql.Connection;
import java.util.List;

public interface UserDAO {
    boolean insertUser(User user, Connection conn);

    User findById(User user,Connection conn);

    User findByName(User user,Connection conn);

    List<Permission> getUserPermissionId(User user, Connection conn);

    List<Like> getUserLikesId(User user, Connection conn);

    List<Comment> getUserCommentsById(User user, Connection conn);

}
