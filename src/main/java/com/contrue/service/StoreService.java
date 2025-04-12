package com.contrue.service;

import com.contrue.entity.dto.PageResult;
import com.contrue.entity.dto.StoreSelect;
import com.contrue.entity.po.Store;
import com.contrue.entity.dto.AuthResult;
import com.contrue.entity.vo.StoreDetailVO;
import com.contrue.entity.vo.StoreListVO;

import java.sql.SQLException;
import java.util.List;

/**
 * @author confff
 */
public interface StoreService {

    PageResult<StoreListVO> getStoresByLikes(int page, int size);

    PageResult<StoreListVO> getStoresByComments(int page, int size);

    AuthResult loginStore(Store store) throws SQLException;

    List<Store> getAllStore() throws SQLException;

    StoreDetailVO getStoreDetailById(StoreSelect storeSelect) throws SQLException;

    boolean addStore(Store store) throws SQLException;

    boolean updateStore(Store store) throws SQLException;
}
