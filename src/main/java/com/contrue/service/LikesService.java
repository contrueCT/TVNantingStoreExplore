package com.contrue.service;

import com.contrue.entity.po.Like;

import java.sql.SQLException;

/**
 * 补全点赞对象信息，targetType
 * 用户名和用户id
 * @author confff
 */
public interface LikesService {

    //通过对象Id和对象类型点赞
    boolean likeStore(Like like) throws SQLException;

    //通过对象id和对象类型删除
    boolean unlikeStore(Like like) throws SQLException;

    boolean likeComment(Like like) throws SQLException;

    boolean unlikeComment(Like like) throws SQLException;
}
