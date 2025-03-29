package com.contrue.dao.Impl;

import com.contrue.dao.StoreDAO;
import com.contrue.entity.dto.PageResult;
import com.contrue.entity.po.Store;
import com.contrue.entity.po.StoreRole;
import com.contrue.mapper.StoreMapper;
import com.contrue.mapper.StoreRoleMapper;
import com.contrue.util.orm.Resources;
import com.contrue.util.orm.session.SqlSession;
import com.contrue.util.orm.session.SqlSessionFactory;
import com.contrue.util.orm.session.SqlSessionFactoryBuilder;

import java.sql.Connection;
import java.util.List;

/**
 * @author confff
 */
public class StoreDAOImpl implements StoreDAO {

    private static class SingletonHolder {
        private static final StoreDAO INSTANCE = new StoreDAOImpl();
    }

    public static StoreDAO getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private StoreDAOImpl(){}

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
}
