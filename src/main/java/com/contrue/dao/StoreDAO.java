package com.contrue.dao;

import com.contrue.entity.po.Store;

import java.sql.Connection;
import java.util.List;

/**
 * @author confff
 */
public interface StoreDAO {
    List<Store> getAllStores(Connection conn);

    Store getStoreById(Store store,Connection conn);

    //先插入，再查询，再插入Store_role
    boolean addStore(Store store,Connection conn);

    boolean updateStore(Store store,Connection conn);





}
