package com.lfcx.driver.util;

/**
 * Created by sj on 2015/11/16.
 */
public class StringEmptyUtil {

    public static  boolean isEmpty(String...strings){
        if (strings==null||strings.length==0  ){
            return  true;
        }
        for (String s:strings){

            if (s==null || s.trim().length()==0){
                return true;
            }
        }
        return  false;
    }
}
