package com.susu.gradlecommanddemo.main;

import java.util.List;

/**
 * 作者：suxianming on 2016/4/28 10:51
 */
public interface SuccessView {
    void showProgress();
    void hideProgress();
    void setItems(List<String> list);
    void showMessage(String message);
}
