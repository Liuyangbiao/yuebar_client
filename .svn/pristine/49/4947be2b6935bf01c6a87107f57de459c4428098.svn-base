package com.fullteem.yueba.util;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;

public class VideoPlayerHelper implements
		MediaPlayer.OnBufferingUpdateListener,
		MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
		MediaPlayer.OnErrorListener {

	// 播放器的几种状态
	public final static int STATE_IDLE = 0;
	public final static int STATE_PLAYING = 1;
	public final static int STATE_PAUSE = 2;
	public final static int STATE_PREPARING = 3;

	// 发给ui的消息定义
	public final static int MSG_MEDIA_PLAYING = 1;
	public final static int MSG_MEDIA_PAUSE = 2;
	public final static int MSG_MEDIA_PREPARING = 3;
	public final static int MSG_MEDIA_END = 4;
	public final static int MSG_MEDIA_BUFFERING_START = 5;
	public final static int MSG_MEDIA_BUFFERING_END = 6;
	public final static int MSG_MEDIA_FULLSCREEN_TRIGGER = 7;

	// private int videoWidth;
	// private int videoHeight;
	// private int videoDuration;
	public MediaPlayer mediaPlayer;

	private Messenger mUiMsg = null;
	private int mState = STATE_IDLE;

	// 以下的用于检测卡顿
	// private Runnable mRunnable;
	private boolean mNetBuffering; // 是否由于网速等问题导致缓冲中
	private boolean mUserPause; // 是否是用户主动点击了暂停
	private boolean mIsExit = false;
	private int mOldPosition;
	private Context mContext;
	private SurfaceHolder mSurHolder;

	public VideoPlayerHelper() {

	}

	public void init(Messenger msger, SurfaceHolder surfh, Context context) {
		mUiMsg = msger;
		mSurHolder = surfh;
		mContext = context;
		mediaPlayer = new MediaPlayer();
		resetPlayer();
		// mHandler = new Handler(mContext.getMainLooper());
	}

	private void resetPlayer() {
		mediaPlayer.reset();
		mediaPlayer.setDisplay(mSurHolder);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);
	}

	// 返回当前播放器状态
	public int getState() {
		return mState;
	}

	public int getDuration() {
		return mediaPlayer.getDuration();

	}

	public int getCurrentPosition() {
		try {
			return mediaPlayer.getCurrentPosition();
		} catch (Exception e) {
			return -1;
		}

	}

	public void play() {
		mUserPause = false;
		mediaPlayer.start();
		mState = STATE_PLAYING;
		send_msg_ui(MSG_MEDIA_PLAYING);
	}

	public void seekTo(int progress) {
		mediaPlayer.seekTo(progress);
	}

	// 播放url指定的视频
	public void playUrl(String videoUrl) {
		try {
			LogUtil.LogInfo("VideoPlayerHelper", "for debug:the videoUrl is:"
					+ videoUrl, null);
			if (videoUrl == null || "".equalsIgnoreCase(videoUrl)) {
				videoUrl = "http://zb.atianqi.com:8889/app_2/ls_3.stream/playlist.m3u8";
			}

			if (mediaPlayer != null) {
				resetPlayer();
				mediaPlayer.setDataSource(videoUrl);
			}
			// 通知ui正在加载
			send_msg_ui(MSG_MEDIA_PREPARING);
			mediaPlayer.prepareAsync();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pause() {
		mUserPause = true;
		mediaPlayer.pause();
		mState = STATE_PAUSE;
		send_msg_ui(MSG_MEDIA_PAUSE);
	}

	private void ui_clear() {
		mUiMsg = null;
	}

	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
		mIsExit = true;
		mState = STATE_IDLE;
	}

	public void release() {
		stop();
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}

		ui_clear();
	}

	private void send_msg_ui(int msg) {
		if (mUiMsg == null)
			return;

		try {
			Message tmsg = Message.obtain(null, msg);
			mUiMsg.send(tmsg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// videoWidth = mediaPlayer.getVideoWidth();
		// videoHeight = mediaPlayer.getVideoHeight();
		// videoDuration = mediaPlayer.getDuration();

		mp.start();
		mState = STATE_PLAYING;
		send_msg_ui(MSG_MEDIA_PLAYING);

		mOldPosition = mediaPlayer.getCurrentPosition();
		mIsExit = false;

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (mIsExit)
						return;

					if (mUserPause) {
						try {
							Thread.sleep(500);
							continue;
						} catch (InterruptedException e) {
							return;
						}
					}

					int newpos = mediaPlayer.getCurrentPosition();

					if (newpos == mOldPosition) {
						mNetBuffering = true;
						send_msg_ui(MSG_MEDIA_BUFFERING_START);
					} else {
						if (mNetBuffering) {
							send_msg_ui(MSG_MEDIA_BUFFERING_END);
							mNetBuffering = false;
						}
					}

					mOldPosition = newpos;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		}).start();

	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		Log.e("mediaPlayer", "onCompletion");
		stop();
		send_msg_ui(MSG_MEDIA_END);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		Log.e("mediaPlayer", "onBufferingUpdate");
		/*
		 * skbProgress.setSecondaryProgress(bufferingProgress); int
		 * currentProgress
		 * =skbProgress.getMax()*mediaPlayer.getCurrentPosition()/
		 * mediaPlayer.getDuration(); // Log.e(currentProgress+"% play",
		 * bufferingProgress + "% buffer");
		 */
	}

	@Override
	public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
		Toast.makeText(AppContext.getApplication(),
				mContext.getString(R.string.cannot_play), Toast.LENGTH_SHORT)
				.show();
		// stop();
		mIsExit = true;
		mState = STATE_IDLE;
		// resetPlayer();
		send_msg_ui(MSG_MEDIA_END);

		return true;
	}

	/*
	 * @Override public boolean onInfo (MediaPlayer mp, int what, int extra) {
	 * Log.e("mediaPlayer", "onInfo"); switch (what) { case
	 * MediaPlayer.MEDIA_INFO_BUFFERING_START:
	 * send_msg_ui(MSG_MEDIA_BUFFERING_START); break; case
	 * MediaPlayer.MEDIA_INFO_BUFFERING_END:
	 * send_msg_ui(MSG_MEDIA_BUFFERING_END); break; } return true; }
	 */

}
