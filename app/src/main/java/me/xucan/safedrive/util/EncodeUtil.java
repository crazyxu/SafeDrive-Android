package me.xucan.safedrive.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by xucan on 2016/4/28.
 */
public class EncodeUtil {
    public static String toUtf8(String str){
        String newStr = "";
        try {
            newStr = new String(str.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newStr;
    }
}
