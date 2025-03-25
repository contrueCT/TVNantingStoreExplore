package com.contrue.service;

import com.contrue.entity.po.Comment;

import java.sql.SQLException;

/**
 * @author confff
 */
public interface CommentService {

    boolean deleteComment(Comment comment) throws SQLException;

    boolean commentStore(Comment comment) throws SQLException;
}
