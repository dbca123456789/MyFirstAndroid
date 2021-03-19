package com.example.myfirstandroid.Utils;

import android.content.Context;
import android.content.SharedPreferences;

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
}
