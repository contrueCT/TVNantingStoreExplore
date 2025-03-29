import com.contrue.entity.dto.PageResult;
import com.contrue.entity.po.Like;
import com.contrue.entity.po.Role;
import com.contrue.entity.po.Store;
import com.contrue.entity.po.User;
import com.contrue.service.Impl.StoreServiceImpl;
import com.contrue.service.Impl.UserServiceImpl;
import com.contrue.service.StoreService;
import com.contrue.service.UserService;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

public class TestService {
    @Test
    public void TestUserServiceCheckUserInfo() throws SQLException {
        User user = new User();
        user.setId(2);
        UserService userService = UserServiceImpl.getInstance();
        User checkUser = userService.checkUserInfoById(user);
        System.out.println(checkUser);
    }

    @Test
    public void TestUserService() throws SQLException {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setAddress("test");
        user.setEmail("test@test.com");
        user.setPhone("12345678910");
        user.setRoles(Role.getNormalUserRoleList());
        UserService userService = UserServiceImpl.getInstance();
        boolean b = userService.registerUser(user);
        System.out.println(b);
    }

    @Test
    public void TestStoreService() throws SQLException {
        Store store = new Store();
        store.setName("Java炒粉");
        store.setAddress("南亭");
        store.setPassword("123456");
        store.setPhone("13888888888");
        store.setShortDescription("百年老字号");
        store.setDescription("百年老字号，认准Java炒粉");
        store.setRoles(Role.getNormalUserRoleList());
        StoreService storeService = StoreServiceImpl.getInstance();
        boolean b = storeService.addStore(store);
        System.out.println(b);
    }

    @Test
    public void TestStoreServiceGetStoreBy() throws SQLException {
        StoreService storeService = StoreServiceImpl.getInstance();
        PageResult pageResult = storeService.getStoresByComments(1,10);
        System.out.println(pageResult.getResults());
    }
}



