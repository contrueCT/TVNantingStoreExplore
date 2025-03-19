import com.contrue.mapper.UserMapper;
import com.contrue.po.User;
import com.contrue.util.MyDBConnection;
import com.contrue.util.orm.Resources;
import com.contrue.util.orm.configuration.Configuration;
import com.contrue.util.orm.configuration.XmlConfigBuilder;
import com.contrue.util.orm.session.SqlSession;
import com.contrue.util.orm.session.SqlSessionFactory;
import com.contrue.util.orm.session.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

            List<User> list = userMapper.joinSelectPermission(user);
            System.out.println(list);
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

            List<User> list = userMapper.joinSelectLikes(user);
            System.out.println(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
