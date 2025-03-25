package com.contrue.service.Impl;

import com.contrue.dao.CommentDAO;
import com.contrue.dao.Impl.CommentDAOImpl;
import com.contrue.entity.po.Comment;
import com.contrue.service.CommentService;
import com.contrue.util.MyDBConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author confff
 */
public class CommentServiceImpl implements CommentService {

    private static class SingletonHolder {
        private static final CommentService INSTANCE = new CommentServiceImpl();
    }

    private CommentServiceImpl(){}

    public static CommentService getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public boolean deleteComment(Comment comment) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        CommentDAO commentDAO = CommentDAOImpl.getInstance();
        conn.setAutoCommit(false);
        try {
            if(comment==null){
                return false;
            }
            boolean result = commentDAO.deleteComment(comment,conn);
            conn.commit();
            return result;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            throw new RuntimeException(e);
        }finally {
            if(conn!=null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public boolean commentStore(Comment comment) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        CommentDAO commentDAO = CommentDAOImpl.getInstance();
        conn.setAutoCommit(false);
        try {
            if(comment==null){
                return false;
            }
            boolean result = commentDAO.addComment(comment,conn);
            conn.commit();
            return result;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            throw new RuntimeException(e);
        }finally {
            if(conn!=null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
