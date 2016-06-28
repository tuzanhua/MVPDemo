package com.example.timer;
import java.util.ArrayList;

/**
 * 项目名称 :MVPDemo
 * 类描述:  定时器的观察者管理类
 * 创建人 : 001
 * 创建时间:2016/6/2 11:41
 * 修改时间:2016/6/2 11:41
 * 修改备注:
 */
public class TimerManger {
    //单例模式
    private TimerManger() {
    }

    private static TimerManger timerManger = new TimerManger();

    public static TimerManger getInstance() {
        return timerManger;
    }


    //创建存储观察者的集合
    private ArrayList<ObserverTimer> mLists = new ArrayList();

    //添加
    public void registObserver(ObserverTimer observerTimer) {
        if (!mLists.contains(observerTimer)) {
            mLists.add(observerTimer);
        }
    }

    //反注册
    public void unRegistObserver(ObserverTimer observerTimer) {
        mLists.remove(observerTimer);
    }

    //通知所有的观察者更新
    public void notifyAllObserver() {
        for (ObserverTimer observerTimer : mLists) {
            observerTimer.updateUI();
        }
    }

    interface ObserverTimer {
        //数据变化时候需要做的操作
        void updateUI();
    }
}
