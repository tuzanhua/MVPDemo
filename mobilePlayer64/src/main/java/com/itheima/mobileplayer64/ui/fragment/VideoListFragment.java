package com.itheima.mobileplayer64.ui.fragment;

import java.util.ArrayList;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore.Video.Media;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.itheima.mobileplayer64.R;
import com.itheima.mobileplayer64.adapter.VideoListCursorAdapter;
import com.itheima.mobileplayer64.bean.VideoItem;
import com.itheima.mobileplayer64.db.MediaPlayerAsyncQueryHandler;
import com.itheima.mobileplayer64.ui.activity.VideoPlayerActivity;

public class VideoListFragment extends BaseFragment {

	/** 当Listview的某一项被点击时回调此监听器的方法 */
	private final class OnVideoItemClickListener implements
			OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
//			gotoVideoPlayerWithItem(parent, position);
			gotoVideoPlayerWithList(parent, position);
			
		}

		/** 使用整个视频列表跳转到视频播放界面 */
		private void gotoVideoPlayerWithList(AdapterView<?> parent, int position) {
			// 获取被点击项的数据
			Cursor cursor = (Cursor) parent.getItemAtPosition(position);
			ArrayList<VideoItem> videoItems = VideoItem.instanceListFromCursor(cursor);

			// 将数据传递给播放界面
			Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
//			Intent intent = new Intent(getActivity(), VitamioPlayerActivity.class);
			intent.putExtra("videoItems", videoItems); // List对象不能放入intent，只能使用ArrayList传递列表数据
			intent.putExtra("position", position);
			startActivity(intent);
		}

		/** 使用单个item跳转到视频播放界面 */
		private void gotoVideoPlayerWithItem(AdapterView<?> parent, int position) {
			// 获取被点击项的数据
			Cursor cursor = (Cursor) parent.getItemAtPosition(position);
			VideoItem videoItem = VideoItem.instanceFromCursor(cursor);
			
			// 将数据传递给播放界面
			Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
			intent.putExtra("videoItem", videoItem);
			startActivity(intent);
		}
	}

	private ListView listView;
	private VideoListCursorAdapter mAdapter;

	@Override
	protected int getLayoutResId() {
		return R.layout.main_video_list;
	}

	@Override
	protected void initView(View view) {
		listView = (ListView) view.findViewById(R.id.simple_listview);
	}

	@Override
	protected void initListener() {
		mAdapter = new VideoListCursorAdapter(getActivity(), null);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnVideoItemClickListener());
	}

	@Override
	protected void initData() {
		ContentResolver resolver = getActivity().getContentResolver();
		// 主线程查询
//		// 获取视频数据
//		Cursor cursor = resolver.query(Media.EXTERNAL_CONTENT_URI, new String[] { Media._ID, Media.TITLE,
//				Media.DURATION, Media.SIZE, Media.DATA }, null, null, null);
////		CursorUtils.printCursor(cursor);
//		
//		// 填充视频列表
//		mAdapter.swapCursor(cursor);
		
		// 子线程查询
		AsyncQueryHandler asyncQueryHandler = new MediaPlayerAsyncQueryHandler(resolver);
		asyncQueryHandler.startQuery(0, mAdapter, Media.EXTERNAL_CONTENT_URI, new String[] { Media._ID, Media.TITLE,
				Media.DURATION, Media.SIZE, Media.DATA }, null, null, null);
	}

	@Override
	protected void processClick(View v) {
		
	}

}
