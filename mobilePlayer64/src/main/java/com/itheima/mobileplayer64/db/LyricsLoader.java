package com.itheima.mobileplayer64.db;

import java.io.File;

import android.os.Environment;

public class LyricsLoader {
	
	private static File root = new File(Environment.getExternalStorageDirectory(),"test/audio/"); 
	
	/** 从歌曲名查找歌词文件 */
	public static File loadLyricFile(String displayName) {
		// 根据歌曲名从本地查找.lrc文件
		File lyricFile = fileExist(displayName,".lrc");
		if (lyricFile != null) {
			return lyricFile;
		}
		
		// 没找到lrc文件，尝试查找txt文件
		lyricFile = fileExist(displayName,".txt");
		if (lyricFile != null) {
			return lyricFile;
		}
		
		// 尝试其他后缀的歌词文件
		//....
		//....
		
		// 所有后缀的歌词文件都尝试完后，仍没有找到则尝试从服务器下载歌词
		//....
		//....
		
		// 下载完成之后，将生成的歌词文件返回
		lyricFile = fileExist(displayName,".lrc");
		if (lyricFile != null) {
			return lyricFile;
		}
		
		// 所有情况都没能获取到歌词文件，返回null
		return null;
	}

	/** 检测指定歌曲名和后缀的歌词文件是否存在，存在则返回该文件，否则返回null */
	private static File fileExist(String displayName, String lastName) {
		File lyricFile =  new File(root,displayName+lastName);
		if (lyricFile !=null && lyricFile.exists()) {
			return lyricFile;
		}else {
			return null;
		}
	}
}
