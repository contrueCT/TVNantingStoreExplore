package com.contrue.webapp.web.servlet;

import com.contrue.Framework.annotation.Autowired;
import com.contrue.webapp.entity.dto.StoreSelect;
import com.contrue.webapp.entity.po.Like;
import com.contrue.webapp.entity.po.Store;
import com.contrue.webapp.entity.po.User;
import com.contrue.webapp.entity.vo.Result;
import com.contrue.webapp.entity.vo.StoreDetailVO;
import com.contrue.webapp.service.Impl.LikeServiceImpl;
import com.contrue.webapp.service.Impl.StoreServiceImpl;
import com.contrue.webapp.service.LikesService;
import com.contrue.webapp.service.StoreService;
import com.contrue.util.JsonParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author confff
 */
@WebServlet("/api/stores/*")
public class StoreServlet extends BaseServlet{
    @Override
    protected String getServletRegistration() {
        return "/api/stores/{storeId}/*";
    }

    @Autowired
    private StoreService storeService;

    @Autowired
    private LikesService likesService;

    //获取商铺详情
    public void getStores(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //测试
        System.out.println("执行了这个方法getStores");

        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("pageSize");
        String sortBy = request.getParameter("sort");

        if(pageStr == null&&sizeStr == null&&sortBy == null){
            Result success = Result.success(storeService.getStoreListCount());
            writeJson(response, success);
            return;
        }

        if(sortBy == null){
            sortBy = "comments";
        }

        int page = 1;
        int pageSize = 6;

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
    public void getStoresId(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String userId = (String) request.getAttribute("subjectId");
        String storeId = (String) request.getAttribute("storeId");
        Store store = new Store();
        store.setId(Integer.parseInt(storeId));

        //测试
        System.out.println("获取商铺详情方法被执行");

        User user = new User();
        user.setId(Integer.parseInt(userId));
        StoreSelect select = new StoreSelect(store, user);
        System.out.println(select);
        StoreDetailVO storeDetailById = storeService.getStoreDetailById(select);
        Result result = Result.success(storeDetailById);
        writeJson(response,result);
    }

    //点赞商铺
    public void postLike(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

        String subjectIdStr = (String) request.getAttribute("subjectId");

        String subjectName = (String) request.getAttribute("subjectName");

        int subjectId = Integer.parseInt(subjectIdStr);

        Like like = JsonParser.parseJson(request, Like.class);

        like.setUserId(subjectId);
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
        String userId = (String) request.getAttribute("subjectId");

        Like like = new Like();
        like.setTargetId(Integer.parseInt(storeIdStr));
        like.setTargetType("store");
        like.setUserId(Integer.parseInt(userId));

        //测试
        System.out.println("执行了deleteLike方法");

        if(likesService.unlikeStore(like)){
            //测试
            System.out.println("成功调用了deleteLike方法");

            writeJson(response,Result.success());
        }else{
            writeJson(response,Result.error("取消点赞失败"));
        }
    }

}
