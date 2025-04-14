package com.contrue.webapp.service.Impl;

import com.contrue.Framework.annotation.Autowired;
import com.contrue.Framework.annotation.Component;
import com.contrue.webapp.dao.Impl.LikeDAOImpl;
import com.contrue.webapp.dao.Impl.StoreDAOImpl;
import com.contrue.webapp.dao.StoreDAO;
import com.contrue.webapp.entity.dto.PageResult;
import com.contrue.webapp.entity.dto.StoreSelect;
import com.contrue.webapp.entity.dto.AuthResult;
import com.contrue.webapp.entity.po.*;
import com.contrue.webapp.entity.vo.StoreDetailVO;
import com.contrue.webapp.entity.vo.StoreListVO;
import com.contrue.webapp.service.StoreService;
import com.contrue.util.JWT.JWTUtil;
import com.contrue.util.MyDBConnection;
import com.contrue.util.SystemLogger;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author confff
 */
@Component
public class StoreServiceImpl implements StoreService {
    @Autowired
    private StoreDAO storeDAO;
    @Autowired
    private LikeDAOImpl likeDAO;

    @Override
    public PageResult<StoreListVO> getStoresByLikes(int page, int size) {
        Connection conn = MyDBConnection.getConnection();
        PageResult<Store> pageResultForSelect = new PageResult<>();
        pageResultForSelect.setSortBy("store_likes_count");
        pageResultForSelect.setCurrentPage(page);
        pageResultForSelect.setSize(size);
        List<Store> stores = storeDAO.getStoresPage(pageResultForSelect,conn);

        List<StoreListVO> storeListVOs = new ArrayList<>();

        for(Store store : stores){
            StoreListVO storeListVO = new StoreListVO();
            storeListVO.setId(store.getId());
            storeListVO.setName(store.getName());
            storeListVO.setLikesCount(store.getLikesCount());
            storeListVO.setCommentsCount(store.getCommentsCount());
            storeListVO.setShortDescription(store.getShortDescription());
            storeListVO.setAddress(store.getAddress());
            storeListVOs.add(storeListVO);
        }

        PageResult<StoreListVO> pageResult = new PageResult<>();
        pageResult.setSortBy("likes");
        pageResult.setCurrentPage(page);
        pageResult.setSize(size);
        pageResult.setResults(storeListVOs);
        pageResult.setTotal(stores.size());
        return pageResult;
    }

    @Override
    public PageResult<StoreListVO> getStoresByComments(int page, int size) {
        Connection conn = MyDBConnection.getConnection();
        PageResult<Store> pageResultForSelect = new PageResult<>();
        pageResultForSelect.setSortBy("store_comments_count");
        pageResultForSelect.setCurrentPage(page);
        pageResultForSelect.setSize(size);

        List<Store> stores = storeDAO.getStoresPage(pageResultForSelect,conn);

        List<StoreListVO> storeListVOs = new ArrayList<>();
        for(Store store : stores){
            StoreListVO storeListVO = new StoreListVO();
            storeListVO.setId(store.getId());
            storeListVO.setName(store.getName());
            storeListVO.setLikesCount(store.getLikesCount());
            storeListVO.setCommentsCount(store.getCommentsCount());
            storeListVO.setShortDescription(store.getShortDescription());
            storeListVO.setAddress(store.getAddress());
            storeListVOs.add(storeListVO);
        }
        PageResult<StoreListVO> pageResult = new PageResult<>();
        pageResult.setSortBy("comment");
        pageResult.setCurrentPage(page);
        pageResult.setSize(size);
        pageResult.setResults(storeListVOs);
        pageResult.setTotal(stores.size());
        return pageResult;
    }

