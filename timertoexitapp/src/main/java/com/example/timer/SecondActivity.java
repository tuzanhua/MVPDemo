package com.example.timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目名称 :MVPDemo
 * 类描述:
 * 创建人 : 001
 * 创建时间:2016/6/2 9:31
 * 修改时间:2016/6/2 9:31
 * 修改备注:
 */
public class SecondActivity extends BaseActivity {
    @BindView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn)
    public void onClick() {
        startActivity(new Intent(SecondActivity.this,ThreeActivity.class));
    }
}
