package com.itheima.mobileplayer64.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.itheima.mobileplayer64.utils.StringUtils;

import android.database.Cursor;
import android.provider.MediaStore.Video.Media;

public class AudioItem implements Serializable{

	private String title, arties, path;

	/** 从cursor解析出一个AudioItem */
	public static AudioItem instanceFromCursor(Cursor cursor) {
		AudioItem videoItem = new AudioItem();
		videoItem.title = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
		// 去除title里的文件名
		videoItem.title = StringUtils.formatAudioDisplayName(videoItem.title);
		
		videoItem.arties = cursor.getString(cursor.getColumnIndex(Media.ARTIST));
		videoItem.path = cursor.getString(cursor.getColumnIndex(Media.DATA));

		return videoItem;
	}

	/** 根据参数里的Cursor解析出整个的播放列表 */
	public static ArrayList<AudioItem> instanceListFromCursor(Cursor cursor) {
		ArrayList<AudioItem> audioItems = new ArrayList<AudioItem>();
		if (cursor == null) {
			return audioItems;
		}
		
		cursor.moveToPosition(-1);
		while (cursor.moveToNext()) {
			AudioItem audioItem = instanceFromCursor(cursor);
			audioItems.add(audioItem);
		}
		
		return audioItems;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArties() {
		return arties;
	}

	public void setArties(String arties) {
		this.arties = arties;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