    @Override
    public AuthResult loginStore(Store store) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        AuthResult authResult = new AuthResult();
        try {
            //非空校验
            if(store==null){
                authResult.setCode(400);
            }
            if(store.getName()==null||store.getPassword()==null){
                authResult.setCode(400);
            }
            //获取数据库中的用户数据
            Store checkStore = storeDAO.findByName(store, conn);
            if(checkStore==null){
                authResult.setCode(400);
            }
            authResult.setMsg("登录信息错误，登录失败");
            conn.commit();

            if(BCrypt.checkpw(store.getPassword(),checkStore.getPassword())){
                //登录验证成功
                String AccessToken = JWTUtil.generateAccessToken(checkStore.getId().toString(),"store",checkStore.getName());
                String refreshToken = JWTUtil.generateRefreshToken(checkStore.getId().toString(),"store",checkStore.getName());
                authResult.setAccessToken(AccessToken);
                authResult.setRefreshToken(refreshToken);
                authResult.setCode(200);
                authResult.setMsg("登录成功");
                return authResult;
            }else{
                authResult.setCode(401);
                authResult.setMsg("密码错误，登录失败");
            }
            return authResult;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            throw new RuntimeException(e);
        }finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public List<Store> getAllStore() throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        try {
            conn.setAutoCommit(false);
            List<Store> storeList = storeDAO.getAllStores(conn);
            if (storeList == null) {
                return Collections.emptyList();
            }
            conn.commit();
            return storeList;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            throw new RuntimeException(e);
        }finally {
            if(conn!=null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public StoreDetailVO getStoreDetailById(StoreSelect storeSelect) throws SQLException {
        Store store = storeSelect.getStore();
        User user = storeSelect.getUser();

        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try{
            Store checkStore = storeDAO.findById(store,conn);
            //判断是否被当前用户点赞
            Like selectedLike = new Like();
            selectedLike.setUserId(user.getId());
            selectedLike.setTargetId(checkStore.getId());
            selectedLike.setTargetType("store");
            Like isLike = likeDAO.getLike(selectedLike, conn);
            if(isLike!=null){
                checkStore.setLiked(true);
            }
            System.out.println("是否被点赞："+checkStore.isLiked());
            //判断评论是否被点赞
            List<Comment> comments = checkStore.getComments();
            for(Comment comment : comments){
                Like selectedLikeComment = new Like();
                selectedLikeComment.setUserId(user.getId());
                selectedLikeComment.setTargetId(comment.getId());
                selectedLikeComment.setTargetType("comment");
                Like isLikeComment = likeDAO.getLike(selectedLikeComment, conn);
                if(isLikeComment!=null){
                    comment.setLiked(true);
                }
            }

            //设置商铺详情
            StoreDetailVO storeDetailVO = getStoreDetailVO(checkStore);

            conn.commit();
            return storeDetailVO;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            SystemLogger.logError(e.getMessage(),e);
            return null;
        }finally {
            if(conn!=null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    private static StoreDetailVO getStoreDetailVO(Store checkStore) {
        StoreDetailVO storeDetailVO = new StoreDetailVO();
        storeDetailVO.setId(checkStore.getId());
        storeDetailVO.setName(checkStore.getName());
        storeDetailVO.setDescription(checkStore.getDescription());
        storeDetailVO.setAddress(checkStore.getAddress());
        storeDetailVO.setPhone(checkStore.getPhone());
        storeDetailVO.setShortDescription(checkStore.getShortDescription());
        storeDetailVO.setLikesCount(checkStore.getLikesCount());
        storeDetailVO.setCommentsCount(checkStore.getCommentsCount());
        storeDetailVO.setComments(checkStore.getComments());
        storeDetailVO.setLiked(checkStore.isLiked());
        return storeDetailVO;
    }


    @Override
    public boolean addStore(Store store) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            if(store==null){
                return false;
            }
            store.setRoles(Role.getStoreRoleList());
            if(store.getName()==null||store.getPassword()==null||store.getPhone()==null||store.getRoles()==null){
                return false;
            }

            store.setPassword(BCrypt.hashpw(store.getPassword(), BCrypt.gensalt()));

            boolean result = storeDAO.addStore(store,conn);
            conn.commit();
            return result;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            throw new RuntimeException(e);
        }finally {
            if(conn!=null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public boolean updateStore(Store store) throws SQLException {
        Connection conn = MyDBConnection.getConnection();
        conn.setAutoCommit(false);
        try {
            if(store==null){
                return false;
            }
            boolean result = storeDAO.updateStore(store,conn);
            conn.commit();
            return result;
        } catch (Exception e) {
            if(conn!=null){
                conn.rollback();
            }
            throw new RuntimeException(e);
        }finally {
            if(conn!=null){
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    @Override
    public PageResult<StoreListVO> getStoreListCount() {
        Connection conn = MyDBConnection.getConnection();
        List<Store> allStores = storeDAO.getAllStores(conn);
        int total = allStores.size();
        PageResult<StoreListVO> result =  new PageResult<StoreListVO>();
        result.setTotal(total);
        return result;
    }
}
