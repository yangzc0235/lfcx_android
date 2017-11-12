package com.lfcx.common.utils;

/**
 * author: drawthink
 * date  : 2017/10/21
 * des   :
 */

public class StringUtils {
    public static String generateCheckCode(){
        return String.valueOf((int)(Math.random()*9000)+1000);
    }
}
