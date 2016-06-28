package com.itheima.mobileplayer64.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.itheima.mobileplayer64.R;
import com.itheima.mobileplayer64.adapter.MainFragmentPagerAdapter;
import com.itheima.mobileplayer64.ui.fragment.AudioListFragment;
import com.itheima.mobileplayer64.ui.fragment.VideoListFragment;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class MainActivity extends BaseActivity {
	
	/** 当ViewPager发生变化的时候被回调 */
	private final class OnVideoPageChangeListener implements
			OnPageChangeListener {
		@Override
		/** 当有界面被选中时返回被选中的position */
		public void onPageSelected(int position) {
			// 更新tab
			updateTabs(position);
		}

		@Override
		/** 当有滑动时间发生时，返回当前第一个可见page的position,以及手指所在的位置*/
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			
//			logE("MainActivity.onPageScrolled.position="+position+";offsetPixels="+positionOffsetPixels+";Offset="+positionOffset);
			// 移动大小 = 起始位置 + 偏移位置
			// 起始位置 = position * 指示器宽度
			// 偏移位置 = positionOffsetPixels / 页数
//			int offsetX = positionOffsetPixels /mFragments.size();
			int offsetX = (int) (positionOffset * indicate_line.getWidth());
			int startX = position * indicate_line.getWidth();
			int translationX = startX + offsetX;
			
			// 根据移动大小修改指示器位置
			ViewHelper.setTranslationX(indicate_line, translationX);
		}

		@Override
		/** 当滑动状态发生变更时被回调 */
		public void onPageScrollStateChanged(int state) {
			
		}
	}

	private ViewPager viewPager;
	private List<Fragment> mFragments;
	private TextView tv_video;
	private TextView tv_audio;
	private View indicate_line;

	@Override
	/** 返回Activity使用的布局id */
	protected int getLayoutResId() {
		return R.layout.activity_main;
	}

	@Override
	/** 执行findViewById操作 */
	protected void initView() {
		viewPager = (ViewPager) findViewById(R.id.main_viewpager);
		tv_video = (TextView) findViewById(R.id.main_tv_video);
		tv_audio = (TextView) findViewById(R.id.main_tv_audio);
		indicate_line = findViewById(R.id.main_indicate_line);
	}

	@Override
	/** 执行注册监听器和适配器的操作 */
	protected void initListener() {
		mFragments = new ArrayList<Fragment>();
		viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), mFragments));
		viewPager.setOnPageChangeListener(new OnVideoPageChangeListener());
	}

	@Override
	/** 获取界面上要使用的数据，执行初始化操作 */
	protected void initData() {
		// 填充Fragment列表
		mFragments.add(new VideoListFragment());
		mFragments.add(new AudioListFragment());
		
		// 初始化标签栏大小
		updateTabs(0);
		
		// 初始化指示器宽度
		int screenW = getWindowManager().getDefaultDisplay().getWidth();
		indicate_line.getLayoutParams().width = screenW / mFragments.size();
		indicate_line.requestLayout();
	}

	@Override
	/** 处理在BaseActivity未处理的点击事件 */
	protected void processClick(View v) {
		
	}

	/** 更新所有的Tab */
	private void updateTabs(int position) {
		// 当视频被选中则放大，否则缩小
		updateTab(position, tv_video, 0);
		// 当视频被选中则放大，否则缩小
		updateTab(position, tv_audio, 1);
	}

	/** 更新指定位置的Tab */
	private void updateTab(int position, TextView currentTextView,
			int currentPosition) {
		// TODO ViewPropertyAnimator与ObjectAnimator区别
		if (position == currentPosition) {
			currentTextView.setTextColor(getResources().getColor(R.color.green));
			ViewPropertyAnimator.animate(currentTextView).scaleX(1.2f).setDuration(200);
			ViewPropertyAnimator.animate(currentTextView).scaleY(1.2f).setDuration(200);
		} else {
			currentTextView.setTextColor(getResources().getColor(R.color.halfwhite));
			ViewPropertyAnimator.animate(currentTextView).scaleX(1.0f).setDuration(200);
			ViewPropertyAnimator.animate(currentTextView).scaleY(1.0f).setDuration(200);
		}
	}
}
