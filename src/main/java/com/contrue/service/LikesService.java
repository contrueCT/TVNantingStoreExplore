package com.contrue.service;

import com.contrue.entity.po.Like;

/**
 * @author confff
 */
public interface LikesService {

    boolean likeStore(Like like);

    boolean unlikeStore(Like like);

    boolean likeComment(Like like);

    boolean unlikeComment(Like like);
}
