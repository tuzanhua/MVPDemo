package com.itheima.mobileplayer64.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.provider.MediaStore.Video.Media;

public class VideoItem implements Serializable {

	private String title, path;
	private int duration, size;

	/** 从cursor解析出一个VideoItem */
	public static VideoItem instanceFromCursor(Cursor cursor) {
		VideoItem videoItem = new VideoItem();
		videoItem.title = cursor.getString(cursor.getColumnIndex(Media.TITLE));
		videoItem.path = cursor.getString(cursor.getColumnIndex(Media.DATA));
		videoItem.duration = cursor.getInt(cursor
				.getColumnIndex(Media.DURATION));
		videoItem.size = cursor.getInt(cursor.getColumnIndex(Media.SIZE));

		return videoItem;
	}

	/** 从cursor里解析出整个播放列表的数据 */
	public static ArrayList<VideoItem> instanceListFromCursor(Cursor cursor) {
		ArrayList<VideoItem> videoItems = new ArrayList<VideoItem>();
		if (cursor == null) {
			return videoItems;
		}
		
		// 为了保证能解析出整个列表，先将cursor的位置移动到列表的最前面
		cursor.moveToPosition(-1);
		
		// 循环整个cursor，解析所有数据
		while (cursor.moveToNext()) {
			VideoItem item = instanceFromCursor(cursor);
			videoItems.add(item);
		}
		
		return videoItems;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "VideoItem [title=" + title + ", path=" + path + ", duration="
				+ duration + ", size=" + size + "]";
	}

}
