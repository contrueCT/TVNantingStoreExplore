package com.contrue.webapp.service;

import com.contrue.webapp.entity.dto.PageResult;
import com.contrue.webapp.entity.dto.StoreSelect;
import com.contrue.webapp.entity.po.Store;
import com.contrue.webapp.entity.dto.AuthResult;
import com.contrue.webapp.entity.vo.StoreDetailVO;
import com.contrue.webapp.entity.vo.StoreListVO;

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
