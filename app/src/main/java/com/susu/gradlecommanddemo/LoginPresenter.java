package com.susu.gradlecommanddemo;

/**
 * 作者：suxianming on 2016/4/27 15:10
 */
public interface LoginPresenter {
    //验证用户信息的
    void validateUser(String username,String password);

    //回收对象的
    void onDestroy();

}
