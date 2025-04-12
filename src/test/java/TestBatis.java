import com.contrue.Framework.orm.Resources;
import com.contrue.Framework.orm.session.SqlSession;
import com.contrue.Framework.orm.session.SqlSessionFactory;
import com.contrue.Framework.orm.session.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class TestBatis {
    @Test
    public void testBatis() throws DocumentException {
        String resource = "batis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
    }
}
