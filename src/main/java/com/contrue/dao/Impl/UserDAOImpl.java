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

    public UserDAOImpl(Connection conn) {
        this.conn = conn;
        try {

            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder.build(Resources.getResourceAsStream("batis-config.xml"));
            SqlSession sqlSession = sqlSessionFactory.openSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class,conn);
            UserRoleMapper userRoleMapper = sqlSession.getMapper(UserRoleMapper.class,conn);
            if(userMapper==null||userRoleMapper==null){
                throw new RuntimeException("mapper获取失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    UserMapper userMapper;
    Connection conn;
    UserRoleMapper userRoleMapper;

    @Override
    public boolean insertUser(User user) {
        if(userMapper.insertNewUser(user)==1){
            User checkUser = userMapper.findByUserName(user).get(0);
            if(checkUser!=null&&checkUser.getId()!=null){
                UserRole userRole = new UserRole(checkUser.getId(), user.getRoles().get(0).getId());
                return userRoleMapper.insertUserRole(userRole)>0;
            }
        }
        return false;
    }

    @Override
    public User findById(User user) {
        return userMapper.findById(user).get(0);
    }

    @Override
    public User findByName(User user) {
        return userMapper.findByUserName(user).get(0);
    }

    @Override
    public List<Permission> getUserPermission(User user) {
        User checkUser = userMapper.joinSelectPermission(user).get(0);
        return checkUser.getPermissions();
    }

    @Override
    public List<Like> getUserLikes(User user) {
        User checkUser = userMapper.joinSelectLikes(user).get(0);
        return checkUser.getLikes();
    }

    @Override
    public List<Comment> getUserComments(User user) {
        User checkUser = userMapper.joinSelectComment(user).get(0);
        return checkUser.getComments();
    }
}
