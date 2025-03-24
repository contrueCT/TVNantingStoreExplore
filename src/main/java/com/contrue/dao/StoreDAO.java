package com.contrue.dao;

import com.contrue.entity.po.Store;

import java.util.List;

/**
 * @author confff
 */
public interface StoreDAO {
    List<Store> getAllStores();

    Store getStoreById(Store store);

    //先插入，再查询，再插入Store_role
    boolean addStore(Store store);

    boolean updateStore(Store store);





}
