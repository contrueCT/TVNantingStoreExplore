package com.contrue.dao;

import com.contrue.entity.dto.PageResult;
import com.contrue.entity.po.Store;

import java.sql.Connection;
import java.util.List;

/**
 * @author confff
 */
public interface StoreDAO {
    List<Store> getAllStores(Connection conn);

    Store findById(Store store, Connection conn);

    Store findByName(Store store, Connection conn);

    //先插入，再查询，再插入Store_role
    boolean addStore(Store store,Connection conn);

    /**
     * 通过id查询并修改
     * @return 是否更新成功
     */
    boolean updateStore(Store store,Connection conn);

    List<Store> getStoresPage(PageResult<Store> storePageResult, Connection conn);





}
