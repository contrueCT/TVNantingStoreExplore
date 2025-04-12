package com.contrue.dao;

import com.contrue.entity.dto.SubscribeDTO;
import com.contrue.entity.po.Comment;
import com.contrue.entity.po.Like;
import com.contrue.entity.po.Permission;
import com.contrue.entity.po.User;

import java.sql.Connection;
import java.util.List;

/**
 * @author confff
 */
public interface UserDAO {
    boolean insertUser(User user, Connection conn);

    User findById(User user,Connection conn);

    User findByName(User user,Connection conn);

    List<Permission> getUserPermissionId(User user, Connection conn);

    List<Like> getUserLikesId(User user, Connection conn);

    List<Comment> getUserCommentsById(User user, Connection conn);

    //将新关注插入关注表并将粉丝数+1
    boolean subscribeOther(SubscribeDTO subscribeDTO, Connection conn);

    //通过id查找对应用户并将粉丝数+1
    boolean beSubscribed(User user,Connection conn);
    //通过id查找对应用户并将粉丝数-1
    boolean beUnSubscribed(User user,Connection conn);

    boolean cancelSubscribe(SubscribeDTO subscribeDTO, Connection conn);

}
