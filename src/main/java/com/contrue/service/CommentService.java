package com.contrue.service;

import com.contrue.entity.po.Comment;

/**
 * @author confff
 */
public interface CommentService {

    boolean deleteComment(Comment comment);

    boolean commentStore(Comment comment);
}
