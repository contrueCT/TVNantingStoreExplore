package com.contrue.web.servlet;

import com.contrue.service.Impl.StoreServiceImpl;
import com.contrue.service.StoreService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author confff
 */
@WebServlet("/api/stores/*")
public class StoreServlet extends BaseServlet{
    @Override
    protected String getServletRegistration() {
        return "/api/stores/*";
    }

    private StoreService storeService = StoreServiceImpl.getInstance();

    public void getStores(HttpServletRequest request, HttpServletResponse response) {
        
    }


}
