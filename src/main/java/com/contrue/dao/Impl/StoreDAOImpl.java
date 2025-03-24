package com.contrue.dao.Impl;

import com.contrue.dao.StoreDAO;
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

    StoreMapper storeMapper;
    Connection conn;
    StoreRoleMapper storeRoleMapper;

    public StoreDAOImpl(Connection conn) {
        this.conn = conn;
        try {
            SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder.build(Resources.getResourceAsStream("batis-config.xml"));
            SqlSession sqlSession = sqlSessionFactory.openSession();
            storeMapper = sqlSession.getMapper(StoreMapper.class,conn);
            storeRoleMapper = sqlSession.getMapper(StoreRoleMapper.class,conn);
            if(storeMapper==null||storeRoleMapper==null){
                throw new RuntimeException("mapper获取失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public List<Store> getAllStores() {
        return storeMapper.selectAllStore();
    }

    @Override
    public Store getStoreById(Store store) {
        return storeMapper.selectStoreById(store).get(0);
    }

    @Override
    public boolean addStore(Store store) {
        if(storeMapper.insertStore(store)>0){
            Store checkStore = storeMapper.selectStoreByName(store).get(0);
            if(checkStore!=null){
                StoreRole storeRole = new StoreRole(store.getRoles().get(0).getId(),checkStore.getId());
                return storeRoleMapper.insert(storeRole)>0;
            }
        }
        return false;
    }

    @Override
    public boolean updateStore(Store store) {
        return storeMapper.updateStore(store)>0;
    }
}
