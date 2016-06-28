package com.itheima.mobileplayer64.ui.fragment;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.itheima.mobileplayer64.R;
import com.itheima.mobileplayer64.adapter.AudioListCursorAdapter;
import com.itheima.mobileplayer64.bean.AudioItem;
import com.itheima.mobileplayer64.db.MediaPlayerAsyncQueryHandler;
import com.itheima.mobileplayer64.ui.activity.AudioPlayerActivity;

public class AudioListFragment extends BaseFragment {

	/** 音乐列表的item点击事件监听器 */
	private final class OnAudioItemClickListener implements
			OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// 获取列表数据
			Cursor cursor = (Cursor) parent.getItemAtPosition(position);
			ArrayList<AudioItem> audioItems = AudioItem.instanceListFromCursor(cursor); 
			
			// 带着数据跳转到播放界面
			Intent intent = new Intent(getActivity(), AudioPlayerActivity.class);
			intent.putExtra("audioItems", audioItems);
			intent.putExtra("position", position);
			startActivity(intent);
		}
	}

	private ListView listView;
	private AudioListCursorAdapter mAdapter;

	@Override
	protected int getLayoutResId() {
		return R.layout.main_audio_list;
	}

	@Override
	protected void initView(View view) {
		listView = (ListView) view.findViewById(R.id.simple_listview);
	}

	@Override
	protected void initListener() {
		mAdapter = new AudioListCursorAdapter(getActivity(), null);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnAudioItemClickListener());
	}

	@Override
	protected void initData() {
		// 异步查询获取音乐数据
		ContentResolver resolver = getActivity().getContentResolver();
		MediaPlayerAsyncQueryHandler asyncQueryHandler = new MediaPlayerAsyncQueryHandler(resolver);
		asyncQueryHandler.startQuery(1, mAdapter, Media.EXTERNAL_CONTENT_URI,
				new String[] { Media._ID, Media.DISPLAY_NAME, Media.ARTIST,
						Media.DATA }, null, null, null);
		
	}

	@Override
	protected void processClick(View v) {
		
	}

}
