package com.example.desiginmind.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称 :MVPDemo
 * 类描述:
 * 创建人 : 001
 * 创建时间:2016/5/23 10:47
 * 修改时间:2016/5/23 10:47
 * 修改备注:
 */
public class Subject {
    //保存 注册事件的集合
    List<Observer> lists = new ArrayList<>();
    //注册一个事件
    public void register(Observer observer){
        lists.add(observer);
    }

    //通知注册每一个监听的位置改变状态
    public void notifyUpdate(){

        for(Observer ob : lists){
            ob.update();
        }
    }

    //取消注册的监听  将监听移除对应的集合
    public void unRegister(Observer observer){
        lists.remove(observer);
    }




}
