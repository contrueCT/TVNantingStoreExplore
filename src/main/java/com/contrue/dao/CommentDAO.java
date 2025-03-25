package com.contrue.dao;

import com.contrue.dao.Impl.CommentDAOImpl;
import com.contrue.entity.po.Comment;

import java.sql.Connection;

/**
 * @author confff
 */
public interface CommentDAO {
    boolean addComment(Comment comment, Connection conn);

    boolean deleteComment(Comment comment,Connection conn);
}
