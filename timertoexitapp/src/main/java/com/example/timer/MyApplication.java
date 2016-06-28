package com.example.timer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

/**
 * 项目名称 :MVPDemo
 * 类描述:
 * 创建人 : 001
 * 创建时间:2016/6/2 9:35
 * 修改时间:2016/6/2 9:35
 * 修改备注:
 */
public class MyApplication  extends Application {

    public static int i = 0;

    private static Context mContext;
    //创建存放所有activity的集合
    private static List<Activity> mActivityList = new LinkedList<Activity>();
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

    }

    public static  Context getContext(){
        return mContext;
    }
    //创建添加activity 的方法
    public static void addActivity(Activity activity) {
        mActivityList.add(activity);
    }


    //创建移除所有activity 退出的方法
    public static void exitApp() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
    }

    //移除单个activity的方法
    public static void removeActivity(Activity activity) {
        if (mActivityList.contains(activity))
            mActivityList.remove(activity);
    }
}
