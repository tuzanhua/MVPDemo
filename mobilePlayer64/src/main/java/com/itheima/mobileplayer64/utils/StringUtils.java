package com.itheima.mobileplayer64.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

	/** 一小时使用的毫秒数 */
	private static final int HOUR = 60 * 60 * 1000;
	/** 一分钟使用的毫秒数 */
	private static final int MIN = 60 * 1000;
	/** 一秒钟使用的毫秒数 */
	private static final int SEC = 1000;
	
	/** 根据参数里的duration大小，返回一个格式化的时间字符串</br>
	 * 时间格式如：01:33:22 或 03:22 */
	public static String formartDuration(int duration) {
		String str = null;
		
		// 得到小时数
		int hour = duration / HOUR; 
		int remainTime = duration % HOUR;
		
		// 得到分钟数
		int min = remainTime / MIN;
		remainTime = remainTime % MIN;
		
		// 得到秒钟数
		int sec = remainTime / SEC;
		
		// 格式化为时间格式
		if (hour > 0) {
			str = String.format("%02d:%02d:%02d", hour, min, sec);
		} else {
			str = String.format("%02d:%02d", min, sec);
		}
		return str;
	}
	
	/** 格式化时间，显示为：01:22:33  */
	public static String fromatSystemTime() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(date);
	}
	
	/** 歌曲的displayName包含文件后缀，使用时需要将后缀去除 */
	public static String formatAudioDisplayName(String displayName) {
		// dida.mp3
		String[] arr = displayName.split("\\.");
		return arr[0];
	}
}
