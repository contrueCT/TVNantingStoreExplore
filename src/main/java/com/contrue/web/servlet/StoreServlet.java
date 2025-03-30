package com.contrue.web.servlet;

import com.contrue.entity.po.Like;
import com.contrue.entity.po.Store;
import com.contrue.entity.vo.Result;
import com.contrue.service.Impl.LikeServiceImpl;
import com.contrue.service.Impl.StoreServiceImpl;
import com.contrue.service.LikesService;
import com.contrue.service.StoreService;
import com.contrue.util.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author confff
 */
@WebServlet("/api/stores")
public class StoreServlet extends BaseServlet{
    @Override
    protected String getServletRegistration() {
        return "/api/stores/*";
    }

    private StoreService storeService = StoreServiceImpl.getInstance();
    private LikesService likesService = LikeServiceImpl.getInstance();

    //获取商铺详情
    public void getStores(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //测试
        System.out.println("执行了这个方法getStores");

        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("pageSize");
        String sortBy = request.getParameter("sortBy");

        int page = 1;
        int pageSize = 10;

        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }
        if (sizeStr != null) {
            pageSize = Integer.parseInt(sizeStr);
        }
        //根据likes排序
        if("likes".equals(sortBy)){
            Result result = Result.success(storeService.getStoresByLikes(page, pageSize).getResults());
            writeJson(response, result);
        }else{
            Result result = Result.success(storeService.getStoresByComments(page, pageSize).getResults());
            writeJson(response, result);
        }

    }
    //通过id获取某商铺详情
    public void getStoreId(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String storeId = (String) request.getAttribute("storeId");
        Store store = new Store();
        store.setId(Integer.parseInt(storeId));

        Store storeDetailById = storeService.getStoreDetailById(store);
        Result result = Result.success(storeDetailById);
        writeJson(response,result);
    }

    //点赞商铺
    public void postLike(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String storeIdStr = (String) request.getAttribute("storeId");
        String subjectIdStr = (String) request.getAttribute("subjectId");
        String subjectType = (String) request.getAttribute("subjectType");
        String subjectName = (String) request.getAttribute("subjectName");
        int subjectId = Integer.parseInt(subjectIdStr);
        int storeId = Integer.parseInt(storeIdStr);

        Like like = new Like();
        like.setId(storeId);
        like.setTargetType("store");
        like.setUserId(subjectId);
        like.setTargetId(storeId);
        like.setUserName(subjectName);

        if(likesService.likeStore(like)){
            writeJson(response,Result.success());
        }else{
            writeJson(response,Result.error("点赞失败"));
        }
    }

    //修改商铺信息
    public void postUpdate(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String storeIdStr = (String) request.getAttribute("storeId");

        Store store = JsonParser.parseJson(request, Store.class);
        store.setId(Integer.parseInt(storeIdStr));

        if(storeService.updateStore(store)){
            writeJson(response,Result.success());
        }else{
            writeJson(response,Result.error("修改商铺信息失败"));
        }
    }

    //取消点赞商铺
    public void deleteLike(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String storeIdStr = (String) request.getAttribute("storeId");

        Like like = new Like();
        like.setTargetId(Integer.parseInt(storeIdStr));
        like.setTargetType("store");
        if(likesService.unlikeStore(like)){
            writeJson(response,Result.success());
        }else{
            writeJson(response,Result.error("取消点赞失败"));
        }
    }

}
