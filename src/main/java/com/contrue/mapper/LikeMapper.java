package com.contrue.mapper;

import com.contrue.entity.po.Like;

import java.util.List;

/**
 * @author confff
 */
public interface LikeMapper {
    int insertLike(Like record);

    int delete(Like record);

    List<Like> getLike(Like record);

}
