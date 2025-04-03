package com.contrue.service;

import com.contrue.entity.dto.PageResult;
import com.contrue.entity.dto.StoreSelect;
import com.contrue.entity.po.Store;
import com.contrue.entity.dto.AuthResult;

import java.sql.SQLException;
import java.util.List;

/**
 * @author confff
 */
public interface StoreService {

    PageResult<Store> getStoresByLikes(int page, int size);

    PageResult<Store> getStoresByComments(int page, int size);

    AuthResult loginStore(Store store) throws SQLException;

    List<Store> getAllStore() throws SQLException;

    Store getStoreDetailById(StoreSelect storeSelect) throws SQLException;

    boolean addStore(Store store) throws SQLException;

    boolean updateStore(Store store) throws SQLException;
}
