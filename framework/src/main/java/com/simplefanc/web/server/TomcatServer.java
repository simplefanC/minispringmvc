package com.simplefanc.web.server;

import com.simplefanc.web.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

public class TomcatServer {
    private Tomcat tomcat;
    private String[] args;

    public TomcatServer(String[] args) {
        this.args = args;
    }

    public void startServer() throws LifecycleException {
        tomcat = new Tomcat();
        tomcat.setPort(6699);
        tomcat.start();

        Context context = new StandardContext();//对Context的标准实现
        context.setPath("");//为Context设置路径
        context.addLifecycleListener(new Tomcat.FixContextListener());//生命周期监听器

        //http://localhost:6699/test.json
        //建立tomcat和servlet的联系

        //实例化并加入容器
        DispatcherServlet servlet = new DispatcherServlet();
        Tomcat.addServlet(context, "dispatcherServlet", servlet).setAsyncSupported(true);//设置支持异步
        //添加servlet到uri的映射
        context.addServletMappingDecoded("/", "dispatcherServlet");
        //context容器需要依附在host容器,将其注册到默认的host容器
        tomcat.getHost().addChild(context);

        Thread awaitThread = new Thread("tomcat_await_thread"){
            @Override
            public void run() {
                TomcatServer.this.tomcat.getServer().await();//tomcatserver一直在等待
            }
        };
        awaitThread.setDaemon(false);//非守护线程
        awaitThread.start();
    }

}
