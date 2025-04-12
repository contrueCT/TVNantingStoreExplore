package com.contrue.webapp.dao;

import com.contrue.webapp.entity.po.Comment;

import java.sql.Connection;

/**
 * @author confff
 */
public interface CommentDAO {
    boolean addComment(Comment comment, Connection conn);

    boolean deleteComment(Comment comment,Connection conn);
}
