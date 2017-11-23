package com.example.wenDaService.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    //服务器访问都是多线程访问的,如果直接这样写的话。会存在线程安全的问题。两条线程同时访问的话，会出错
//    private User user;
    //线程局部变量,每一条线程都有一个user变量，可以理解成<threadId,User>.
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
