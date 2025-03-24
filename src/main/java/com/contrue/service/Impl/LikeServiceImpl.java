package com.contrue.service.Impl;

import com.contrue.dao.LikeDAO;
import com.contrue.entity.po.Like;
import com.contrue.service.LikesService;

/**
 * @author confff
 */
public class LikeServiceImpl implements LikesService {

    LikeDAO likeDAO;

    @Override
    public boolean likeStore(Like like) {
        if(like==null) return false;
        like.setTargetType("store");
        return likeDAO.addLike(like);
    }

    @Override
    public boolean unlikeStore(Like like) {
        if(like==null) return false;
        return likeDAO.deleteLike(like);
    }

    @Override
    public boolean likeComment(Like like) {
        if(like==null) return false;
        like.setTargetType("comment");
        return likeDAO.addLike(like);
    }

    @Override
    public boolean unlikeComment(Like like) {
        if(like==null) return false;
        return likeDAO.deleteLike(like);
    }
}
