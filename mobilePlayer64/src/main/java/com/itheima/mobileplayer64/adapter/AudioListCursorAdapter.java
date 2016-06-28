package com.itheima.mobileplayer64.adapter;

import com.itheima.mobileplayer64.R;
import com.itheima.mobileplayer64.bean.AudioItem;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AudioListCursorAdapter extends CursorAdapter {

	public AudioListCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	public AudioListCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	public AudioListCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// 创建view
		View view = View.inflate(context, R.layout.main_audio_item, null);
		view.setTag(new ViewHolder(view));
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// 填充布局
		ViewHolder holder = (ViewHolder) view.getTag();
		AudioItem audioItem = AudioItem.instanceFromCursor(cursor);
		holder.tv_title.setText(audioItem.getTitle());
		holder.tv_arties.setText(audioItem.getArties());
	}

	private class ViewHolder {
		TextView tv_title, tv_arties;

		/** 在构造方法里初始化布局view */
		public ViewHolder(View view) {
			tv_title = (TextView) view.findViewById(R.id.main_audio_item_tv_title);
			tv_arties = (TextView) view.findViewById(R.id.main_audio_item_tv_arties);
		}
	}
	
}
