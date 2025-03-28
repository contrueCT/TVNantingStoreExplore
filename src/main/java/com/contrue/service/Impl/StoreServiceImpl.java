package com.contrue.service.Impl;

import com.contrue.dao.Impl.StoreDAOImpl;
import com.contrue.dao.StoreDAO;
import com.contrue.entity.po.Store;
import com.contrue.entity.vo.AuthResult;
import com.contrue.service.StoreService;
import com.contrue.util.JWT.JWTUtil;
import com.contrue.util.MyDBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @author confff
 */
public class StoreServiceImpl implements StoreService {
    //记得加初始化
    private static class SingletonHolder {
        private static final StoreServiceImpl INSTANCE = new StoreServiceImpl();
    }

    public static StoreServiceImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private StoreServiceImpl() {}

    @Override
    public AuthResult loginStore(Store store) throws SQLException {
        StoreDAO storeDAO = StoreDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        AuthResult authResult = new AuthResult();
        try {
            //非空校验
            if(store==null){
                authResult.setCode(400);
            }
            if(store.getName()==null||store.getPassword()==null){
                authResult.setCode(400);
            }
            //获取数据库中的用户数据
            Store checkStore = storeDAO.findByName(store, conn);
            if(checkStore==null){
                authResult.setCode(400);
            }
            authResult.setMsg("登录信息错误，登录失败");
            conn.commit();
            if(BCrypt.checkpw(store.getPassword(),checkStore.getPassword())){
                //登录验证成功
                String AccessToken = JWTUtil.generateAccessToken(checkStore.getId().toString(),"store",checkStore.getName());
                String refreshToken = JWTUtil.generateRefreshToken(checkStore.getId().toString(),"store",checkStore.getName());
                authResult.setAccessToken(AccessToken);
                authResult.setRefreshToken(refreshToken);
                authResult.setCode(200);
                authResult.setMsg("登录成功");
                return authResult;
            }else{
                authResult.setCode(401);
                authResult.setMsg("密码错误，登录失败");
            }
            return authResult;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            throw new RuntimeException(e);
        }finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public List<Store> getAllStore() throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            List<Store> storeList = StoreDAOImpl.getInstance().getAllStores(conn);
            if (storeList == null) {
                return Collections.emptyList();
            }
            conn.commit();
            return storeList;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            throw new RuntimeException(e);
        }finally {
            if(conn!=null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public Store getStoreDetailById(Store store) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try{
            Store checkStore = StoreDAOImpl.getInstance().findById(store,conn);
            conn.commit();
            return checkStore;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            throw new RuntimeException(e);
        }finally {
            if(conn!=null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }


    @Override
    public boolean addStore(Store store) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            if(store==null){
                return false;
            }
            if(store.getName()==null||store.getPassword()==null||store.getPhone()==null||store.getRoles()==null){
                return false;
            }

            store.setPassword(BCrypt.hashpw(store.getPassword(), BCrypt.gensalt()));

            boolean result = StoreDAOImpl.getInstance().addStore(store,conn);
            conn.commit();
            return result;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            throw new RuntimeException(e);
        }finally {
            if(conn!=null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public boolean updateStore(Store store) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            if(store==null){
                return false;
            }
            boolean result = StoreDAOImpl.getInstance().updateStore(store,conn);
            conn.commit();
            return result;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            throw new RuntimeException(e);
        }finally {
            if(conn!=null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
