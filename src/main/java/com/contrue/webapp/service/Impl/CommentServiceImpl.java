package com.contrue.webapp.service.Impl;

import com.contrue.Framework.annotation.Autowired;
import com.contrue.Framework.annotation.Component;
import com.contrue.webapp.dao.CommentDAO;
import com.contrue.webapp.dao.Impl.CommentDAOImpl;
import com.contrue.webapp.entity.po.Comment;
import com.contrue.webapp.service.CommentService;
import com.contrue.util.MyDBConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author confff
 */
@Component
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDAO commentDAO;

    @Override
    public boolean deleteComment(Comment comment) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
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
