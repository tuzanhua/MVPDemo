package com.example.desiginmind.observer;

import android.util.Log;

/**
 * 项目名称 :MVPDemo
 * 类描述:
 * 创建人 : 001
 * 创建时间:2016/5/23 10:44
 * 修改时间:2016/5/23 10:44
 * 修改备注:
 */
public class ConcreateObserver2 implements Observer {
    @Override
    public void update() {
        // 第二个观察者 当有改变的时候做出对应的更新
        Log.e("guanchazhe","我是第二个观察者,观察到改变之后做出的对应的操作");
    }
}
