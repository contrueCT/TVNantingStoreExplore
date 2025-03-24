package com.contrue.dao;

import com.contrue.entity.po.Comment;

/**
 * @author confff
 */
public interface CommentDAO {
    boolean addComment(Comment comment);

    boolean deleteComment(Comment comment);

}
