import com.contrue.entity.po.User;
import com.contrue.util.MyDBConnection;
import com.contrue.util.orm.MyORM;
import com.contrue.util.orm.MyORMImpl;
import com.contrue.util.orm.SelectMethod;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TestConnectionPool {

    @Test
    public void testConnectionPool() throws SQLException {
        Connection conn1 = MyDBConnection.getConnection();
        Connection conn2 = MyDBConnection.getConnection();
        Connection conn3 = MyDBConnection.getConnection();
        Connection conn4 = MyDBConnection.getConnection();
        Connection conn5 = MyDBConnection.getConnection();
        conn1.close();
        conn2.close();
        conn3.close();
        conn4.close();
        conn5.close();
    }

    @Test
    public void testORM() throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        MyORM orm = new MyORMImpl();
        User selected = new User();
        selected.setId(1);
        List<User> users = orm.select(conn,selected, SelectMethod.AND);
        System.out.println(users.get(0));
    }
}
