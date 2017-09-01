package com.codi.laolaiqiao.util.text;

/**
 * Created by GarryKing on 2016/8/18.
 * E-mail:flyhzq@sina.com
 */
public class StringUtil {

    public static boolean isBlank(String str) {
        if (null == str) return true;
        if (str.length() == 0) return true;
        String tmp = str.trim();
        if (tmp.length() == 0) return true;
        return false;
    }

}
