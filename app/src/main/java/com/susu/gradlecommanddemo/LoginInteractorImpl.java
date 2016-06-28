package com.susu.gradlecommanddemo;


import android.os.Handler;
import android.text.TextUtils;

/**
 * 作者：suxianming on 2016/4/27 15:36
 */
public class LoginInteractorImpl implements LoginInteractor{
    @Override
    public void login(final String username, final String password, final OnFinishLogin listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(username)){
                    listener.onUsernameError();
                }
                if(TextUtils.isEmpty(password)){
                    listener.onPasswordError();
                }
                if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
                    listener.onSuccess();
                }
            }
        }, 2000);
    }
}
