package com.itheima.mobileplayer64.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import com.itheima.mobileplayer64.bean.Lyric;
import com.itheima.mobileplayer64.utils.LogUtils;

public class LyricsParser {
	private static final String TAG = "itcast_LyricsParser";

	/** 从文件里解析出歌词列表 */
	public static ArrayList<Lyric> parserFromFile(File lyricFile) {
		ArrayList<Lyric> lyrics = new ArrayList<Lyric>();
		// 检测数据可用性
		if (lyricFile == null|| !lyricFile.exists()) {
			lyrics.add(new Lyric(0, "没有找到歌词文件"));
			return lyrics;
		}
		
		// 逐行读取歌词并解析
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(lyricFile), "gbk"));
			String line = reader.readLine();
			while (line !=null) {
				// 解析一行歌词，有可能会有多个歌词对象
				ArrayList<Lyric> sameLyrics = parserLine(line);
				// 把解析出来的数据，放入列表
				lyrics.addAll(sameLyrics);
				
				// 获取下一行歌词
				line = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 把歌词按起始时间排序
		Collections.sort(lyrics);
		
		return lyrics;
	}
	
	/** 解析一行歌词，获取歌词对象 */
	private static ArrayList<Lyric> parserLine(String line) {
//		LogUtils.e(TAG, "LyricsParser.parserLine.line="+line);
		ArrayList<Lyric> sameLyrics = new ArrayList<Lyric>();
		
		// [00:10.26][00:20.26][00:40.26]演唱：汪峰
		String[] arr = line.split("\\]");
		// [00:10.26 [00:20.26 [00:40.26 演唱：汪峰
		String content = arr[arr.length -1]; 
		
		// 循环每一个时间，解析出该行歌词包含的所有时间点
		for (int i = 0; i < arr.length - 1; i++) {
			Lyric lyric = parserTime(arr[i], content);
			sameLyrics.add(lyric);
		}
		
		return sameLyrics;
	}

	/** 解析时间字符串，并使用content生成一个歌词对象 */
	private static Lyric parserTime(String timeStr, String content) {
		
//		LogUtils.e(TAG, "LyricsParser.parserTime.timeStr="+timeStr);
		// [00:10.26
		String[] arr  = timeStr.split(":");
		// [00 10.26
		String MIN = arr[0].substring(1);
		
		// 10.26
		arr = arr[1].split("\\.");
		
		// 10 26
		String SEC = arr[0];
		String MSEC = arr[1];
		
		// 将字符串转换成数值
		int min = Integer.parseInt(MIN); 
		int sec = Integer.parseInt(SEC);
		int msec = Integer.parseInt(MSEC);
		
		// 计算startPoint
		int startPoint = min * 60 * 1000
				+ sec * 1000
				+ msec * 10;
		return new Lyric(startPoint, content);
	}
}
