package com.example.timer;

import android.app.Activity;
import android.os.Bundle;

/**
 * 项目名称 :MVPDemo
 * 类描述:
 * 创建人 : 001
 * 创建时间:2016/6/2 9:41
 * 修改时间:2016/6/2 9:41
 * 修改备注:
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.addActivity(this);
    }
}
