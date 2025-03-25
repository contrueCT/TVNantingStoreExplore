package com.contrue.mapper;

import com.contrue.entity.po.Comment;

/**
 * @author confff
 */
public interface CommentMapper {
    int insert(Comment record);

    int deleteById(Comment record);
}
