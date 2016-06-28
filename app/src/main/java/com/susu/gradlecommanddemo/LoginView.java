package com.susu.gradlecommanddemo;

/**
 * 作者：suxianming on 2016/4/27 14:10
 */
public interface LoginView {
    void showProgress();
    void hideProgress();
    void setUsernameError();
    void setPwdError();
    void navigateToHome();
}
