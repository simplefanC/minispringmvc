package com.simplefanc.core;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描器
 */
public class ClassScanner {
    public static List<Class<?>> scanClasses(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();
        //包名转化为文件路径
        String path = packageName.replace(".", "/");
        //通过类加载器获取资源
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);
        //遍历资源
        while(resources.hasMoreElements()){
            URL resource = resources.nextElement();
            if(resource.getProtocol().contains("jar")){//判断资源类型 如果为jar包
                JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                //jar包路径名
                String jarFilePath = jarURLConnection.getJarFile().getName();
                //通过jar包路径获取jar包下所有的类
                classList.addAll(getClassesFromJar(jarFilePath, path));
            }else {
                //todo 其他资源类型
            }
        }
        return classList;
    }

    private static List<Class<?>> getClassesFromJar(String jarFilePath, String path) throws IOException, ClassNotFoundException {//可以通过类的相对路径指定想要的类
        List<Class<?>> classes = new ArrayList<>();
        //将jar包路径转化为JarFile实例
        JarFile jarFile = new JarFile(jarFilePath);
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()){
            //每个JarEntry都是一个jar包里的文件
            JarEntry jarEntry = jarEntries.nextElement();
            String entryName = jarEntry.getName();//例如com/simplefanc/Application.class
            if(entryName.startsWith(path) && entryName.endsWith(".class")){
                String classFullName = entryName.replace("/", ".").substring(0, entryName.length() - 6);//获取到类全限定名
                //通过类加载器将其加载到jvm
                classes.add(Class.forName(classFullName));
            }
        }
        return classes;
    }
}
