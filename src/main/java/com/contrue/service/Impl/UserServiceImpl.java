package com.contrue.service.Impl;

import com.contrue.constant.ClassConstants;
import com.contrue.constant.MessageConstant;
import com.contrue.constant.StateCodeConstant;
import com.contrue.dao.Impl.StoreDAOImpl;
import com.contrue.dao.Impl.UserDAOImpl;
import com.contrue.dao.StoreDAO;
import com.contrue.dao.UserDAO;
import com.contrue.entity.dto.SubscribeDTO;
import com.contrue.entity.po.*;
import com.contrue.entity.dto.AuthResult;
import com.contrue.service.UserService;
import com.contrue.util.JWT.JWTUtil;
import com.contrue.util.MyDBConnection;
import com.contrue.util.SystemLogger;
import org.mindrot.jbcrypt.BCrypt;

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
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
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
    public AuthResult loginUser(User user) throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        AuthResult authResult = new AuthResult();
        try {
            //非空校验
            if(user==null){
                authResult.setCode(StateCodeConstant.INFO_ERROR);
            }
            if(user.getUsername()==null||user.getPassword()==null){
                authResult.setCode(StateCodeConstant.INFO_ERROR);
            }
            //获取数据库中的用户数据
            User checkUser = userDAO.findByName(user,conn);
            if(checkUser==null){
                authResult.setCode(StateCodeConstant.USER_NOT_FOUND);
            }
            authResult.setMsg(MessageConstant.LOGIN_FAIL_INFO_ERROR);
            if(BCrypt.checkpw(user.getPassword(),checkUser.getPassword())){
                //登录验证成功
                String accessToken = JWTUtil.generateAccessToken(checkUser.getId().toString(),ClassConstants.USER_CLASS_SIMPLE,checkUser.getUsername());
                String refreshToken = JWTUtil.generateRefreshToken(checkUser.getId().toString(),ClassConstants.USER_CLASS_SIMPLE,checkUser.getUsername());
                authResult.setAccessToken(accessToken);
                authResult.setRefreshToken(refreshToken);
                authResult.setCode(StateCodeConstant.SUCCESS);
                authResult.setMsg(MessageConstant.LOGIN_SUCCESS);
                return authResult;
            }else{
                authResult.setCode(401);
                authResult.setMsg(MessageConstant.LOGIN_FAIL_PASSWORD_ERROR);
            }
            return authResult;
        } catch (Exception e) {
            SystemLogger.logError(e.getMessage(),e);
            throw new RuntimeException(e);
        }finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public List<Like> checkOwnLikesById(User user) throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        try {
            //非空校验
            if(user==null){
                return Collections.emptyList();
            }
            List<Like> likes = userDAO.getUserLikesId(user,conn);
            if(likes==null){
                return Collections.emptyList();
            }
            return likes;
        } catch (Exception e) {
            SystemLogger.logError(e.getMessage(),e);
            throw new RuntimeException(e);
        }finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public List<Comment> checkOwnCommentsById(User user) throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        try {
            //非空校验
            if(user==null){
                return Collections.emptyList();
            }
            List<Comment> comments = userDAO.getUserCommentsById(user,conn);
            if(comments==null){
                return Collections.emptyList();
            }
            return comments;
        } catch (Exception e) {
            SystemLogger.logError(e.getMessage(),e);
            throw new RuntimeException(e);
        }finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public User checkUserInfoById(User user) throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        try {
            //非空校验
            if(user==null){
                return null;
            }
            User checkUser = userDAO.findById(user,conn);
            if(checkUser==null){
                return null;
            }
            return checkUser;
        } catch (Exception e) {
            SystemLogger.logError(e.getMessage(),e);
            throw new RuntimeException(e);
        }finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public List<Permission> checkUserPermissionsById(User user) throws SQLException {
        UserDAO userDAO = UserDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        try {
            if(user==null){
                return Collections.emptyList();
            }
            List<Permission> permissions = userDAO.getUserPermissionId(user,conn);
            if(permissions==null){
                return Collections.emptyList();
            }
            return permissions;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean subscribeOther(SubscribeDTO subscribeDTO) throws SQLException {
        if(subscribeDTO==null){
            return false;
        }
        if(subscribeDTO.getUserId()==null||subscribeDTO.getTargetId()==null||subscribeDTO.getTargetType()==null){
            return false;
        }
        UserDAO userDAO = UserDAOImpl.getInstance();
        StoreDAO storeDAO = StoreDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try{
            if(ClassConstants.USER_CLASS_SIMPLE.equals(subscribeDTO.getTargetType())){
                User targteUser = new User();
                targteUser.setId(subscribeDTO.getTargetId());
                if(userDAO.subscribeOther(subscribeDTO,conn) && userDAO.beSubscribed(targteUser,conn)){
                    conn.commit();
                    return true;
                }
            }else{
                //商铺
                Store store = new Store();
                store.setId(subscribeDTO.getTargetId());
                if(userDAO.subscribeOther(subscribeDTO,conn) && storeDAO.beSubscribed(store,conn)){
                    conn.commit();
                    return true;
                }
            }
        }catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            SystemLogger.logError(e.getMessage(), e);
            throw e;
        }finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
        return false;
    }

    @Override
    public boolean cancelSubscribe(SubscribeDTO subscribeDTO) throws SQLException {
        if(subscribeDTO==null){
            return false;
        }
        if(subscribeDTO.getUserId()==null||subscribeDTO.getTargetId()==null||subscribeDTO.getTargetType()==null){
            return false;
        }
        UserDAO userDAO = UserDAOImpl.getInstance();
        StoreDAO storeDAO = StoreDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try{
            if(ClassConstants.USER_CLASS_SIMPLE.equals(subscribeDTO.getTargetType())){
                User targetUser = new User();
                targetUser.setId(subscribeDTO.getTargetId());
                if(userDAO.cancelSubscribe(subscribeDTO,conn) && userDAO.beUnSubscribed(targetUser,conn)){
                    conn.commit();
                    return true;
                }
            }else{
                //商铺
                Store store = new Store();
                store.setId(subscribeDTO.getTargetId());
                if(userDAO.cancelSubscribe(subscribeDTO,conn) && storeDAO.beUnSubscribed(store,conn)){
                    conn.commit();
                    return true;
                }
            }
        }catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            SystemLogger.logError(e.getMessage(), e);
            throw e;
        }finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
        return false;
    }
}
