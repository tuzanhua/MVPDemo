package com.itheima.mobileplayer64.ui.activity;

import java.io.File;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.itheima.mobileplayer64.R;
import com.itheima.mobileplayer64.bean.AudioItem;
import com.itheima.mobileplayer64.db.LyricsLoader;
import com.itheima.mobileplayer64.service.AudioPlayerService;
import com.itheima.mobileplayer64.service.AudioPlayerService.AudioServiceBinder;
import com.itheima.mobileplayer64.utils.StringUtils;
import com.itheima.mobileplayer64.view.LyricView;

public class AudioPlayerActivity extends BaseActivity {

	private AudioServiceConnection mAudioServiceConnection;
	private AudioServiceBinder mAudioServiceBinder;
	private ImageView iv_pause;
	private AudioReceiver mAudioReceiver;
	private TextView tv_title;
	private TextView tv_arties;
	private ImageView iv_wave;
	private TextView tv_position;
	private SeekBar sk_position;
	
	private static final int MSG_UPDATE_POSITION = 0;
	private static final int MSG_UPDATE_LYRICS = 1;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_POSITION:
				updatePositionDelay();
				break;
			case MSG_UPDATE_LYRICS:
				updateLyric();
				break;

			default:
				break;
			}
		};
	};
	private ImageView iv_pre;
	private ImageView iv_next;
	private ImageView iv_playmode;
	private LyricView lyricView;
	
	/** 当时间进度发生变更时会回调此类的方法 */
	private final class OnAudioSeekBarChangeListener implements
			OnSeekBarChangeListener {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// 只有是用户的滑动事件，才需要进行跳转
			if (fromUser) {
				// 更新播放位置
				mAudioServiceBinder.seekTo(progress);
				updatePosition(progress);
			}
		}
	}

	/** 获取应用中与音乐播放相关的广播 */
	private final class AudioReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if ("com.itheima.mobileplayer.pause_status_change".equals(intent.getAction())) {
				// 获取当前播放状态
				boolean pauseStatus = intent.getBooleanExtra("pause_status", true);
				logE("AudioPlayerActivity.onReceive。pause_status_change");
				// 播放状态发生变化，更新播放按钮
				updatePauseBtn(pauseStatus);
				
			} else if ("com.itheima.mobileplayer.prepared".equals(intent.getAction())) {
				logE("AudioPlayerActivity.onReceive。prepared");
				// 视频开始播放，更新界面
				// 更新播放按钮
				updatePauseBtn(true);
				
				// 更新歌曲名，歌手名
				AudioItem audioItem = (AudioItem) intent.getSerializableExtra("audioitem");
				tv_title.setText(audioItem.getTitle());
				tv_arties.setText(audioItem.getArties());
				
				// 开启时间更新
				sk_position.setMax(mAudioServiceBinder.getDuration());
				updatePositionDelay();
				
				// 更新当前使用的播放模式
				updatePlayModeBtn();
				
				//设置歌词文件
//				String filename = "test/audio/" + audioItem.getTitle() + ".lrc";
//				File lyricFile = new File(Environment.getExternalStorageDirectory(), filename);
				File lyricFile = LyricsLoader.loadLyricFile(audioItem.getTitle());
				logE("AudioPlayerActivity.onReceive.lyricFile="+lyricFile);
				lyricView.setLyricsFile(lyricFile);
				
				// 开启歌词更新
				updateLyric();
			} else if ("com.itheima.mobileplayer.completion".equals(intent.getAction())) {
				// 视频播放结束，更新界面
				// 更新播放按钮使用的图片
				updatePauseBtn(false);
				// 已播放时间设为视频长度
				updatePosition(mAudioServiceBinder.getDuration());
			}
		}
	}

	/** 绑定服务使用的连接对象 */
	private final class AudioServiceConnection implements
			ServiceConnection {

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			logE("AudioPlayerActivity.onServiceConnected.service="+service);
			mAudioServiceBinder = (AudioServiceBinder) service;
		}
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.audio_player;
	}
	
	@Override
	protected void initView() {
		iv_pause = (ImageView) findViewById(R.id.audio_player_iv_pause);
		tv_title = (TextView) findViewById(R.id.audio_player_tv_title);
		tv_arties = (TextView) findViewById(R.id.audio_player_tv_arties);
		iv_wave = (ImageView) findViewById(R.id.audio_player_iv_wave);
		tv_position = (TextView) findViewById(R.id.audio_player_tv_position);
		sk_position = (SeekBar) findViewById(R.id.audio_player_sk_position); 
		iv_pre = (ImageView) findViewById(R.id.audio_player_iv_pre);
		iv_next = (ImageView) findViewById(R.id.audio_player_iv_next);
		iv_playmode = (ImageView) findViewById(R.id.audio_player_iv_playmode);
		lyricView = (LyricView) findViewById(R.id.audio_notify_lyricview);
	}

	@Override
	protected void initListener() {
		iv_pause.setOnClickListener(this);
		iv_pre.setOnClickListener(this);
		iv_next.setOnClickListener(this);
		iv_playmode.setOnClickListener(this);
		
		sk_position.setOnSeekBarChangeListener(new OnAudioSeekBarChangeListener());
		
		IntentFilter filter = new IntentFilter("com.itheima.mobileplayer.pause_status_change");
		filter.addAction("com.itheima.mobileplayer.prepared");
		filter.addAction("com.itheima.mobileplayer.completion");
		mAudioReceiver = new AudioReceiver();
		registerReceiver(mAudioReceiver, filter);
	}

	@Override
	protected void initData() {

		// 获取数据
		// 传统方式
//		ArrayList<AudioItem> audioItems = (ArrayList<AudioItem>) getIntent().getSerializableExtra("audioItems");
//		int position = getIntent().getIntExtra("position", -1);
//		
//		Intent intent = new Intent(this, AudioPlayerService.class);
//		intent.putExtra("audioItems", audioItems);
//		intent.putExtra("position", position);
//		// 启动service播放歌曲
//		startService(intent);
		
		// 简单方式
		Intent intent = new Intent(getIntent());
		intent.setClass(this, AudioPlayerService.class);
		
		// 启动service播放歌曲
		mAudioServiceConnection = new AudioServiceConnection();
		bindService(intent, mAudioServiceConnection, Service.BIND_AUTO_CREATE);
		startService(intent);
		
		// 开启示波器动画
		AnimationDrawable waveAnim = (AnimationDrawable) iv_wave.getBackground();
		waveAnim.start();
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.audio_player_iv_pause:
			switchPauseStatus();
			break;
		case R.id.audio_player_iv_pre:
			playPre();
			break;
		case R.id.audio_player_iv_next:
			playNext();
			break;
		case R.id.audio_player_iv_playmode:
			switchPlayMode();
			break;

		default:
			break;
		}
	}

	/** 切换暂停/播放状态  */
	private void switchPauseStatus() {
		mAudioServiceBinder.switchPauseStatus();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		logE("AudioPlayerActivity.onDestroy");
		// 解绑服务
		unbindService(mAudioServiceConnection);
		
		// 解绑广播接收者
		unregisterReceiver(mAudioReceiver);
		
		// 移除mHandler控制的消息循环
		mHandler.removeCallbacksAndMessages(null);
	}
	
	/** 更新暂停按钮的图片 */
	private void updatePauseBtn(boolean isPlaying) {
		logE("AudioPlayerActivity.updatePauseBtn");
		if (isPlaying) {
			iv_pause.setImageResource(R.drawable.audio_player_pause_selector);
			// 开始播放的时候开启进度更新
			sendUpdatePositionMsg();
//			updateLyric();
		} else {
			iv_pause.setImageResource(R.drawable.audio_player_play_selector);
			// 暂停播放的时候停止进度更新
			mHandler.removeMessages(MSG_UPDATE_POSITION);
//			mHandler.removeMessages(MSG_UPDATE_LYRICS);
		}
	}

	/** 更新播放时间，并延迟一段时间后再次更新 */
	private void updatePositionDelay() {
		// 更新界面
		int position = mAudioServiceBinder.getCurrentPosition();
//		logE("AudioPlayerActivity.updatePositionDelay.position="+position);
		updatePosition(position);
		
		// 发送延迟更新消息
		sendUpdatePositionMsg();
	}

	/** 发送延迟更新播放时间的消息 */
	private void sendUpdatePositionMsg() {
		mHandler.removeMessages(MSG_UPDATE_POSITION);
		mHandler.sendEmptyMessageDelayed(MSG_UPDATE_POSITION, 500);
	}

	/** 更新进度显示的文字 */
	private void updatePosition(int position) {
		logE("AudioPlayerActivity.updatePosition.position="+position);
		int duration = mAudioServiceBinder.getDuration();
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtils.formartDuration(position))
		.append("/")
		.append(StringUtils.formartDuration(duration));
		
		tv_position.setText(sb.toString());
		sk_position.setProgress(position);
	}

	/** 播放上一首歌 */
	private void playPre() {
		mAudioServiceBinder.playPre();
	}

	/** 播放下一首歌 */
	private void playNext() {
		mAudioServiceBinder.playNext();
	}

	/** 切换播放模式 */
	private void switchPlayMode() {
		mAudioServiceBinder.switchPlayMode();
		updatePlayModeBtn();
	}

	/** 根据当前使用的播放模式，更新使用的图片 */
	private void updatePlayModeBtn() {
		switch (mAudioServiceBinder.getPlayMode()) {
		case AudioPlayerService.PLAYMODE_ALL_REPEAT:
			// 列表循环
			iv_playmode.setImageResource(R.drawable.audio_playemode_allrepeat_selector);
			break;
		case AudioPlayerService.PLAYMODE_RANDOM:
			// 随机播放
			iv_playmode.setImageResource(R.drawable.audio_playemode_random_selector);
			break;
		case AudioPlayerService.PLAYMODE_SINGLE_REPEAT:
			// 单曲循环
			iv_playmode.setImageResource(R.drawable.audio_playemode_singlerepeat_selector);
			break;
		}
	}
	
	/** 更新歌词位置 */
	private void updateLyric() {
		lyricView.setPosition(mAudioServiceBinder.getCurrentPosition(), mAudioServiceBinder.getDuration());
		
//		mHandler.removeMessages(MSG_UPDATE_LYRICS);
//		mHandler.sendEmptyMessage(MSG_UPDATE_LYRICS);
	}

}
