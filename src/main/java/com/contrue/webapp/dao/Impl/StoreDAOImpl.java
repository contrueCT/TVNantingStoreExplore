package com.contrue.webapp.dao.Impl;

import com.contrue.Framework.annotation.Component;
import com.contrue.webapp.dao.StoreDAO;
import com.contrue.webapp.entity.dto.PageResult;
import com.contrue.webapp.entity.po.Store;
import com.contrue.webapp.entity.po.StoreRole;
import com.contrue.webapp.mapper.StoreMapper;
import com.contrue.webapp.mapper.StoreRoleMapper;
import com.contrue.util.SystemLogger;
import com.contrue.Framework.orm.Resources;
import com.contrue.Framework.orm.session.SqlSession;
import com.contrue.Framework.orm.session.SqlSessionFactory;
import com.contrue.Framework.orm.session.SqlSessionFactoryBuilder;

import java.sql.Connection;
import java.util.List;

/**
 * @author confff
 */
@Component
public class StoreDAOImpl implements StoreDAO {

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

    private StoreMapper getStoreMapper(Connection conn){
        try {
            return sqlSession.getMapper(StoreMapper.class, conn);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private StoreRoleMapper getStoreRoleMapper(Connection conn){
        try{
            return sqlSession.getMapper(StoreRoleMapper.class,conn);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Store> getAllStores(Connection conn) {
        return getStoreMapper(conn).listAllStore(new Store());
    }

    @Override
    public Store findById(Store store, Connection conn) {
        return getStoreMapper(conn).findById(store).get(0);
    }

    @Override
    public Store findByName(Store store, Connection conn) {
        StoreMapper storeMapper = getStoreMapper(conn);
        List<Store> findStore = storeMapper.findByName(store);
        if(findStore!=null&& !findStore.isEmpty()){
            return findStore.get(0);
        }
        return null;
    }

    @Override
    public boolean addStore(Store store,Connection conn) {
        StoreMapper storeMapper = getStoreMapper(conn);
        StoreRoleMapper storeRoleMapper = getStoreRoleMapper(conn);
        if(storeMapper.insertStore(store)>0){
            Store checkStore = storeMapper.findByName(store).get(0);
            if(checkStore!=null){
                StoreRole storeRole = new StoreRole(store.getRoles().get(0).getId(),checkStore.getId());
                return storeRoleMapper.insert(storeRole)>0;
            }
        }
        return false;
    }

    @Override
    public boolean updateStore(Store store,Connection conn) {
        return getStoreMapper(conn).updateStore(store)>0;
    }

    @Override
    public List<Store> getStoresPage(PageResult<Store> storePageResult, Connection conn) {
        StoreMapper storeMapper = getStoreMapper(conn);
        return storeMapper.listStoreByPage(storePageResult);
    }

    @Override
    public boolean beSubscribed(Store store, Connection conn) {
        try{
            StoreMapper storeMapper = getStoreMapper(conn);
            if(storeMapper.addFollowersCount(store)>0){
                return true;
            }
            return false;
        }catch (Exception e){
            SystemLogger.logError(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean beUnSubscribed(Store store, Connection conn) {
        try{
            StoreMapper storeMapper = getStoreMapper(conn);
            if(storeMapper.reduceFollowersCount(store)>0){
                return true;
            }
            return false;
        }catch (Exception e){
            SystemLogger.logError(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }
}
