package com.contrue.mapper;

import com.contrue.entity.po.User;

import java.util.List;

/**
 * @author confff
 */
public interface UserMapper {

    //方法名与xml中的方法保持一致
    int insertNewUser(User user);

    List<User> findById(User user);

    List<User> joinSelectPermission(User user);

    List<User> joinSelectLikes(User user);

    List<User> findByUserName(User user);

    List<User> joinSelectComment(User user);
}
