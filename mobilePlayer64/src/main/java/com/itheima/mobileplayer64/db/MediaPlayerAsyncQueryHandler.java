package com.itheima.mobileplayer64.db;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

public class MediaPlayerAsyncQueryHandler extends AsyncQueryHandler {

	public MediaPlayerAsyncQueryHandler(ContentResolver cr) {
		super(cr);
	}

	@Override
	/** 当查询结束时回调 */
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		if (cookie != null && cookie instanceof CursorAdapter) {
			//更新CursorAdapter使用的数据
			((CursorAdapter) cookie).swapCursor(cursor);
		}
	}
}
