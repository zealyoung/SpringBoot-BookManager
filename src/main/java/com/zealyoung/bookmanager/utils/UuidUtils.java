package com.zealyoung.bookmanager.utils;

import java.util.UUID;

/**
 * @Author:Zealyoung
 */
public class UuidUtils {
    public static String next(){
        return UUID.randomUUID().toString().replace("-","a");
    }
}
