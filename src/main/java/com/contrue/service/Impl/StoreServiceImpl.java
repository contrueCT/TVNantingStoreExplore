package com.contrue.service.Impl;

import com.contrue.dao.Impl.LikeDAOImpl;
import com.contrue.dao.Impl.StoreDAOImpl;
import com.contrue.dao.StoreDAO;
import com.contrue.entity.dto.PageResult;
import com.contrue.entity.dto.StoreSelect;
import com.contrue.entity.po.Like;
import com.contrue.entity.po.Store;
import com.contrue.entity.dto.AuthResult;
import com.contrue.entity.po.User;
import com.contrue.service.StoreService;
import com.contrue.util.JWT.JWTUtil;
import com.contrue.util.MyDBConnection;
import com.contrue.util.SystemLogger;
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
    public PageResult<Store> getStoresByLikes(int page, int size) {
        StoreDAO storeDAO = StoreDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        PageResult<Store> pageResult = new PageResult<>();
        pageResult.setSortBy("likes");
        pageResult.setCurrentPage(page);
        pageResult.setSize(size);
        List<Store> stores = storeDAO.getStoresPage(pageResult,conn);
        pageResult.setResults(stores);
        pageResult.setTotal(stores.size());
        return pageResult;
    }

    @Override
    public PageResult<Store> getStoresByComments(int page, int size) {
        StoreDAO storeDAO = StoreDAOImpl.getInstance();
        Connection conn = MyDBConnection.getConnection();
        PageResult<Store> pageResult = new PageResult<>();
        pageResult.setSortBy("comment");
        pageResult.setCurrentPage(page);
        pageResult.setSize(size);
        List<Store> stores = storeDAO.getStoresPage(pageResult,conn);
        pageResult.setResults(stores);
        pageResult.setTotal(stores.size());
        return pageResult;
    }

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
    public Store getStoreDetailById(StoreSelect storeSelect) throws SQLException {
        Store store = storeSelect.getStore();
        User user = storeSelect.getUser();

        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try{
            Store checkStore = StoreDAOImpl.getInstance().findById(store,conn);
            //判断是否被当前用户点赞
            Like selectedLike = new Like();
            selectedLike.setUserId(user.getId());
            selectedLike.setTargetId(checkStore.getId());
            Like isLike = LikeDAOImpl.getInstance().getLike(selectedLike, conn);
            if(isLike!=null){
                checkStore.setLiked(true);
            }
            conn.commit();
            return checkStore;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            SystemLogger.logError(e.getMessage(),e);
            return null;
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
