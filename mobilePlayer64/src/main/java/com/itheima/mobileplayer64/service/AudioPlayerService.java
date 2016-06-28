package com.itheima.mobileplayer64.service;

import java.util.ArrayList;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.itheima.mobileplayer64.R;
import com.itheima.mobileplayer64.bean.AudioItem;
import com.itheima.mobileplayer64.ui.activity.AudioPlayerActivity;
import com.itheima.mobileplayer64.utils.LogUtils;

public class AudioPlayerService extends Service {
	private static final String TAG = "itcast_AudioPlayerService";
	
	private SharedPreferences mPreferences;
	private AudioServiceBinder mAudioServiceBinder;
	private ArrayList<AudioItem> mAudioItems;
	private int mPosition;
	
	/** 播放模式：列表循环 */
	public static final int PLAYMODE_ALL_REPEAT = 0;
	/** 播放模式：随机播放 */
	public static final int PLAYMODE_RANDOM = 1;
	/** 播放模式：单曲循环 */
	public static final int PLAYMODE_SINGLE_REPEAT = 2;
	/** 记录当前使用的播放模式 */
	private int mPlayMode = PLAYMODE_ALL_REPEAT;
	
	/** 表示通知类型的key */
	private static final String KEY_NOTIFY_TYPE = "notify_type";
	/** 通知栏事件：前一曲 */
	private static final int NOTIFY_TYPE_PRE = 0;
	/** 通知栏事件：后一曲 */
	private static final int NOTIFY_TYPE_NEXT = 1;
	/** 通知栏事件：空白部分点击 */
	private static final int NOTIFY_TYPE_CONTAINER = 2;

	@Override
	public IBinder onBind(Intent intent) {
		LogUtils.e(TAG, "AudioPlayerService.onBind");
		return mAudioServiceBinder;
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		super.unbindService(conn);
		LogUtils.e(TAG, "AudioPlayerService.unbindService");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.e(TAG, "AudioPlayerService.onDestroy");
		mAudioServiceBinder.release();
	}

