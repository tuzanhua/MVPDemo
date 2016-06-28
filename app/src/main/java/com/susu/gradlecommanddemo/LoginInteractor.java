package com.susu.gradlecommanddemo;

import android.content.Context;
import android.content.Intent;

import javax.xml.xpath.XPathFunction;

/**
 * 作者：suxianming on 2016/4/27 15:30
 * 跟登录有关联的接口 这个是Model层
 */

public interface LoginInteractor {
    interface  OnFinishLogin{
        void onUsernameError();
        void onPasswordError();
        void onSuccess();
    }
    void login(String username,String password,OnFinishLogin onFinishLogin);
}
