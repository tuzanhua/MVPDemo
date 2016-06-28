package com.itheima.mobileplayer64.bean;

public class Lyric implements Comparable<Lyric>{

	/** 当前行起始播放时间 */
	private int startPoint;
	/** 当前行的文字内容 */
	private String content;

	public Lyric(int startPoint, String content) {
		super();
		this.startPoint = startPoint;
		this.content = content;
	}

	public int getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(int startPoint) {
		this.startPoint = startPoint;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int compareTo(Lyric another) {
		return this.startPoint - another.startPoint;
	}

}
