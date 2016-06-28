/*package com.itheima.mobileplayer64.ui.activity;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.widget.VideoView;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.itheima.mobileplayer64.R;
import com.itheima.mobileplayer64.bean.VideoItem;
import com.itheima.mobileplayer64.utils.StringUtils;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class VitamioPlayerActivity extends BaseActivity {
	
	private static final int MSG_UPDATE_SYSTEM_TIME = 0;
	private static final int MSG_UPDATE_POSITION = 1;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_SYSTEM_TIME:
				// 更新系统时间
				updateSystemTime();
				break;
			case MSG_UPDATE_POSITION:
				// 更新已播放进度
				updatePositionDelay();
				break;
			default:
				break;
			}
		};
	};

	*//** 当播放发生错误时被回调 *//*
	private final class OnVideoErrorListener implements
			OnErrorListener {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// 当播放发生错误的时候，显示一个提示对话框，点击则退出界面
			AlertDialog.Builder builder = new AlertDialog.Builder(VitamioPlayerActivity.this);
			builder.setTitle("提示")
			.setMessage("不支持播放该视频")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).create().show();
			return false;
		}
	}

	*//** 在播放过程中发生一些状态变更时回调 *//*
	private final class OnVideoInfoListener implements OnInfoListener {
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			// 当视频播放中发生缓冲的时候，显示一个等待提示
			switch (what) {
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				holding.setVisibility(View.VISIBLE);
				break;
			case MediaPlayer.MEDIA_INFO_BUFFERING_END:
				holding.setVisibility(View.GONE);
				break;
			}
			return false;
		}
	}

	*//**当缓冲进度发生变化是被回调 *//*
	private final class OnVideoBufferingUpdateListener implements
			OnBufferingUpdateListener {
		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			logE("VideoPlayerActivity.onBufferingUpdate.percent="+percent);
			// 计算出缓冲的百分比
			float bufferPercent = percent / 100f;
			// 根据视频总长度，计算缓冲的时间大小
			int bufferTime = (int) (bufferPercent * mp.getDuration());
			
			// 更新第二进度的位置
			sk_position.setSecondaryProgress(bufferTime);
		}
	}

	*//** 手势操作的监听器 *//*
	private final class SimpleVideoOnGestureListener extends
			GestureDetector.SimpleOnGestureListener {
		
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			
			if (mIsControlorShowing) {
				// 隐藏控制面板
				hideControlor();
			} else {
				// 显示控制面板
				showControlor();
			}
			
			return super.onSingleTapConfirmed(e);
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			// 切换暂停/播放状态
			switchPauseStatus();
		}
		
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			//切换全屏状态
			switchFullScreen();
			
			return super.onDoubleTap(e);
		}
	}

	*//** 当视频播放结束时会回调此监听 *//*
	private final class OnVideoCompletionListener implements
			OnCompletionListener {
		@Override
		public void onCompletion(MediaPlayer mp) {
			logE("VideoPlayerActivity.onCompletion.isplaying="+mp.isPlaying());
			// 切换播放按钮
			updatePauseBtn(false);
			
			// 不再更新进度
			mHandler.removeMessages(MSG_UPDATE_POSITION);
			
			// 由于系统存在误差，播放完毕时可能MediaPlayer的播放进度并不完整，需要把进度设置为视频长度
			updatePosition((int) mp.getDuration());
		}
	}

	*//** SeekBar变更的监听器 *//*
	private final class OnVideoSeekBarChangeListener implements
			OnSeekBarChangeListener {
		@Override
		*//** 手指离开SeekBar时调用 *//*
		public void onStopTrackingTouch(SeekBar seekBar) {
		}

		@Override
		*//** 手指放到SeekBar上时调用 *//*
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		*//** 当SeekBar进度发生变更的时候被掉用 *//*
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
//			logE("VideoPlayerActivity.onProgressChanged.fromUser="+fromUser+";progress="+progress);
			
			// 非用户发起的修改，则不处理。如：setmax的时候会使用0来初始化进度，此时不应该修改音量
			if (!fromUser) {
				return;
			}
			
			switch (seekBar.getId()) {
			case R.id.video_player_sk_volume:
				// 修改系统音量
				updateVoulme(progress);
				
				break;
			case R.id.video_player_sk_position:
				// 跳转播放进度
				videoView.seekTo(progress);
				tv_position.setText(StringUtils.formartDuration(progress));
				
				break;

			default:
				break;
			}
		}
	}

	*//** 关注电量变更的广播 *//*
	private class OnBatteyChangeReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// 获取电量的key为：level
			int level = intent.getIntExtra("level", 0);
//			logE("VideoPlayerActivity.onReceive.level="+level);
			// 更新电量的图片
			updateBatteryImage(level);
		}
	}
	
	*//** 当视频准备完成后自动开始播放 *//*
	private final class OnVideoPreparedListener implements
			OnPreparedListener {
		@Override
		public void onPrepared(MediaPlayer mp) {
			logE("VideoPlayerActivity.onPrepared");

			// 隐藏加载遮罩
			loading_cover.setVisibility(View.GONE);
			
			// 当进入prepare状态后开始播放视频
			videoView.start();
			
			// 初始化时间进度
			tv_duration.setText(StringUtils.formartDuration((int) mp.getDuration()));
			sk_position.setMax((int) mp.getDuration());
			
			// 开启进度更新
			updatePositionDelay();
			
			// 更新暂停按钮
			updatePauseBtn(videoView.isPlaying());
			
		}
	}
	private OnBatteyChangeReceiver mBatteyChangeReceiver;

	private VideoView videoView;
	private TextView tv_title;
	private TextView tv_system_time;
	private ImageView iv_battery;

	private SeekBar sk_volume;

	private AudioManager mAudioManager;

	private ImageView iv_mute;

	private int mLastVolume;

	private float mStartY;

	private int mStartVolume;

	private int mScreenH;

	private int mScreenW;

	private View alpha_cover;

	private float mStartAlpha;

	private TextView tv_position;

	private TextView tv_duration;

	private SeekBar sk_position;
	private ImageView iv_pause;
	private int mPosition;
	private ArrayList<VideoItem> mVideoItems;
	private ImageView iv_pre;
	private ImageView iv_next;
	private LinearLayout ll_top;
	private LinearLayout ll_bottom;
	private GestureDetector mGestureDetector;
	*//** 按钮面板是否显示的标志位 *//*
	private boolean mIsControlorShowing = false;
	private ImageView iv_fullscreen;
	private View loading_cover;
	private View holding;

	@Override
	protected int getLayoutResId() {
		return R.layout.vitamio_player;
	}

	@Override
	protected void initView() {
		videoView = (VideoView) findViewById(R.id.video_player_videoview);
		tv_title = (TextView) findViewById(R.id.video_player_tv_title);
		tv_system_time = (TextView) findViewById(R.id.video_player_tv_system_time);
		iv_battery = (ImageView) findViewById(R.id.video_player_iv_battery);
		sk_volume = (SeekBar) findViewById(R.id.video_player_sk_volume);
		iv_mute = (ImageView) findViewById(R.id.video_player_iv_mute);
		alpha_cover = findViewById(R.id.video_player_alpha_cover);
		tv_position = (TextView) findViewById(R.id.video_player_tv_position);
		tv_duration = (TextView) findViewById(R.id.video_player_tv_duration);
		sk_position = (SeekBar) findViewById(R.id.video_player_sk_position);
		iv_pause = (ImageView) findViewById(R.id.video_player_iv_pause); 
		iv_pre = (ImageView) findViewById(R.id.video_player_iv_pre);
		iv_next = (ImageView) findViewById(R.id.video_player_iv_next);
		iv_fullscreen = (ImageView) findViewById(R.id.video_player_iv_fullscreen);
		
		ll_top = (LinearLayout) findViewById(R.id.video_player_ll_top); 
		ll_bottom = (LinearLayout) findViewById(R.id.video_player_ll_bottom); 
		loading_cover = findViewById(R.id.video_player_loading_cover);
		holding = findViewById(R.id.video_player_holding);
	}

	@Override
	protected void initListener() {
		// 在PrepareListener里开启播放
		videoView.setOnPreparedListener(new OnVideoPreparedListener());
		// 注册播放完毕的处理监听
		videoView.setOnCompletionListener(new OnVideoCompletionListener());
		// 注册缓冲进度更新监听
		videoView.setOnBufferingUpdateListener(new OnVideoBufferingUpdateListener());
		// 注册缓冲的状态监听
		videoView.setOnInfoListener(new OnVideoInfoListener());
		// 注册错误状态监听
		videoView.setOnErrorListener(new OnVideoErrorListener());
		
		// 注册音量控制条的滑动监听器
		OnVideoSeekBarChangeListener mSeekBarChangeListener = new OnVideoSeekBarChangeListener();
		sk_volume.setOnSeekBarChangeListener(mSeekBarChangeListener);
		sk_position.setOnSeekBarChangeListener(mSeekBarChangeListener);
		
		// 注册界面上按钮的点击事件
		iv_mute.setOnClickListener(this);
		iv_pause.setOnClickListener(this);
		iv_pre.setOnClickListener(this);
		iv_next.setOnClickListener(this);
		iv_fullscreen.setOnClickListener(this);

		// 处理单击、双击、长按
		mGestureDetector = new GestureDetector(this, new SimpleVideoOnGestureListener());
		
		// 注册关注电量变更的广播接收者
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		mBatteyChangeReceiver = new OnBatteyChangeReceiver();
		registerReceiver(mBatteyChangeReceiver, filter);
	}

	@Override
	protected void initData() {
		
		// 检测Vitamio的C库是否支持
		LibsChecker.checkVitamioLibs(mActivity);
		
		Uri uri = getIntent().getData();
		logE("VideoPlayerActivity.initData.uri="+uri);
		
		if (uri == null) {
			// 从应用内部发起的调用
			initFromInside();
		} else {
			// 从应用外部发起的调用
			videoView.setVideoURI(uri);
			iv_pre.setEnabled(false);
			iv_next.setEnabled(false);
			tv_title.setText(uri.getPath());
		}
		
		
		// 初始化系统时间
		updateSystemTime();
		
		// 初始化音量控制条
		initVolumeSeekBar();
		
		// 获取屏幕宽高，供手势滑动时使用
		mScreenH = getWindowManager().getDefaultDisplay().getHeight();
		mScreenW = getWindowManager().getDefaultDisplay().getWidth();
		
		// 初始屏幕亮度为最亮
		ViewHelper.setAlpha(alpha_cover, 0);
		
		// 隐藏控制面板
		initHideControlor();
		
		// 根据VideoView的配置切换全屏按钮
		updateFullScreenBtn();
	}

	private void initFromInside() {
		// 获取初始化数据
		// 使用单个item打开播放界面
//		VideoItem videoItem = (VideoItem) getIntent().getSerializableExtra("videoItem");
		
		// 使用整个播放列表进入界面
		mVideoItems = (ArrayList<VideoItem>) getIntent().getSerializableExtra("videoItems");
		mPosition = getIntent().getIntExtra("position", -1);
		
		// 播放选中的视频
		playItem();
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.video_player_iv_mute:
			// 切换静音状态
			switchMuteStatus();
			
			break;
		case R.id.video_player_iv_pause:
			switchPauseStatus();
			break;
		case R.id.video_player_iv_pre:
			playPreVideo();
			break;
		case R.id.video_player_iv_next:
			playNextVideo();
			break;
		case R.id.video_player_iv_fullscreen:
			switchFullScreen();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 移除当前界面上的所有延迟消息
		mHandler.removeCallbacksAndMessages(null);

		// 移除广播接收者
		unregisterReceiver(mBatteyChangeReceiver);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 获取手指按下时的位置
			mStartY = event.getY();
			mStartVolume = getCurrentVolume();
			mStartAlpha = ViewHelper.getAlpha(alpha_cover);
			break;
		
		case MotionEvent.ACTION_MOVE:
			// 获取手指当前的位置
			float currentY = event.getY();
			
			// 计算手指移动的距离
			float offsetY = mStartY - currentY;
			
			// 计算手指一动距离占屏幕的百分比
			float movePercent = offsetY / mScreenH;
			
			if (event.getX() <mScreenW/2) {
				// 屏幕左侧，处理亮度变化
				moveAlpha(movePercent);
			} else {
				// 屏幕右侧，处理音量变化
				moveVolume(movePercent);
			}
			
			break;
		default:
			break;
		}
		
		return super.onTouchEvent(event);
	}

	*//** 根据手指划过屏幕的百分比，修改屏幕亮度 *//*
	private void moveAlpha(float movePercent) {
		// 获取最终要使用的亮度大小
		float finalAlpha = mStartAlpha + movePercent;
//		logE("VideoPlayerActivity.moveAlpha.finalAlpha="+finalAlpha+";mStartAlpha="+mStartAlpha+";movePercent="+movePercent);
		if (finalAlpha >= 0 && finalAlpha <= 1) {
			// 修改亮度
			ViewHelper.setAlpha(alpha_cover, finalAlpha);
		}
	}

	*//** 根据手指在屏幕划过的百分比修改音量 *//*
	private void moveVolume(float movePercent) {
		// 计算要修改的音量大小
		int maxVolume =sk_volume.getMax();
		int offsetVolume = (int) (movePercent * maxVolume);
		
		// 获取最终要使用的音量大小
		int finalVolume = mStartVolume + offsetVolume;
		
		// 修改系统音量
		updateVoulme(finalVolume);
	}
	
	*//** 更新系统时间，并隔一段时间后再次更新 *//*
	private void updateSystemTime() {
//		logE("VideoPlayerActivity.updateSystemTime.time="+System.currentTimeMillis());
		tv_system_time.setText(StringUtils.fromatSystemTime());
		
		mHandler.sendEmptyMessageDelayed(MSG_UPDATE_SYSTEM_TIME, 500);
	}

	*//**
	 * 根据level的值更新电量控件使用的图片
	 * @param level 当前的系统电量，范围为[0,100]
	 *//*
	public void updateBatteryImage(int level) {
		if (level < 10) {
			iv_battery.setImageResource(R.drawable.ic_battery_0);
		} else if (level < 20) {
			iv_battery.setImageResource(R.drawable.ic_battery_10);
		} else if (level < 40) {
			iv_battery.setImageResource(R.drawable.ic_battery_20);
		} else if (level < 60) {
			iv_battery.setImageResource(R.drawable.ic_battery_40);
		} else if (level < 80) {
			iv_battery.setImageResource(R.drawable.ic_battery_60);
		} else if (level < 100) {
			iv_battery.setImageResource(R.drawable.ic_battery_80);
		} else {
			iv_battery.setImageResource(R.drawable.ic_battery_100);
		}
	}
	
	*//** 初始化界面时，根据当前系统电量设置seekbar的最大值和当前位置 *//*
	private void initVolumeSeekBar() {
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		sk_volume.setMax(maxVolume);
		
		logE("VideoPlayerActivity.initVolumeSeekBar,after set max");
		// 获取系统当前音量
		int currentVolume = getCurrentVolume();
		sk_volume.setProgress(currentVolume);
	}

	*//** 获取当前系统Music流的音量 *//*
	private int getCurrentVolume() {
		return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	*//** 更新系统Music流的音量为 progress的大小*//*
	private void updateVoulme(int progress) {
		// 更新音量
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
		// 音量发生变化的同时更新音量进度条位置
		sk_volume.setProgress(progress);
	}

	*//** 将系统音量在 0 和 初始音量之间切换 *//*
	private void switchMuteStatus() {
		if (getCurrentVolume() == 0) {
			// 静音状态,将音量恢复的原先的大小
			updateVoulme(mLastVolume);
		}else {
			// 非静音状态,记录当前音量，并将系统音量设置为0
			mLastVolume = getCurrentVolume();
			updateVoulme(0);
		}
	}

	*//** 更新已播放和时间进度条位置，并延迟一段时间后再次更新 *//*
	public void updatePositionDelay() {
		// 获取当前已播放时间
		int position = (int) videoView.getCurrentPosition();
//		logE("VideoPlayerActivity.updatePosition.position="+position);
		updatePosition(position);
		
		// 发送延迟更新消息
		mHandler.sendEmptyMessageDelayed(MSG_UPDATE_POSITION, 500);
	}

	*//** 根据position说明的大小更新时间进度 *//*
	private void updatePosition(int position) {
		// 更新界面
		tv_position.setText(StringUtils.formartDuration(position));
		sk_position.setProgress(position);
	}
		
	*//** 切换暂停/播放状态 *//*
	private void switchPauseStatus() {
		// 正在播放则暂停，否则开始播放
		if (videoView.isPlaying()) {
			videoView.pause();
			mHandler.removeMessages(MSG_UPDATE_POSITION);
		} else {
			videoView.start();
			mHandler.sendEmptyMessageDelayed(MSG_UPDATE_POSITION, 500);
		}
		updatePauseBtn(videoView.isPlaying());
	}

	*//** 切换暂停按钮使用的图片 
	 * @param isPlaying *//*
	private void updatePauseBtn(boolean isPlaying) {
		if (isPlaying) {
			iv_pause.setImageResource(R.drawable.video_player_pause_selector);
		} else {
			iv_pause.setImageResource(R.drawable.video_player_play_selector);
		}
	}

	*//** 播放当前mPsition指定的视频 *//*
	private void playItem() {
		// 数据可用性检测
		if (mVideoItems == null || mVideoItems.size() <= 0 || mPosition < 0) {
			return;
		}
		
		VideoItem videoItem = mVideoItems.get(mPosition);
//		logE("VideoPlayerActivity.initData.videoItem="+videoItem);
		
		// 播放视频
		Uri uri = Uri.parse(videoItem.getPath());
		videoView.setVideoURI(uri);
		
		// 初始化标题
		tv_title.setText(videoItem.getTitle());

		// 更新上一曲/下一曲 按钮
		updatePreAndNextBtn();
	}

	*//** 播放上一个视频 *//*
	private void playPreVideo() {
		if (mPosition > 0) {
			// 不是第一个视频，则把当播放的位置向前移一位
			mPosition--;
			// 播放视频
			playItem();
		}
	}

	*//** 播放下一个视频 *//*
	private void playNextVideo() {
		if (mPosition < mVideoItems.size() - 1) {
			// 不是最后一个视频，则把当前播放位置向后移一位
			mPosition++;
			// 播放视频
			playItem();
		}
	}

	*//** 更新上一曲/下一曲按钮的可用性  *//*
	private void updatePreAndNextBtn() {
		iv_pre.setEnabled(mPosition != 0);
		iv_next.setEnabled(mPosition != mVideoItems.size() - 1);
	}
	
	*//** 在界面初始化时隐藏控制面板 *//*
	private void initHideControlor() {
		// 使用getMeasureHeight获取顶部栏高度
		ll_top.measure(0, 0);
		int topH = ll_top.getMeasuredHeight();
		
		// 隐藏顶部栏
		ViewPropertyAnimator.animate(ll_top).translationY(-topH);
		
		// 使用getHeight获取底部栏高度
		ll_bottom.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				// 移除监听器
				ll_bottom.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				
				// 此时布局已经显示,getHeight可以获取到正确的高度
				logE("VideoPlayerActivity.onGlobalLayout.getHeight"+ll_bottom.getHeight());
				// 隐藏底部栏
				ViewPropertyAnimator.animate(ll_bottom).translationY(ll_bottom.getHeight());
			}
		});
	}

	*//** 隐藏控制面板 *//*
	public void hideControlor() {
		ViewPropertyAnimator.animate(ll_top).translationY(-ll_top.getHeight());
		ViewPropertyAnimator.animate(ll_bottom).translationY(ll_bottom.getHeight());
		mIsControlorShowing = false;
	}

	*//** 显示控制面板 *//*
	public void showControlor() {
		ViewPropertyAnimator.animate(ll_top).translationY(0);
		ViewPropertyAnimator.animate(ll_bottom).translationY(0);
		mIsControlorShowing = true;
	}	

	*//** 切换全屏/ 默认比例 *//*
	private void switchFullScreen() {
		videoView.switchFullScreen();
		
		updateFullScreenBtn();
	}

	*//** 根据当前是否是全屏，切换全屏按钮使用的图片 *//*
	private void updateFullScreenBtn() {
		if (videoView.isFullScreen()) {
			iv_fullscreen.setImageResource(R.drawable.video_player_defaultscreen_selector);
		} else {
			iv_fullscreen.setImageResource(R.drawable.video_player_fullscreen_selector);
		}
	}
}
*/