package com.contrue.service.Impl;

import com.contrue.dao.CommentDAO;
import com.contrue.entity.po.Comment;
import com.contrue.service.CommentService;

/**
 * @author confff
 */
public class CommentServiceImpl implements CommentService {

    CommentDAO commentDAO;

    @Override
    public boolean deleteComment(Comment comment) {
        if(comment==null){
            return false;
        }
        return commentDAO.deleteComment(comment);
    }

    @Override
    public boolean commentStore(Comment comment) {
        if(comment==null){
            return false;
        }
        return commentDAO.addComment(comment);
    }
}
