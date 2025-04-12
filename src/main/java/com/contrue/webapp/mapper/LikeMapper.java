package com.contrue.webapp.mapper;

import com.contrue.webapp.entity.po.Like;

import java.util.List;

/**
 * @author confff
 */
public interface LikeMapper {
    int insertLike(Like record);

    int delete(Like record);

    List<Like> getLike(Like record);

}
