import com.contrue.webapp.mapper.UserMapper;
import com.contrue.webapp.entity.po.User;
import com.contrue.util.MyDBConnection;
import com.contrue.Framework.orm.Resources;
import com.contrue.Framework.orm.session.SqlSession;
import com.contrue.Framework.orm.session.SqlSessionFactory;
import com.contrue.Framework.orm.session.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class TestORM {
    @Test
    public void TestORMRunning() throws DocumentException {
        Connection conn = MyDBConnection.getConnection();
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder.build(Resources.getResourceAsStream("batis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class,conn);
            User user = new User();
            user.setUsername("pei神");
            user.setPassword("123456");
            user.setPhone("13999999999");

            int rows = userMapper.insertNewUser(user);
            System.out.println(rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Test
    public void TestORMSelect() throws DocumentException {
        Connection conn = MyDBConnection.getConnection();
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder.build(Resources.getResourceAsStream("batis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class,conn);
            User user = new User();
            user.setId(1);

            User checkUser = userMapper.joinSelectPermission(user).get(0);
            System.out.println(checkUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void TestORMSelectLikes() throws DocumentException {
        Connection conn = MyDBConnection.getConnection();
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder.build(Resources.getResourceAsStream("batis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class,conn);
            User user = new User();
            user.setId(1);

            User checkUser = userMapper.joinSelectPermission(user).get(0);
            System.out.println(checkUser.getPermissions());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
