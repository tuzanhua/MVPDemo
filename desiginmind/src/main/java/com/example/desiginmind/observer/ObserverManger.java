package com.example.desiginmind.observer;

import java.util.ArrayList;

/**
 * 项目名称 :MVPDemo
 * 类描述:  观察者模式的管理类
 * 创建人 : 001
 * 创建时间:2016/5/23 14:08
 * 修改时间:2016/5/23 14:08
 * 修改备注:
 */
public class ObserverManger  {

    //观察者模式的观察者存储 集合
    private ArrayList<Observer> mLists = new ArrayList<>();

    //单例
    private ObserverManger(){}

   static  ObserverManger observerManger = new ObserverManger();

    public static ObserverManger getInstance(){
        return  observerManger;
    }

    // 注册监听
    public void registerListener(Observer observer){
        mLists.add(observer);
    }

    // fanzhuce
    public  void unRegisterListener(Observer observer){
        mLists.remove(observer);
    }


    // 通知所有的监听器状态发生变化
    public void notifyAllListener(){
        for (Observer observer : mLists){
            observer.update();
        }
    }
}
