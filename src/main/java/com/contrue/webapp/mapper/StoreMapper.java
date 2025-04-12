package com.contrue.webapp.mapper;

import com.contrue.webapp.entity.dto.PageResult;
import com.contrue.webapp.entity.po.Store;

import java.util.List;

/**
 * @author confff
 */
public interface StoreMapper {

    List<Store> listAllStore(Store store);

    List<Store> listStoreByPage(PageResult<Store> pageResult);

    List<Store> findById(Store store);

    List<Store> findByName(Store store);

    int insertStore(Store store);

    int updateStore(Store store);

    int addFollowersCount(Store store);

    int reduceFollowersCount(Store store);
}
