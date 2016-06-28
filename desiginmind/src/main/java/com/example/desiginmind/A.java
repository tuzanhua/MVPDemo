package com.example.desiginmind;


/**
 * 项目名称 :MVPDemo
 * 类描述:
 * 创建人 : 001
 * 创建时间:2016/5/23 10:12
 * 修改时间:2016/5/23 10:12
 * 修改备注:
 */
public class A {
  private CallBack  callback;
    // 注册一个事件
    public  void register(CallBack callback){
        this.callback = callback;
    }
    //需要调用的时候回调
    public void call(){
        callback.onCall();
    }
}
