package com.contrue.mapper;

import com.contrue.entity.po.Store;

import java.util.List;

/**
 * @author confff
 */
public interface StoreMapper {

    List<Store> listAllStore(Store store);

    List<Store> findById(Store store);

    List<Store> findByName(Store store);

    int insertStore(Store store);

    int updateStore(Store store);

}
