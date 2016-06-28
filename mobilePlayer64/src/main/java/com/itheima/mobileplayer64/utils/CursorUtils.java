package com.itheima.mobileplayer64.utils;

import android.database.Cursor;

public class CursorUtils {

	private static final String TAG = "itcast_CursorUtils";
	
	/** 打印cursor里的所有内容 */
	public static void printCursor(Cursor cursor) {
		if (cursor==null) {
			return;
		}
		// 打印查询到的结果
		
		LogUtils.e(TAG, "CursorUtils.printCursor.查询到的数据量为："+cursor.getCount());
		LogUtils.e(TAG, "==========================================");
		
		cursor.moveToPosition(-1);// 避免cursor已经被移动过位置
		// 行遍历
		while (cursor.moveToNext()) {
			// 列遍历
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				LogUtils.e(TAG, "CursorUtils.printCursor.name="+cursor.getColumnName(i)+";value="+cursor.getString(i));
			}
			LogUtils.e(TAG, "==========================================");
		}
	}
}
