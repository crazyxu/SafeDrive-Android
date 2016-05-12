package me.xucan.safedrive.db;

import android.content.Context;
import android.content.SharedPreferences;

import me.xucan.safedrive.App;
import me.xucan.safedrive.bean.User;

/**
 * Created on 2016/5/9.
 */
public class MySp {
    private final static String SP_NAME_USER = "user";
    private static SharedPreferences sp;
    static {
        sp = App.getInstance().getContext().getSharedPreferences(SP_NAME_USER, Context.MODE_PRIVATE);
    }

    /**
     * 保存用户
     * @param user
     */
    public static void saveUser(User user){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userName", user.getUserName());
        editor.putInt("userId", user.getUserId());
        editor.putString("phone", user.getPhone());
        editor.putString("token", user.getToken());
        editor.putString("portraitUrl", user.getPortraitUrl());
        editor.apply();
    }

    /**
     * 获取用户
     * @return
     */
    public static User getUser(){
        User user = new User();
        user.setUserName(sp.getString("userName", ""));
        user.setPhone(sp.getString("phone", ""));
        user.setPortraitUrl(sp.getString("portraitUrl", ""));
        user.setToken(sp.getString("token", ""));
        user.setUserId(sp.getInt("userId", 0));
        return user;
    }

    /**
     * 清除全部数据
     */
    public static void clear(){
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

}
