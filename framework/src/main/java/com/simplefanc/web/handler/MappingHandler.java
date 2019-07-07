package com.simplefanc.web.handler;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 请求映射器
 */
public class MappingHandler {
    private String uri;//请求uri
    private Method method;//对应的controller方法
    private Class<?> controller;//对应的类
    private String[] args;//调用方法所需要的参数

    //处理成功返回true
    public boolean handle(ServletRequest req, ServletResponse res) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String requestUri = ((HttpServletRequest) req).getRequestURI();
        if (!uri.equals(requestUri)) {//判断uri和请求的uri是否相等
            return false;
        }

        //准备参数
        Object[] parameters = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            parameters[i] = req.getParameter(args[i]);//通过参数名获取ServletRequest里的参数
        }

        Object ctl = controller.newInstance();//实例化controller
//        Object ctl = BeanFactory.getBean(controller);
        Object response = method.invoke(ctl, parameters);
        res.getWriter().println(response.toString());//将方法返回的结果放到ServletResponse里去
        return true;
    }


    MappingHandler(String uri, Method method, Class<?> cls, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = cls;
        this.args = args;
    }
}
