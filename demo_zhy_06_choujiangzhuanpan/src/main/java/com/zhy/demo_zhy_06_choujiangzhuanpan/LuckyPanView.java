package com.zhy.demo_zhy_06_choujiangzhuanpan;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class LuckyPanView extends SurfaceView implements Callback, Runnable
{

	private SurfaceHolder mHolder;
	/**
	 * 与SurfaceHolder绑定的Canvas
	 */
	private Canvas mCanvas;
	/**
	 * 用于绘制的线程
	 */
	private Thread t;
	/**
	 * 线程的控制开关
	 */
	private boolean isRunning;

	public LuckyPanView(Context context)
	{
		this(context, null);
	}

	public LuckyPanView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mHolder = getHolder();
		mHolder.addCallback(this);

		// setZOrderOnTop(true);// 设置画布 背景透明
		// mHolder.setFormat(PixelFormat.TRANSLUCENT);
		
		//设置可获得焦点
		setFocusable(true);
		setFocusableInTouchMode(true);
		//设置常亮
		this.setKeepScreenOn(true);

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{

		// 开启线程
		isRunning = true;
		t = new Thread(this);
		t.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// 通知关闭线程
		isRunning = false;
	}

	@Override
	public void run()
	{
		// 不断的进行draw
		while (isRunning)
		{
			draw();
		}

	}

	private void draw()
	{
		try
		{
			// 获得canvas
			mCanvas = mHolder.lockCanvas();
			if (mCanvas != null)
			{
				// drawSomething..
			}
		} catch (Exception e)
		{
		} finally
		{
			if (mCanvas != null)
				mHolder.unlockCanvasAndPost(mCanvas);
		}
	}
}
