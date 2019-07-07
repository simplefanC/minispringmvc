package com.simplefanc;

import com.simplefanc.starter.MiniApplication;

public class Application {
    public static void main(String[] args) {
        System.out.println("hello world");
        MiniApplication.run(Application.class, args);//调用框架类
    }
}
