package com.contrue.dao.Impl;

import com.contrue.dao.UserDAO;
import com.contrue.entity.po.*;
import com.contrue.mapper.UserMapper;
import com.contrue.mapper.UserRoleMapper;
import com.contrue.util.orm.Resources;
import com.contrue.util.orm.session.SqlSession;
import com.contrue.util.orm.session.SqlSessionFactory;
import com.contrue.util.orm.session.SqlSessionFactoryBuilder;

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

    @Override
    public boolean insertUser(User user,Connection conn) {
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
        return checkUser.getLikes();
    }

    @Override
    public List<Comment> getUserCommentsById(User user, Connection conn) {
        UserMapper userMapper = getUserMapper(conn);
        User checkUser = userMapper.joinSelectComment(user).get(0);
        return checkUser.getComments();
    }
}
