package com.contrue.webapp.dao.Impl;

import com.contrue.webapp.dao.UserDAO;
import com.contrue.webapp.entity.dto.SubscribeDTO;
import com.contrue.webapp.mapper.SubscribesMapper;
import com.contrue.webapp.mapper.UserMapper;
import com.contrue.webapp.mapper.UserRoleMapper;
import com.contrue.util.SystemLogger;
import com.contrue.orm.Resources;
import com.contrue.orm.session.SqlSession;
import com.contrue.orm.session.SqlSessionFactory;
import com.contrue.orm.session.SqlSessionFactoryBuilder;
import com.contrue.webapp.entity.po.*;

import java.sql.Connection;
import java.util.List;

/**
 * @author confff
 */
public class UserDAOImpl implements UserDAO {

    private static class SingletonHolder {
        private static final UserDAO INSTANCE = new UserDAOImpl();
    }

    public static UserDAO getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private UserDAOImpl() {
    }

    private static SqlSessionFactory sqlSessionFactory;
    private static SqlSession sqlSession;

    static{
        try{
            sqlSessionFactory = SqlSessionFactoryBuilder.build(Resources.getResourceAsStream("batis-config.xml"));
            sqlSession = sqlSessionFactory.openSession();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private UserMapper getUserMapper(Connection conn){
        try {
            return sqlSession.getMapper(UserMapper.class, conn);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private UserRoleMapper getUserRoleMapper(Connection conn){
        try {
            return sqlSession.getMapper(UserRoleMapper.class, conn);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private SubscribesMapper getSubscribesMapper(Connection conn){
        try {
            return sqlSession.getMapper(SubscribesMapper.class, conn);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insertUser(User user, Connection conn) {
        UserRoleMapper userRoleMapper = getUserRoleMapper(conn);
        UserMapper userMapper = getUserMapper(conn);
        if(userMapper.insertNewUser(user)==1){
            User checkUser = userMapper.findByUserName(user).get(0);
            if(checkUser!=null&&checkUser.getId()!=null){
                UserRole userRole = new UserRole(checkUser.getId(), user.getRoles().get(0).getId());
                return userRoleMapper.insert(userRole)>0;
            }
        }
        return false;
    }

    @Override
    public User findById(User user,Connection conn) {
        UserMapper userMapper = getUserMapper(conn);
        return userMapper.findById(user).get(0);
    }

    @Override
    public User findByName(User user,Connection conn) {
        UserMapper userMapper = getUserMapper(conn);
        List<User> findUsers = userMapper.findByUserName(user);
        if(findUsers!=null&& !findUsers.isEmpty()){
            return findUsers.get(0);
        }
        return null;
    }

    @Override
    public List<Permission> getUserPermissionId(User user, Connection conn) {
        UserMapper userMapper = getUserMapper(conn);
        User checkUser = userMapper.joinSelectPermission(user).get(0);
        return checkUser.getPermissions();
    }

    @Override
    public List<Like> getUserLikesId(User user, Connection conn) {
        UserMapper userMapper = getUserMapper(conn);
        User checkUser = userMapper.joinSelectLikes(user).get(0);
        //测试
        System.out.println(checkUser.getLikes());
        if(checkUser.getLikes()!=null&& !checkUser.getLikes().isEmpty()&&checkUser.getLikes().get(0).getTargetId()!=null){
            return checkUser.getLikes();
        }
        return null;
    }

    @Override
    public List<Comment> getUserCommentsById(User user, Connection conn) {
        UserMapper userMapper = getUserMapper(conn);
        User checkUser = userMapper.joinSelectComment(user).get(0);
        //测试
        System.out.println(checkUser.getComments());
        if(checkUser!=null&&checkUser.getComments()!=null&& !checkUser.getComments().isEmpty()&&checkUser.getComments().get(0).getTargetId()!=null){
            return checkUser.getComments();
        }
        return null;
    }

    @Override
    public boolean subscribeOther(SubscribeDTO subscribeDTO, Connection conn) {
        UserMapper userMapper = getUserMapper(conn);
        SubscribesMapper subscribesMapper = getSubscribesMapper(conn);
        User user = new User();
        user.setId(subscribeDTO.getUserId());
        Subscribe subscribe = new Subscribe();
        subscribe.setUserId(subscribeDTO.getUserId());
        subscribe.setTargetId(subscribeDTO.getTargetId());
        subscribe.setTargetType(subscribeDTO.getTargetType());
        subscribe.setTargetName(subscribeDTO.getTargetName());
        try {
            return subscribesMapper.insertSubscribes(subscribe) > 0 && userMapper.addSubscribesCount(user) > 0;
        } catch (Exception e) {
            SystemLogger.logError(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean beSubscribed(User user, Connection conn) {
        UserMapper userMapper = getUserMapper(conn);
        try {
            return userMapper.addFollowersCount(user) > 0;
        } catch (Exception e) {
            SystemLogger.logError(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }



    @Override
    public boolean beUnSubscribed(User user, Connection conn) {
        UserMapper userMapper = getUserMapper(conn);
        try {
            return userMapper.reduceFollowersCount(user) > 0;
        } catch (Exception e) {
            SystemLogger.logError(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean cancelSubscribe(SubscribeDTO subscribeDTO, Connection conn) {
        UserMapper userMapper = getUserMapper(conn);
        SubscribesMapper subscribesMapper = getSubscribesMapper(conn);
        User user = new User();
        user.setId(subscribeDTO.getUserId());
        Subscribe subscribe = new Subscribe();
        subscribe.setUserId(subscribeDTO.getUserId());
        subscribe.setTargetId(subscribeDTO.getTargetId());
        subscribe.setTargetType(subscribeDTO.getTargetType());
        subscribe.setTargetName(subscribeDTO.getTargetName());
        try {
            return subscribesMapper.deleteSubscribes(subscribe) > 0 && userMapper.reduceSubscribesCount(user) > 0;
        } catch (Exception e) {
            SystemLogger.logError(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
