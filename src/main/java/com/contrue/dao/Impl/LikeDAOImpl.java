package com.contrue.dao.Impl;

import com.contrue.dao.LikeDAO;
import com.contrue.entity.po.Like;
import com.contrue.mapper.LikeMapper;
import com.contrue.util.SystemLogger;
import com.contrue.util.orm.Resources;
import com.contrue.util.orm.session.SqlSession;
import com.contrue.util.orm.session.SqlSessionFactory;
import com.contrue.util.orm.session.SqlSessionFactoryBuilder;

import java.sql.Connection;

/**
 * @author confff
 */
public class LikeDAOImpl implements LikeDAO {

    private static class SingletonHolder {
        private static final LikeDAO INSTANCE = new LikeDAOImpl();
    }

    private LikeDAOImpl(){}

    public static LikeDAO getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static SqlSessionFactory sqlSessionFactory;
    private static SqlSession sqlSession;

    static {
        try{
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("batis-config.xml"));
            sqlSession = sqlSessionFactory.openSession();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private LikeMapper getLikeMapper(Connection conn){
        try {
            return sqlSession.getMapper(LikeMapper.class, conn);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addLike(Like like,Connection conn) {
        return getLikeMapper(conn).insertLike(like)>0;
    }

    @Override
    public boolean deleteLike(Like like,Connection conn) {
        return getLikeMapper(conn).delete(like)>0;
    }

    @Override
    public Like getLike(Like like, Connection conn) {
        try {
            Like checkLike = getLikeMapper(conn).getLike(like).get(0);
            return checkLike;
        }catch (Exception e){
            return null;
        }
    }

}
