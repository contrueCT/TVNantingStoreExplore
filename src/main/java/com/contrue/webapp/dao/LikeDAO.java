package com.contrue.webapp.dao;

import com.contrue.webapp.entity.po.Like;

import java.sql.Connection;

/**
 * @author confff
 */
public interface LikeDAO {
    boolean addLike(Like like, Connection conn);

    //通过商铺Id删除
    boolean deleteLike(Like like,Connection conn);

    Like getLike(Like like,Connection conn);
}
