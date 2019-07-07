package com.simplefanc.starter;

import com.simplefanc.core.ClassScanner;
import com.simplefanc.web.handler.HandlerManager;
import com.simplefanc.web.server.TomcatServer;
import org.apache.catalina.LifecycleException;

import java.util.List;

/**
 * 框架入口类
 */
public class MiniApplication {
    //传参为1.应用的入口类 通过入口类可以定位到项目的根目录 获取到应用入口类的信息
    //2.应用入口类启动时的参数数组
    public static void run(Class<?> cls, String[] args){
        System.out.println("Hello MiniSpring");
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer();
            //获取所有的class
            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName());
            classList.forEach(it -> System.out.println(it.getName()));

            //调用HandlerManager初始化所有MappingHandler
            HandlerManager.resolveMappingHandler(classList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
