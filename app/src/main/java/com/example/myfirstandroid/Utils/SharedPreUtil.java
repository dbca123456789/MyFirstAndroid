package com.example.myfirstandroid.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myfirstandroid.Activity.LoginActivity;

public class SharedPreUtil {

    private static final String FILE_NAME = "user_login";

    public static final String LOGIN_DATA = "loginData";
    public static final String IS_LOGIN = "isLogin";



    /**
     *
     * @param context
     * @param key
     * @param defaltObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaltObject){

        String type = defaltObject.getClass().getSimpleName();

        // MODE_PRIVATE 指定同样文件名时，所写内容会覆盖原文件的内容
        SharedPreferences pre = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        switch (type){
            case "Integer":
                return pre.getInt(key, (Integer) defaltObject);
            case "String":
                return pre.getString(key, (String) defaltObject);
            case "Boolean":
                return pre.getBoolean(key, (Boolean) defaltObject);
            case "Float":
                return pre.getFloat(key, (Float) defaltObject);
            case "Long":
                return pre.getLong(key, (Long) defaltObject);
            default:
                return null;
        }

    }

    public static void setParam(Context context, String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.apply();
    }

    public static void removeParam(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.clear();
    }
}
