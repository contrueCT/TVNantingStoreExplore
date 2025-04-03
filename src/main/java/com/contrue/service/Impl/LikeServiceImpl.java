package com.contrue.service.Impl;

import com.contrue.dao.Impl.LikeDAOImpl;
import com.contrue.dao.LikeDAO;
import com.contrue.entity.po.Like;
import com.contrue.service.LikesService;
import com.contrue.util.MyDBConnection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 获取连接并调用DAO
 * @author confff
 */
public class LikeServiceImpl implements LikesService {

    private static class SingletonHolder {
        private static final LikeServiceImpl INSTANCE = new LikeServiceImpl();
    }

    public static LikeServiceImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private LikeServiceImpl() {
    }

    @Override
    public boolean likeStore(Like like) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            if(like==null) return false;
            //判断是否已经点赞过
            Like checkLike = LikeDAOImpl.getInstance().getLike(like,conn);
            if(checkLike!=null) return false;

            like.setTargetType("Store");
            boolean result = LikeDAOImpl.getInstance().addLike(like,conn);
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
            //检查点赞是否存在
            Like checkLike = LikeDAOImpl.getInstance().getLike(like,conn);
            if(checkLike==null) return false;

            boolean result = LikeDAOImpl.getInstance().deleteLike(like,conn);
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
    public boolean likeComment(Like like) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            if(like==null) return false;
            //检查点赞是否存在
            Like checkLike = LikeDAOImpl.getInstance().getLike(like,conn);
            if(checkLike!=null) return false;
            like.setTargetType("Comment");
            like.setTargetName("comment");
            boolean result = LikeDAOImpl.getInstance().deleteLike(like,conn);
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
    public boolean unlikeComment(Like like) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            if(like==null) return false;
            boolean result = LikeDAOImpl.getInstance().deleteLike(like,conn);
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
