package com.contrue.mapper;

import com.contrue.entity.po.Store;

import java.util.List;

/**
 * @author confff
 */
public interface StoreMapper {

    List<Store> selectAllStore();

    List<Store> selectStoreById(Store store);

    List<Store> selectStoreByName(Store store);

    int insertStore(Store store);

    int updateStore(Store store);

}
