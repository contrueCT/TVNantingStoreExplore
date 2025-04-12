package com.contrue.webapp.mapper;

import com.contrue.webapp.entity.po.Comment;

/**
 * @author confff
 */
public interface CommentMapper {
    int insert(Comment record);

    int deleteById(Comment record);
}
