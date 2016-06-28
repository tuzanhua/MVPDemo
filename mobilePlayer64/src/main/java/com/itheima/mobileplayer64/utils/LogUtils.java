package com.itheima.mobileplayer64.utils;

import android.util.Log;

public class LogUtils {
	/** log控制开关 */
	private static final boolean ENABLE = true;

	/** 打印一个Debug等级的log */
	public static void d(String tag, String msg) {
		if (ENABLE) {
			Log.d(tag, msg);
		}
	}

	/** 打印一个Error等级的log */
	public static void e(String tag, String msg) {
		if (ENABLE) {
			Log.e(tag, msg);
		}
	}

	/** 打印一个Error等级的log,tag为 itcast_类名 */
	public static void e(Class<?> cls, String msg) {
		if (ENABLE) {
			Log.e("itcast_" + cls.getSimpleName(), msg);
		}
	}
}
