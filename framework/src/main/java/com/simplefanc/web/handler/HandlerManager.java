package com.simplefanc.web.handler;

import com.simplefanc.web.mvc.Controller;
import com.simplefanc.web.mvc.RequestMapping;
import com.simplefanc.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理MappingHandler
 */
public class HandlerManager {
    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    //把带有controller注解的类挑选出来
    public static void resolveMappingHandler(List<Class<?>> classList) {
        for (Class<?> cls : classList) {
            if (cls.isAnnotationPresent(Controller.class)) {//存在controller注解
                parseHandlerFromController(cls);
            }
        }
    }

    private static void parseHandlerFromController(Class<?> cls) {
        Method[] methods = cls.getDeclaredMethods();//通过反射获取这个类所有的方法
        //遍历这些方法 找到被RequestMapping注解的方法
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {//没被RequestMapping注解的方法不处理
                continue;
            }

            //获取MappingHandler属性
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();

            List<String> paramNameList = new ArrayList<>();
            for (Parameter parameter : method.getParameters()) {
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);//转化为数组

            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params);//构造MappingHandler
            HandlerManager.mappingHandlerList.add(mappingHandler);//放到MappingHandler管理器静态属性
            //接下来 在dispatcherServlet里的service方法使用这些handler
        }
    }
}
