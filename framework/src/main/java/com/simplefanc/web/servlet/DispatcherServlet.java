package com.simplefanc.web.servlet;

import com.simplefanc.web.handler.HandlerManager;
import com.simplefanc.web.handler.MappingHandler;

import javax.servlet.*;
import java.io.IOException;

public class DispatcherServlet implements Servlet {
    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        //当一个请求过来时依次判断这些handler能不能处理这个请求,如果能处理就响应结果
        for (MappingHandler mappingHandler : HandlerManager.mappingHandlerList){
            try {
                if(mappingHandler.handle(req, res)){//处理成功返回true
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