	@Override
	public void onCreate() {
		LogUtils.e(TAG, "AudioPlayerService.onCreate");
		super.onCreate();
		mAudioServiceBinder = new AudioServiceBinder();
		mPreferences = getSharedPreferences("audio.conf", MODE_PRIVATE);
		
		// 初始化用户选择的播放模式
		mPlayMode = mPreferences.getInt("playmode", PLAYMODE_ALL_REPEAT);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		int notify_type = intent.getIntExtra(KEY_NOTIFY_TYPE, -1);
		LogUtils.e(TAG, "AudioPlayerService.onStartCommand，notify_type="+notify_type);
		if (notify_type!=-1) {
			// 从通知启动的服务
			switch (notify_type) {
			case NOTIFY_TYPE_PRE:
				// 播放上一首歌
				mAudioServiceBinder.playPre();
				break;
			case NOTIFY_TYPE_NEXT:
				// 播放下一首歌
				mAudioServiceBinder.playNext();
				break;
			case NOTIFY_TYPE_CONTAINER:
				// 通知栏打开播放界面，保持播放原先的歌曲，通知界面更新即可
				mAudioServiceBinder.notifyPrepared();
				break;
			}
			
		} else {
			// 从播放界面正常启动

			// 获取要播放的歌曲信息
			mAudioItems = (ArrayList<AudioItem>) intent.getSerializableExtra("audioItems");
			int position = intent.getIntExtra("position", -1);
			if (mPosition == position) {
				// 同一首歌，保持播放原先的歌曲，通知界面更新即可
				mAudioServiceBinder.notifyPrepared();
			} else {
				// 不同的歌，播放新歌
				mPosition = position;
				mAudioServiceBinder.play();
			}
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	/** 显示通知 */
	private void showNotification() {
		// contentView在Android3.0以后才能正常使用，在3.0以前只能使用普通的通知
		if (VERSION.SDK_INT < VERSION_CODES.HONEYCOMB) {
			showNormalNotification();
		} else {
			showCustomViewNotify();
//			showCustomViewNotifyByNewAPI();
		}		
		
	}

//	/** 使用新API来显示通知 */
//	private void showCustomViewNotifyByNewAPI() {
//
//		// 获取当前播放的歌曲
//		AudioItem audioItem = mAudioItems.get(mPosition);
//		// 使用新的API来生成通知
//		Notification.Builder builder = new Notification.Builder(this);
//		builder.setSmallIcon(R.drawable.notification_music_playing)
//		.setTicker("正在播放："+audioItem.getArties())
//		.setOngoing(true)
////		.setWhen(System.currentTimeMillis())
////		.setContentTitle("听说")
////		.setContentText("华子")
////		.setContentIntent(getContainerPendingIntent());
//		.setContent(getRemoteView());
//		Notification notification = builder.getNotification();
//		
//		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		notificationManager.notify(0, notification);
//	}

	/** 显示一个自定义布局的通知 */
	private void showCustomViewNotify() {
		
		LogUtils.e(TAG, "AudioPlayerService.showCustomViewNotify");
		// 获取当前播放的歌曲
		AudioItem audioItem = mAudioItems.get(mPosition);
		
		// 创建一个自定义布局的通知
		Notification notification = new Notification(R.drawable.notification_music_playing, "正在播放："+audioItem.getTitle(), System.currentTimeMillis());
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.contentView = getRemoteView();
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
	}

	private RemoteViews getRemoteView() {
		
		LogUtils.e(TAG, "AudioPlayerService.getRemoteView");
		// 获取当前播放的歌曲
		AudioItem audioItem = mAudioItems.get(mPosition);
		
		// 创建RemoteView
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.audio_notify);

		// 设置RemoteView里控件的文字
		remoteViews.setTextViewText(R.id.audio_notify_tv_title, audioItem.getTitle());
		remoteViews.setTextViewText(R.id.audio_notify_tv_arties, audioItem.getArties());
		
		// 设置RemoteView里的点击事件
		remoteViews.setOnClickPendingIntent(R.id.audio_notify_iv_pre, getPrePendingIntent());
		remoteViews.setOnClickPendingIntent(R.id.audio_notify_iv_next, getNextPendingIntent());
		remoteViews.setOnClickPendingIntent(R.id.audio_notify_layout, getContainerPendingIntent());
		
		return remoteViews;
	}

	/** 生成点击上一曲按钮使用的PendingIntent */
	private PendingIntent getPrePendingIntent() {
		
		LogUtils.e(TAG, "AudioPlayerService.getPrePendingIntent");
		Intent intent = new Intent(this, AudioPlayerService.class);
		intent.putExtra(KEY_NOTIFY_TYPE, NOTIFY_TYPE_PRE);
		return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	/** 生成点击下一曲按钮使用的PendingIntent */
	private PendingIntent getNextPendingIntent() {
		
		LogUtils.e(TAG, "AudioPlayerService.getNextPendingIntent");
		Intent intent = new Intent(this, AudioPlayerService.class);
		intent.putExtra(KEY_NOTIFY_TYPE, NOTIFY_TYPE_NEXT);
		return PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	/** 生成点击空白部分使用的PendingIntent */
	private PendingIntent getContainerPendingIntent() {
		
		LogUtils.e(TAG, "AudioPlayerService.getContainerPendingIntent");
		Intent intent = new Intent(this, AudioPlayerActivity.class);
		intent.putExtra(KEY_NOTIFY_TYPE, NOTIFY_TYPE_CONTAINER);
		return PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	/** 弹出一个普通的通知 */
	private void showNormalNotification() {
		
		LogUtils.e(TAG, "AudioPlayerService.showNormalNotification");
		AudioItem audioItem = mAudioItems.get(mPosition);
		//显示一个普通的通知
		Notification notification = new Notification(R.drawable.icon, "正在播放："+audioItem.getTitle(), System.currentTimeMillis());
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.setLatestEventInfo(this, audioItem.getTitle(), audioItem.getArties(), getContainerPendingIntent());
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
	}

	/** 取消通知 */
	private void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
	}
	
	public class AudioServiceBinder extends Binder{
		
		/** 播放结束，通知界面更新 */
		private final class OnAudioCompletionListener implements
				OnCompletionListener {
			@Override
			public void onCompletion(MediaPlayer mp) {
				// 通知界面更新
				notifyCompletion();
				// 自动播放下一首歌
				autoPlayNext();
			}
		}

		/** 音频准备完毕，开始播放 */
		private final class OnAudioPreparedListener implements
				OnPreparedListener {
			@Override
			public void onPrepared(MediaPlayer mp) {
				// 准备完成，开始播放
				mediaPlayer.start();
				
				// 通知界面更新
				notifyPrepared();
				
				// 显示通知
				showNotification();
			}
		}

		private MediaPlayer mediaPlayer;

		/** 播放当前mPosition位置的歌曲 */
		public void play() {
			// 数据可用性判断
			if (mAudioItems == null || mPosition == -1 || mAudioItems.size() == 0) {
				return;
			}
			
			try {
				// 播放选中的歌曲
				AudioItem item = mAudioItems.get(mPosition);
				LogUtils.e(TAG, "AudioPlayerService.onStartCommand.item="+item);

				// 释放掉旧的歌曲资源
				release();
				
				// 播放新的歌曲
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(AudioPlayerService.this, Uri.parse(item.getPath()));
				mediaPlayer.setOnPreparedListener(new OnAudioPreparedListener());
				mediaPlayer.setOnCompletionListener(new OnAudioCompletionListener());
				mediaPlayer.prepareAsync();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/** 释放掉旧的歌曲资源 */
		private void release() {
			if (mediaPlayer != null) {
				mediaPlayer.release();
				mediaPlayer = null;
			}
		}
		
		/** 切换播放/暂停 */
		public void switchPauseStatus() {
			if (mediaPlayer == null)
				return;
			
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				// 取消通知
				cancelNotification();
			} else {
				mediaPlayer.start();
				// 显示通知
				showNotification();
			}
			
			notifyPauseStatus();
		}
		
		/** 发送广播通知播放状态的变化 */
		private void notifyPauseStatus() {
			Intent intent = new Intent("com.itheima.mobileplayer.pause_status_change");
			intent.putExtra("pause_status", mediaPlayer.isPlaying());
			sendBroadcast(intent);
		}

		/** 音乐已经开始播放，通知界面更新 */
		private void notifyPrepared() {
			Intent intent = new Intent("com.itheima.mobileplayer.prepared");
			intent.putExtra("audioitem", mAudioItems.get(mPosition));
			sendBroadcast(intent);
		}
		
		/** 音乐播放结束，通知界面更新 */
		public void notifyCompletion() {
			Intent intent = new Intent("com.itheima.mobileplayer.completion");
			sendBroadcast(intent);
		}
		
		/** 返回当前正在播放音乐的长度 */
		public int getDuration() {
			return mediaPlayer == null ? 0 : mediaPlayer.getDuration();
		}
		
		/** 返回当前正在播放音乐的长度 */
		public int getCurrentPosition() {
			return mediaPlayer == null ? 0 : mediaPlayer.getCurrentPosition();
		}
		
		/** 跳转到指定为位置播放 */
		public void seekTo(int position) {
			if (mediaPlayer!=null) {
				mediaPlayer.seekTo(position);
			} 
		}
		
		/** 播放上一首歌 */
		public void playPre() {
			if (mPosition != 0) {
				// 播放位置前移
				mPosition--;
				
				// 播放歌曲
				play();
			} else {
				Toast.makeText(AudioPlayerService.this, "已经是第一首歌了！", Toast.LENGTH_SHORT).show();
			}
		}
		
		/** 播放下一首歌 */
		public void playNext() {
			if (mPosition!= mAudioItems.size()-1) {
				// 播放位置后移
				mPosition++;
				
				// 播放歌曲
				play();
			} else {
				Toast.makeText(AudioPlayerService.this, "已经是最后一首歌了！", Toast.LENGTH_SHORT).show();
			}
		}
		
		/** 根据当前播放模式，切换到下一种模式 */
		public void switchPlayMode() {
			switch (mPlayMode) {
			case PLAYMODE_ALL_REPEAT:
				mPlayMode = PLAYMODE_RANDOM;
				break;
			case PLAYMODE_RANDOM:
				mPlayMode = PLAYMODE_SINGLE_REPEAT;
				break;
			case PLAYMODE_SINGLE_REPEAT:
				mPlayMode = PLAYMODE_ALL_REPEAT;
				break;
			}
			
			// 播放模式发生变更，将其记录到配置文件
			mPreferences.edit().putInt("playmode", mPlayMode).commit();
		}

		/** 根据当前播放模式，自动选择下一首歌，并播放 */
		public void autoPlayNext() {
			// 数据可用性验证
			if (mAudioItems == null) {
				return;
			}
			
			// 根据当前的播放模式，修改mPosition的值
			switch (mPlayMode) {
			case PLAYMODE_ALL_REPEAT:
				// 自动播放下一首歌，如果是最后一首歌则回到第一首歌
				if (mPosition == mAudioItems.size() - 1) {
					mPosition = 0;
				} else {
					mPosition ++;
				}
				
				break;
			case PLAYMODE_RANDOM:
				// 随机选择一首歌
				mPosition = new Random().nextInt(mAudioItems.size());
				
				break;
			case PLAYMODE_SINGLE_REPEAT:
				// 保持当前的歌
				
				break;
			}
			
			// 播放歌曲
			play();
		}
		
		/** 返回当前使用的播放模式  */
		public int getPlayMode() {
			return mPlayMode;
		}
	}
}
