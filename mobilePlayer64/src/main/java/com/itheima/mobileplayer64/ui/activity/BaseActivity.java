package com.itheima.mobileplayer64.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.itheima.mobileplayer64.R;
import com.itheima.mobileplayer64.utils.LogUtils;

/** 规范代码结构，提供公用方法 */
public abstract class BaseActivity extends FragmentActivity implements
		OnClickListener {

	/** 对当前Activity的引用 */
	protected Activity mActivity;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mActivity = this;
		
		setContentView(getLayoutResId());

		initView();
		registerCommonButton();
		initListener();
		initData();
	}
	
	/** 返回Activity使用的布局id */
	protected abstract int getLayoutResId();

	/** 执行findViewById操作 */
	protected abstract void initView();

	/** 执行注册监听器和适配器的操作 */
	protected abstract void initListener();

	/** 获取界面上要使用的数据，执行初始化操作 */
	protected abstract void initData();

	/** 处理在BaseActivity未处理的点击事件 */
	protected abstract void processClick(View v);

	@Override
	public void onClick(View v) {
		// 将在多数界面都存在的点击事件统一处理
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			// 处理在BaseActivity未处理的点击事件 
			processClick(v);
			break;
		}
	}

	/** 直接注册在多个界面都有的按钮的点击事件 */
	private void registerCommonButton() {
		// 返回按钮
		View view = findViewById(R.id.back);
		if (view != null) {
			view.setOnClickListener(this);
		}
	}

	/** 显示一个Toast */
	protected void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	/** 打印一个Error级的log */
	protected void logE(String msg) {
		LogUtils.e(this.getClass(), msg);
	}
}
