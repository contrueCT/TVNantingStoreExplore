package com.contrue.service.Impl;

import com.contrue.dao.Impl.StoreDAOImpl;
import com.contrue.dao.StoreDAO;
import com.contrue.entity.po.Store;
import com.contrue.service.StoreService;
import com.contrue.util.MyDBConnection;

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
            Store checkStore = StoreDAOImpl.getInstance().getStoreById(store,conn);
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
        try {
            if(store==null){
                return false;
            }
            if(store.getName()==null||store.getPassword()==null||store.getPhone()==null||store.getRoles()==null){
                return false;
            }
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
