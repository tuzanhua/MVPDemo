package com.example.desiginmind.observer;

import android.util.Log;

/**
 * 项目名称 :MVPDemo
 * 类描述:
 * 创建人 : 001
 * 创建时间:2016/5/23 10:41
 * 修改时间:2016/5/23 10:41
 * 修改备注:
 */
public class ConcreateObserver1 implements Observer {
    @Override
    public void update() {
        // 接受到改变之后做出对应的逻辑处理
        Log.e("guanchazhe","我是第一个观察者,观察到改变之后做出的对应的操作");
    }
}
