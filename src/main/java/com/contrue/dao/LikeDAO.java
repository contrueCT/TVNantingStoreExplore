package com.contrue.dao;

import com.contrue.entity.po.Like;

import java.sql.Connection;

/**
 * @author confff
 */
public interface LikeDAO {
    boolean addLike(Like like, Connection conn);

    boolean deleteLike(Like like,Connection conn);

}
