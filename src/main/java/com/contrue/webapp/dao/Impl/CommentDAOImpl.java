package com.contrue.webapp.dao.Impl;

import com.contrue.Framework.annotation.Component;
import com.contrue.webapp.dao.CommentDAO;
import com.contrue.webapp.entity.po.Comment;
import com.contrue.webapp.mapper.CommentMapper;
import com.contrue.Framework.orm.Resources;
import com.contrue.Framework.orm.session.SqlSession;
import com.contrue.Framework.orm.session.SqlSessionFactory;
import com.contrue.Framework.orm.session.SqlSessionFactoryBuilder;

import java.sql.Connection;

/**
 * @author confff
 */
@Component
public class CommentDAOImpl implements CommentDAO {

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

    private CommentMapper getcommentMapper(Connection conn){
        try {
            return sqlSession.getMapper(CommentMapper.class, conn);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addComment(Comment comment,Connection conn) {
        return getcommentMapper(conn).insert(comment)>0;
    }

    @Override
    public boolean deleteComment(Comment comment,Connection conn) {
        return getcommentMapper(conn).deleteById(comment)>0;
    }
}
