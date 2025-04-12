package com.contrue.webapp.mapper;

import com.contrue.webapp.entity.po.User;

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
    //通过id查找对应用户并将关注数+1
    int addSubscribesCount(User user);
    //通过id查找对应用户并将关注数+1
    int reduceSubscribesCount(User user);
    //通过id查找对应用户并将粉丝数+1
    int addFollowersCount(User user);
    //通过id查找对应用户并将粉丝数-1
    int reduceFollowersCount(User user);
}
