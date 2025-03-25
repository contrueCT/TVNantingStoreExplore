package com.contrue.service;

import com.contrue.entity.po.Store;

import java.sql.SQLException;
import java.util.List;

/**
 * @author confff
 */
public interface StoreService {



    List<Store> getAllStore() throws SQLException;

    Store getStoreDetailById(Store store) throws SQLException;



    boolean addStore(Store store) throws SQLException;

    boolean updateStore(Store store) throws SQLException;
}
