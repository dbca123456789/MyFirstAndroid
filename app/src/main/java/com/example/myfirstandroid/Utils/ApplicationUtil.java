package com.example.myfirstandroid.Utils;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Application维护所有activity
 * 目的:退出功能的实现
 */

public class ApplicationUtil extends Application {
    private List<Activity>  mList = new LinkedList<>();

    private static class SingletonHolder{
        public static ApplicationUtil instance = new ApplicationUtil();
    }


    public ApplicationUtil(){

    }


    /**
     * 单例模式 线程安全 双重校验锁模式、静态内部类
     * @return
     */
    public static ApplicationUtil getInstance(){
        return SingletonHolder.instance;
    }


    public void addActivity(Activity activity){
        mList.add(activity);
    }

    public void exit(){
        try {
            for (Activity activity: mList){
                if (activity!=null){
                    activity.finish();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.exit(0);
        }
    }

}
