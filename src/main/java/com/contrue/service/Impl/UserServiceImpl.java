package com.contrue.service.Impl;

import com.contrue.dao.Impl.UserDAOImpl;
import com.contrue.dao.UserDAO;
import com.contrue.entity.po.Comment;
import com.contrue.entity.po.Like;
import com.contrue.entity.po.Permission;
import com.contrue.entity.po.User;
import com.contrue.service.UserService;
import com.contrue.util.MyDBConnection;
import com.contrue.util.SystemLogger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @author confff
 */
public class UserServiceImpl implements UserService {
    private UserServiceImpl(){
    }

    private static class SingletonHolder{
        private static final UserServiceImpl INSTANCE = new UserServiceImpl();
    }

    public static UserServiceImpl getInstance(){
        return SingletonHolder.INSTANCE;
    }

    @Override
    public boolean registerUser(User user) throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            if(user==null){
                return false;
            }
            if(user.getUsername()==null||user.getPassword()==null||user.getPhone()==null||user.getRoles()==null){
                return false;
            }
            User existThisUser = userDAO.findByName(user,conn);
            if(existThisUser!=null){
                return false;
            }
            conn.commit();
            return userDAO.insertUser(user,conn);
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            SystemLogger.logError(e.getMessage(),e);
            throw e;
        }finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public Integer loginUser(User user) throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            //非空校验
            if(user==null){
                return null;
            }
            if(user.getUsername()==null||user.getPassword()==null){
                return null;
            }
            //获取数据库中的用户数据
            User checkUser = userDAO.findByName(user,conn);
            if(checkUser==null){
                return null;
            }
            conn.commit();
            if(checkUser.getPassword().equals(user.getPassword())){
                return checkUser.getId();
            }
            return null;
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
    public List<Like> checkOwnLikesById(User user) throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            //非空校验
            if(user==null){
                return Collections.emptyList();
            }
            List<Like> likes = userDAO.getUserLikesId(user,conn);
            if(likes==null){
                return Collections.emptyList();
            }
            conn.commit();
            return likes;
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
    public List<Comment> checkOwnCommentsById(User user) throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            //非空校验
            if(user==null){
                return Collections.emptyList();
            }
            List<Comment> comments = userDAO.getUserCommentsById(user,conn);
            if(comments==null){
                return Collections.emptyList();
            }
            conn.commit();
            return comments;
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
    public User checkUserInfoById(User user) throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            //非空校验
            if(user==null){
                return null;
            }
            User checkUser = userDAO.findById(user,conn);
            if(checkUser==null){
                return null;
            }
            conn.commit();
            return checkUser;
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
    public List<Permission> checkUserPermissionsById(User user) throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            if(user==null){
                return Collections.emptyList();
            }
            List<Permission> permissions = userDAO.getUserPermissionId(user,conn);
            if(permissions==null){
                return Collections.emptyList();
            }
            conn.commit();
            return permissions;
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
}
