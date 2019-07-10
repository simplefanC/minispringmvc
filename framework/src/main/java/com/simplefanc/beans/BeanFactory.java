package com.simplefanc.beans;


import com.simplefanc.web.mvc.Controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean工厂:初始化并保存Bean
 */
public class BeanFactory {
    private static Map<Class<?>, Object> classToBean = new ConcurrentHashMap<>();//存储Bean类型到Bean实例的映射 后续拓展可能要并发处理,使用ConcurrentHashMap

    //通过Bean类型取到对应的Bean
    public static Object getBean(Class<?> cls) {
        return classToBean.get(cls);
    }

    //Bean初始化
    public static void initBean(List<Class<?>> classList) throws Exception {//传入扫描到的类定义
        List<Class<?>> toCreate = new ArrayList<>(classList);//classList后续可能要使用
        while (toCreate.size() != 0) {
            int remainSize = toCreate.size();
            for (int i = 0; i < toCreate.size(); i++) {
                if (finishCreate(toCreate.get(i))) {//如果完成创建
                    toCreate.remove(i);
                }
            }
            if (toCreate.size() == remainSize) {//陷入死循环 抛出异常
                throw new Exception("cycle dependency!");
            }
        }
    }

    private static boolean finishCreate(Class<?> cls) throws IllegalAccessException, InstantiationException {
        if (!cls.isAnnotationPresent(Bean.class) && !cls.isAnnotationPresent(Controller.class)) {//如果这个类不需要初始化为Bean
            return true;
        }
        Object bean = cls.newInstance();//创建对象
        for (Field field : cls.getDeclaredFields()) {//遍历类的属性,看有无需要解决的依赖
            if (field.isAnnotationPresent(AutoWired.class)) {//需要使用依赖注入解决这个依赖
                Class<?> fieldType = field.getType();
                Object reliantBean = BeanFactory.getBean(fieldType);//通过类型从Bean工厂获取Bean
                if (reliantBean == null) {
                    return false;
                }
                field.setAccessible(true);
                field.set(bean, reliantBean);
            }
        }
        classToBean.put(cls, bean);
        return true;
    }
}
