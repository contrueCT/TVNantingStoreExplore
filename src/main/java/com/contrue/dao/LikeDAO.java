package com.contrue.dao;

import com.contrue.entity.po.Like;

/**
 * @author confff
 */
public interface LikeDAO {
    boolean addLike(Like like);

    boolean deleteLike(Like like);

}
