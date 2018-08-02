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

    /**
     * 是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
