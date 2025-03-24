package com.contrue.service;

import com.contrue.entity.po.Store;

import java.util.List;

/**
 * @author confff
 */
public interface StoreService {



    List<Store> getAllStore();

    Store getStoreDetailById(Store store);



    boolean addStore(Store store);

    boolean updateStore(Store store);
}
