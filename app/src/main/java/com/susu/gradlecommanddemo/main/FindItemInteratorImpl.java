package com.susu.gradlecommanddemo.main;

import android.os.Handler;

import java.util.Arrays;
import java.util.List;

/**
 * 作者：suxianming on 2016/5/9 14:10
 * 这个是Model层
 */
public class FindItemInteratorImpl implements FindItemInterator {
    @Override
    public void findItems(final onFinishedItemListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onFinished(createArray());
            }
        },2000);
    }

    private List<String> createArray() {
        return Arrays.asList(
                "item1",
                "item2",
                "item3",
                "item4",
                "item5",
                "item6",
                "item7",
                "item8"
        );
    }
}
