package com.itheima.mobileplayer64.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.itheima.mobileplayer64.R;

/**
 * Splash界面的作用 </br>
 * 1,用于初始化应用，比如从配置文件或数据库加载初始化数据 </br>
 * 2,界面分发，比如已经有登陆的账号则跳转到信息列表，没有账号则跳转到登陆界面
 */
public class SplashActivity extends BaseActivity {

	@Override
	protected int getLayoutResId() {
		return R.layout.splash;
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initListener() {

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void processClick(View v) {

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// 延迟一段时间后跳转到主界面
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// 跳转到主界面
				Intent intent = new Intent(mActivity, MainActivity.class);
				startActivity(intent);
				
				// 跳转后关闭当前界面
				finish();
			}
		}, 2000);
	}

}
