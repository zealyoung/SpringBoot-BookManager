package com.zealyoung.bookmanager.utils;

import com.zealyoung.bookmanager.model.User;

/**
 * @Author:Zealyoung
 */
public class ConcurrentUtils {

    private static ThreadLocal<User> host = new ThreadLocal<>();

    public static User getHost(){
        return host.get();
    }

    public static void setHost(User user){
        host.set(user);
    }
}
