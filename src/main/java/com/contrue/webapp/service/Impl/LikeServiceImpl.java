package com.contrue.webapp.service.Impl;

import com.contrue.Framework.annotation.Autowired;
import com.contrue.Framework.annotation.Component;
import com.contrue.webapp.dao.Impl.LikeDAOImpl;
import com.contrue.webapp.dao.LikeDAO;
import com.contrue.webapp.entity.po.Like;
import com.contrue.webapp.service.LikesService;
import com.contrue.util.MyDBConnection;
import com.contrue.util.SystemLogger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 获取连接并调用DAO
 * @author confff
 */
@Component
public class LikeServiceImpl implements LikesService {

    @Autowired
    LikeDAO likeDAO;

    @Override
    public boolean likeStore(Like like) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            if(like==null) return false;

            like.setTargetType("store");

            //判断是否已经点赞过
            Like checkLike = likeDAO.getLike(like,conn);
            if(checkLike!=null) return false;

            boolean result = likeDAO.addLike(like,conn);
            conn.commit();
            return result;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            throw new RuntimeException(e);
        }finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public boolean unlikeStore(Like like) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            if(like==null) return false;
            like.setTargetType("store");
            //检查点赞是否存在
            Like checkLike = likeDAO.getLike(like,conn);
            if(checkLike==null) {
                //测试
                System.out.println("取消点赞时没有找到点赞记录");
                return false;
            }

            boolean result = likeDAO.deleteLike(like,conn);
            conn.commit();
            return result;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            SystemLogger.logError(e.getMessage(),e);
            throw new RuntimeException(e);
        }finally {
            if(conn!=null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public boolean likeComment(Like like) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            if(like==null) return false;
            //检查点赞是否存在
            like.setTargetType("comment");
            Like checkLike = likeDAO.getLike(like,conn);
            if(checkLike!=null) return false;
            like.setTargetName("comment");
            boolean result = likeDAO.addLike(like,conn);
            conn.commit();
            return result;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            SystemLogger.logError(e.getMessage(),e);
            throw new RuntimeException(e);
        }finally {
            if(conn!=null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public boolean unlikeComment(Like like) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            if(like==null) return false;
            boolean result = likeDAO.deleteLike(like,conn);
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
