package com.example.timer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.timer.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目名称 :MVPDemo
 * 类描述:
 * 创建人 : 001
 * 创建时间:2016/6/2 9:33
 * 修改时间:2016/6/2 9:33
 * 修改备注:
 */
public class ThreeActivity extends BaseActivity {
    @BindView(R.id.btn_timer)
    Button btnTimer;
    @BindView(R.id.tv)
    TextView tv;

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.rbtn_1)
    RadioButton rbtn1;
    @BindView(R.id.rbrtn_2)
    RadioButton rbrt2;
    @BindView(R.id.rbrtn_3)
    RadioButton rbrt3;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.cancle)
    Button cancle;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    timerManger.notifyAllObserver();
                    if(MyApplication.i-- >0){
                        mHandler.sendEmptyMessageDelayed(1,1000);
                    }else{
                            mHandler.sendEmptyMessage(2);
                    }
                    tv.setText("还剩余 " + MyApplication.i / 60 + ":" + MyApplication.i % 60 + " s");
                    break;
                case 2:
//                    Process.killProcess(Process.myPid());
                    MyApplication.exitApp();
                    break;
            }
        }
    };
    private TimerManger timerManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        timerManger = TimerManger.getInstance();
        ButterKnife.bind(this);
        // 如何实现一打开 当前activity  如果已经选择计时了 那么显示计时的倒计时
        timerManger.registObserver(new TimerManger.ObserverTimer() {
            @Override
            public void updateUI() {
                tv.setText("还剩余 " + MyApplication.i / 60 + ":" + MyApplication.i % 60 + " s");
            }
        });
        Integer checked = SpUtil.get("checked", 1);
        switch (checked) {
            case 1:
                rbtn1.setChecked(true);
                break;
            case 2:
                rbrt2.setChecked(true);
                break;
            case 3:
                rbrt3.setChecked(true);
                break;
        }

        //添加radiogroup 的改变选择按钮
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_1:
                        MyApplication.i = 60;
                        break;
                    case R.id.rbrtn_2:
                        MyApplication.i = 120;
                        SpUtil.put("checked", 2);
                        break;
                    case R.id.rbrtn_3:
                        MyApplication.i = 180;
                        SpUtil.put("checked", 3);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.btn_timer, R.id.back, R.id.cancle})
    public void onClick(View v) {
        //开启定时关闭的功能   开启子线程进行定时关闭的功能
        switch (v.getId()) {
            case R.id.btn_timer:

                if(MyApplication.i <0){
                    mHandler.sendEmptyMessage(2);
                }else{
                    mHandler.sendEmptyMessage(1);
                }

//                for(;MyApplication.i >0;MyApplication.i--){
//                    if(MyApplication.i < 0){
////                        MyApplication.exitApp();
//                        mHandler.sendEmptyMessage(2);
//                    }
//                    mHandler.sendEmptyMessageDelayed(1,1000);
//                }
//
                new Thread() {
                    @Override
                    public void run() {

                        for (; MyApplication.i > 0; MyApplication.i--) {
                            mHandler.sendEmptyMessage(1);
                            if (MyApplication.i < 0) {
                                break;
                            }
                            //睡1s
                            SystemClock.sleep(1000);
                        }
                        //退出程序
                        MyApplication.i = 0;
                        if (MyApplication.i == 0) {
                            return;
                        }
                        mHandler.sendEmptyMessage(2);
                    }
                }.start();
                break;
            case R.id.back:
                ThreeActivity.this.finish();
                break;
            case R.id.cancle:
//                MyApplication.i = 0;
                mHandler.removeCallbacksAndMessages(null);
                break;
        }
    }
}
