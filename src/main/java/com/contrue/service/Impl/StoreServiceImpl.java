package com.contrue.service.Impl;

import com.contrue.dao.StoreDAO;
import com.contrue.entity.po.Store;
import com.contrue.service.StoreService;

import java.util.Collections;
import java.util.List;

/**
 * @author confff
 */
public class StoreServiceImpl implements StoreService {
    //记得加初始化
    StoreDAO storeDAO;

    @Override
    public List<Store> getAllStore() {
        List<Store> storeList = storeDAO.getAllStores();
        if (storeList == null) {
            return Collections.emptyList();
        }
        return storeList;
    }

    @Override
    public Store getStoreDetailById(Store store) {
        return storeDAO.getStoreById(store);
    }


    @Override
    public boolean addStore(Store store) {
        if(store==null){
            return false;
        }
        if(store.getName()==null||store.getPassword()==null||store.getPhone()==null||store.getRoles()==null){
            return false;
        }
        return addStore(store);
    }

    @Override
    public boolean updateStore(Store store) {
        if(store==null){
            return false;
        }
        return storeDAO.updateStore(store);
    }
}
