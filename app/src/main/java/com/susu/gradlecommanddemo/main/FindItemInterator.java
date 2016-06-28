package com.susu.gradlecommanddemo.main;

import java.util.List;

/**
 * 作者：suxianming on 2016/5/9 14:08
 */
public interface  FindItemInterator {
    interface onFinishedItemListener{
        void onFinished(List<String> items);
    }

    void findItems(onFinishedItemListener onFinishedItemListener);
}
