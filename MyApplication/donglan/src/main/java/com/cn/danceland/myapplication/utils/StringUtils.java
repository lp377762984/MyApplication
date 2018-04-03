package com.cn.danceland.myapplication.utils;

/**
 * Created by feng on 2018/4/3.
 */

public class StringUtils {

    public static boolean isNullorEmpty(String string){
        if(string!=null){
            if("".equals(string)){
                return true;
            }
        }else {
            return true;
        }
        return false;
    }
}
