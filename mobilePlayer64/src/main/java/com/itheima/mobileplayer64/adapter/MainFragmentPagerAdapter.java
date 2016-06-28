package com.itheima.mobileplayer64.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/** 填充主界面ViewPager使用的适配器 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments;

	public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		mFragments = fragments;
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

}
