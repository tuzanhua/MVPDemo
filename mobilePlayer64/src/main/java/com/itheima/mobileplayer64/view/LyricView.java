package com.itheima.mobileplayer64.view;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import com.itheima.mobileplayer64.R;
import com.itheima.mobileplayer64.bean.Lyric;
import com.itheima.mobileplayer64.db.LyricsParser;
import com.itheima.mobileplayer64.utils.LogUtils;

public class LyricView extends TextView {
	
	/** 高亮歌词的颜色 */
	private static final int HIGHTLIGHT_COLOR = Color.GREEN;
	/** 普通歌词的颜色 */
	private static final int NORMAL_COLOR = Color.WHITE;

	private Paint mPaint;
	private int mHighlightTextsize;
	private int mNormalTextsize;
	private int viewW;
	private int viewH;
	private int mLineHeight;
	private ArrayList<Lyric> mLyrics;
	/** 当前高亮的行位置 */
	private int mCurrentLine;
	private int mPosition;
	private int mDuration;

	public LyricView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public LyricView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public LyricView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		mHighlightTextsize = getResources().getDimensionPixelOffset(R.dimen.lyric_highlight_textsize);
		mNormalTextsize = getResources().getDimensionPixelOffset(R.dimen.lyric_normal_textsize);
		mLineHeight = getResources().getDimensionPixelOffset(R.dimen.lyric_line_height);
//		mHighlightTextsize = 22;
//		mNormalTextsize = 14;
//		mLineHeight = 29;
		
		mPaint = new Paint();
		mPaint.setColor(HIGHTLIGHT_COLOR);
		mPaint.setTextSize(mHighlightTextsize);
		mPaint.setAntiAlias(true);
		// 文字对齐方式
		mPaint.setTextAlign(Align.LEFT);
		
		// 模拟数据
//		mCurrentLine = 15;
//		mLyrics = new ArrayList<Lyric>();
//		for (int i = 0; i < 30; i++) {
//			mLyrics.add(new Lyric(i*2000, "当前歌词的行数为："+i));
//		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// 获取View宽高
		viewW = w;
		viewH = h;
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		if (mLyrics == null || mLyrics.size() == 0) {
			drawSingleLine(canvas,"正在加载歌词...");
		} else {
			// 绘制多行歌词
			drawMulitLines(canvas);
		}
	}

	/** 绘制一行居中的文字*/
	private void drawSingleLine(Canvas canvas,String text) {
		// 获取文字宽高
		Rect bounds = new Rect();		
		mPaint.getTextBounds(text, 0, text.length(), bounds);
		
//		int textW = bounds.width();
		int textW = (int) mPaint.measureText(text);
		int textH = bounds.height();
		
		int drawX = viewW/2 - textW/2;
		int drawY = viewH/2 + textH/2;
		
		canvas.drawText(text, drawX, drawY, mPaint);
	}

	/** 绘制多行水平居中的文字 */
	private void drawMulitLines(Canvas canvas) {
		Lyric lyric = mLyrics.get(mCurrentLine);
		
		// 根据已播放时间，对歌词位置进行偏移
		offsetY(canvas, lyric);
		
		// 获取居中绘制的Y位置
		Rect bounds = new Rect();
		mPaint.getTextBounds(lyric.getContent(), 0, lyric.getContent().length(), bounds);
		// int textW = bounds.width();
		int textH = bounds.height();
		int centerY = viewH / 2 + textH / 2;
		
		// 把歌词逐行绘制出来
		for (int i = 0; i < mLyrics.size(); i++) {
			// 更改画笔设置，区分高亮行和普通行
			if (i == mCurrentLine) {
				// 高亮行，绿色，大字体
				mPaint.setTextSize(mHighlightTextsize);
				mPaint.setColor(HIGHTLIGHT_COLOR);
			} else {
				// 普通行，白色，小字体
				mPaint.setTextSize(mNormalTextsize);
				mPaint.setColor(NORMAL_COLOR);
			}
			
			// 绘制该行歌词
			Lyric drawLyric = mLyrics.get(i);
			int drawY = centerY + (i - mCurrentLine)* mLineHeight;
			drawLineHorizontal(canvas, drawLyric.getContent(), drawY);
		}

	}

	/** 根据已播放时间，对歌词位置进行偏移 */
	private void offsetY(Canvas canvas, Lyric lyric) {
		// 获取当前行已消耗时间
		int pasttime = mPosition - lyric.getStartPoint();
		// 获取当前行可用时间
		int lineTime;
		if (mCurrentLine == mLyrics.size() -1) {
			// 最后一行
			lineTime = mDuration - lyric.getStartPoint();
		} else {
			// 非最后一行
			Lyric nextLyric = mLyrics.get(mCurrentLine+1);
			lineTime = nextLyric.getStartPoint() - lyric.getStartPoint();
		}
		// 计算当前行已使用时间的百分比
		float percent = pasttime / (float)lineTime;
		
		// 计算当前行应该偏移的大小
		int offsetY = (int) (percent * mLineHeight);
		
		// 使画布偏移指定的大小
		canvas.translate(0, - offsetY);
	}

	/** 在不同的y高度上绘制一行水平居中的文字 */
	private void drawLineHorizontal(Canvas canvas, String text, int drawY) {
		int textW = (int) mPaint.measureText(text);
		int drawX = viewW / 2 - textW / 2;
		canvas.drawText(text, drawX, drawY, mPaint);
	}
	
	/** 根据当前已播放时间，计算出需要高亮的行，并刷新显示 */
	public void setPosition(int position, int duration){
		mPosition = position;
		mDuration = duration;
		
		// 计算出高亮行
		getHighLightLIine(position);
		
		// 刷新界面
		invalidate();
	}

	/** 根据当前已播放时间计算出需要高亮的行数 */
	private void getHighLightLIine(int position) {
		if (mLyrics == null || mLyrics.size() == 0) {
			return;
		}
		// 高亮行的时间应小于position，并且下一行的时间大于position
		for (int i = 0; i < mLyrics.size(); i++) {
			Lyric lyric = mLyrics.get(i);
			if (i == mLyrics.size() -1 ) {
				if ( position > lyric.getStartPoint()) {
					// 最后一行
					mCurrentLine = i;
				}
			} else {
				// 非最后一行
				Lyric nextLyric = mLyrics.get(i + 1);
				
				LogUtils.e(TAG, "LyricView.getHighLightLIine.position="+position+";lyric="+lyric.getStartPoint()+";nextLyric="+nextLyric.getStartPoint()+"");
				if (lyric.getStartPoint() <= position && nextLyric.getStartPoint() > position) {
					mCurrentLine = i;
					break;
				} 
			}
		}
		
		LogUtils.e(TAG, "LyricView.getHighLightLIine.mCurrentLine="+mCurrentLine+";position="+position);
	}
	private static final String TAG = "itcast_LyricView";
	public void setLyricsFile(File lyricFile) {
		mLyrics = LyricsParser.parserFromFile(lyricFile);
	}
}
