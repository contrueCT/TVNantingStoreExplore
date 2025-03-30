package com.contrue.mapper;

import com.contrue.entity.po.Like;

/**
 * @author confff
 */
public interface LikeMapper {
    int insertLike(Like record);

    int delete(Like record);

}
