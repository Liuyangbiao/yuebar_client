package com.fullteem.yueba.app.singer;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.VideoPlayerHelper;

public class VideoPlayerView extends LinearLayout implements Callback,
		OnClickListener {
	private SurfaceView surfaceView;
	/** 前景图 */
	private ImageView ivVideoBg;
	/** 屏幕播放/暂停按钮 */
	private Button btnPlayAndPause;
	/** 点下时加载ing进度条 */
	private ProgressBar pbVideoLoading;
	/** 底部控制栏 */
	private RelativeLayout rlControlBar;
	/** 控制栏播放/暂停按钮 */
	private Button btnPlay;
	/** 控制栏全屏/取消全屏按钮 */
	private Button btnFullScreen;
	/** 控制栏进度条 */
	private SeekBar skbProgress;
	/** 控制栏当前播放时间 */
	private TextView tvCurTime;
	/** 控制栏视频总时间 */
	private TextView tvTotalTime;

	/** 是否显示中间按钮 */
	private boolean isShowCenterButton = true;

	/** 是否加载中 */
	private boolean isLoading;

	/** 用于接收播放器消息 */
	private final Messenger mPlayerMsg = new Messenger(new IncomingHandler());

	/** 播放界面图高度 */
	private final int VIEW_HEIGHT = 190;
	/** 播放界面图标资源 */
	private final int ICON_CONTROLBAR_PLAY = R.drawable.player_play;
	private final int ICON_CONTROLBAR_PAUSE = R.drawable.player_pause;
	private final int ICON_PLAY = R.drawable.btn_play;
	private final int ICON_PAUSE = R.drawable.btn_pause;
	private final int ICON_RESCREEN = R.drawable.player_rescreen;
	private final int ICON_FULLSCREEN = R.drawable.player_fullscreen;
	private final int ICON_EXIT_PLAY = R.drawable.btn_exit_play;

	/** 是否全屏模式，true为全屏，false为普通 */
	private boolean mIsFullScreen = false;
	/** 屏幕方向改变监听器 */
	private OrientationEventListener mOrientationListener;
	private boolean mClick = false; // 是否点击
	/** 点击进入全屏 */
	private boolean mClickLand = true;
	/** 点击进入竖屏 */
	private boolean mClickPort = true;
	/** 持续时间初始化 */
	private boolean mDurationInited = false;
	/** 屏幕方向改变接口 */
	private IFullScreenEvent mFullScreenEvent = null;
	
	/** auto play with full screen mode*/
	private IPlayerCallback mPlayerCallback = null;
	
	/** 播放资源的路径 */
	private String playUri;
	private Object waitObj = new Object();

	/** 进度条更新timer */
	private Timer mTimer;
	private skbProgressTimerTask mTimerTask;
	private Activity mParentActivity;
	private Context mContext;

	private boolean isInited;// 是否初始化

	public VideoPlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		this.mContext = context;
		View view = View.inflate(context, R.layout.view_video_palyer, null);

		surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
		SurfaceHolder surfaceHolder;
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		ivVideoBg = (ImageView) view.findViewById(R.id.iv_videoBg);
		btnPlayAndPause = (Button) view.findViewById(R.id.btn_playAndPause);
		pbVideoLoading = (ProgressBar) view.findViewById(R.id.pb_videoLoading);
		rlControlBar = (RelativeLayout) view.findViewById(R.id.rl_controlBar);
		btnPlay = (Button) view.findViewById(R.id.btn_play);
		btnFullScreen = (Button) view.findViewById(R.id.btn_fullScreen);
		skbProgress = (SeekBar) view.findViewById(R.id.skb_progress);
		tvCurTime = (TextView) view.findViewById(R.id.tv_curTime);
		tvTotalTime = (TextView) view.findViewById(R.id.tv_totalTime);

		pbVideoLoading.setVisibility(View.GONE);
		rlControlBar.setVisibility(View.GONE);

		LayoutParams layoutParams = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				DisplayUtils.dp2px(context, VIEW_HEIGHT));
		addView(view, layoutParams);
	}

	public VideoPlayerView(Context context) {
		this(context, null);
	}

	/**
	 * 初始化播放
	 * 
	 * @param parentActivity
	 * @param fullScreenEvent
	 *            全屏切换监听
	 * @param playUri
	 *            播放地址
	 * @param logoUrl
	 *            logo地址
	 */
	public void init(Activity parentActivity, IFullScreenEvent fullScreenEvent,
			String playUri, String logoUrl) {
		this.mParentActivity = parentActivity;
		this.mFullScreenEvent = fullScreenEvent;
		this.playUri = playUri;
		initView(logoUrl);
		if (!(parentActivity == null))
			startOrientationListener();
		isInited = true;
	}
	
	/**
	 * 初始化播放
	 * 
	 * @param parentActivity
	 * @param playerCallback
	 *            auto play with full screen
	 * @param playUri
	 *            播放地址
	 * @param logoUrl
	 *            logo地址
	 */
	public void init(Activity parentActivity, IPlayerCallback playerCallback,
			String playUri, String logoUrl) {
		this.mParentActivity = parentActivity;
		this.mPlayerCallback = playerCallback;
		this.playUri = playUri;
		
		btnFullScreen.setBackgroundResource(ICON_EXIT_PLAY);
		
		initView(logoUrl);

		isInited = true;
	}
	
	private void initView(String logoUrl) {
		skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());

		if (mTimerTask != null)
			mTimerTask.cancel();

		mTimer = new Timer();
		mTimerTask = new skbProgressTimerTask();
		mTimer.schedule(mTimerTask, 0, 1000);
		//
		btnPlayAndPause.setBackgroundResource(ICON_PLAY);
		btnPlay.setBackgroundResource(ICON_CONTROLBAR_PLAY);

		btnPlay.setOnClickListener(this);
		if (isShowCenterButton)
			btnPlayAndPause.setOnClickListener(this);
		else
			btnPlayAndPause.setVisibility(View.GONE);
		btnFullScreen.setOnClickListener(this);

		if (mParentActivity == null)
			btnFullScreen.setVisibility(View.INVISIBLE);

		if (!TextUtils.isEmpty(logoUrl)) {
			ivVideoBg.setBackgroundColor(Color.TRANSPARENT);
			ImageLoaderUtil.getImageLoader().displayImage(
					DisplayUtils.getAbsolutelyUrl(logoUrl),
					ivVideoBg,
					ImageLoaderUtil.getOptions(
							R.drawable.img_loading_default_big,
							R.drawable.img_loading_empty_big,
							R.drawable.img_loading_fail_big));
		}

		surfaceView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isLoading)
					return;
				rlControlBar
						.setVisibility(rlControlBar.getVisibility() == View.VISIBLE ? View.GONE
								: View.VISIBLE);
			}
		});
	}

	/**
	 * 加载结束
	 */
	private void uiEndLoading() {
		isLoading = false;
		rlControlBar.setVisibility(View.VISIBLE);
		pbVideoLoading.setVisibility(View.GONE);
	}

	/**
	 * 加载开始
	 */
	private void uiStartLoading() {
		isLoading = true;
		rlControlBar.setVisibility(View.GONE);
		pbVideoLoading.setVisibility(View.VISIBLE);
	}

	// 设置/取消全屏
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setFullScreen(boolean full) {
		WindowManager.LayoutParams attrs = mParentActivity.getWindow()
				.getAttributes();

		if (full) {
			mParentActivity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			if (mFullScreenEvent != null)
				mFullScreenEvent.preFullScreen();

			attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			mParentActivity.getWindow().setAttributes(attrs);

			RelativeLayout rl = (RelativeLayout) findViewById(R.id.videoScreen);
			ViewGroup.LayoutParams laParams = rl.getLayoutParams();
			laParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
			rl.setLayoutParams(laParams);

			if (this.mPlayerCallback != null){
				btnFullScreen.setBackgroundResource(ICON_EXIT_PLAY);
			}else{
				btnFullScreen.setBackgroundResource(ICON_RESCREEN);
			}
			

			mIsFullScreen = true;
		} else {
			mParentActivity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			if (mFullScreenEvent != null) {
				mFullScreenEvent.preRestore();
			}

			attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
			mParentActivity.getWindow().setAttributes(attrs);

			// RelativeLayout rl = (RelativeLayout)
			// findViewById(R.id.videoScreen);
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.videoScreen);
			ViewGroup.LayoutParams laParams = rl.getLayoutParams();
			laParams.height = DisplayUtils.dp2px(mContext, VIEW_HEIGHT);
			rl.setLayoutParams(laParams);

			btnFullScreen.setBackgroundResource(ICON_FULLSCREEN);

			mIsFullScreen = false;
		}
	}

	public interface IFullScreenEvent {

		/** 即将进入全屏时调用 */
		void preFullScreen();

		/** 恢复普通屏幕之后调用 */
		void preRestore();
	}
	
	public interface IPlayerCallback{
		void onExitPlay();
	}

	public boolean isFullScreen() {
		return mIsFullScreen;
	}

	// 全屏模式切换
	public void OnFullScreenClick() {
		mClick = true;
		if (!mIsFullScreen) {
			mClickLand = false;
			setFullScreen(true);

		} else {
			if (mPlayerCallback != null){
				mPlayerCallback.onExitPlay();
			}else{
				setFullScreen(false);
				mClickPort = false;
			}
		}
	}

	public Messenger getPlayerMsg() {
		return mPlayerMsg;
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		try {
			AppContext.mp.init(mPlayerMsg, surfaceHolder, mContext);
		} catch (Exception e) {
			LogUtil.LogError("VideoPlayerView", "mediaPlayer", null);
		}
		
		if (this.mPlayerCallback != null){
			OnFullScreenClick();
			OnPlayClick();
			return;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

	}

	private class skbProgressTimerTask extends TimerTask {
		@Override
		public void run() {
			if (AppContext.mp.getState() == VideoPlayerHelper.STATE_PLAYING
					&& skbProgress.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};

	android.os.Handler handleProgress = new android.os.Handler() {
		@Override
		public void handleMessage(Message msg) {
			int position = AppContext.mp.getCurrentPosition();
			if (position < 0)
				return;
			int duration = AppContext.mp.getDuration();

			if (duration > 0) {
				if (!mDurationInited) {
					mDurationInited = true;
					Time t = new Time();
					t.second = duration / 1000;
					t.normalize(false);

					tvTotalTime.setText(t.format("%T"));
				}

				Time tcur = new Time();
				tcur.second = position / 1000;
				tcur.normalize(false);

				tvCurTime.setText(tcur.format("%T"));

				long pos = skbProgress.getMax() * position / duration;
				skbProgress.setProgress((int) pos);
			}
		};
	};

	// 处理播放按钮按下的事件
	public void OnPlayClick() {
		if (ivVideoBg.getVisibility() != View.GONE)
			ivVideoBg.setVisibility(View.GONE);

		switch (AppContext.mp.getState()) {
		// 正在播放，则切换到暂停
		case VideoPlayerHelper.STATE_PLAYING:
			if (isShowCenterButton) {
				btnPlayAndPause.setVisibility(View.VISIBLE);
				btnPlayAndPause.setBackgroundResource(ICON_PLAY);
			}
			AppContext.mp.pause();
			break;

		// 空闲则开始播放
		case VideoPlayerHelper.STATE_IDLE:
			if (btnPlayAndPause.getVisibility() != View.GONE)
				btnPlayAndPause.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(playUri)) {
				AppContext.mp.playUrl(playUri);
			}
			break;

		// 暂停中，则恢复播放
		case VideoPlayerHelper.STATE_PAUSE:
			if (btnPlayAndPause.getVisibility() != View.GONE)
				btnPlayAndPause.setVisibility(View.GONE);
			AppContext.mp.play();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btnPlay || v == btnPlayAndPause) {
			OnPlayClick();
			return;
		} else if (v == btnFullScreen) {
			OnFullScreenClick();
			return;
		}
	}

	private class IncomingHandler extends android.os.Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case VideoPlayerHelper.MSG_MEDIA_PLAYING:
				uiEndLoading();
				btnPlay.setBackgroundResource(ICON_CONTROLBAR_PAUSE);
				break;
			case VideoPlayerHelper.MSG_MEDIA_PAUSE:
				btnPlay.setBackgroundResource(ICON_CONTROLBAR_PLAY);
				break;
			case VideoPlayerHelper.MSG_MEDIA_PREPARING:
			case VideoPlayerHelper.MSG_MEDIA_BUFFERING_START:
				uiStartLoading();
				break;
			case VideoPlayerHelper.MSG_MEDIA_END:
				btnPlay.setBackgroundResource(ICON_CONTROLBAR_PLAY);
				uiEndLoading();
				ivVideoBg.setVisibility(View.VISIBLE);
				btnPlay.setVisibility(View.VISIBLE);
				break;
			case VideoPlayerHelper.MSG_MEDIA_BUFFERING_END:
				uiEndLoading();
				break;
			case VideoPlayerHelper.MSG_MEDIA_FULLSCREEN_TRIGGER:
				OnFullScreenClick();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	private class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

			this.progress = progress * AppContext.mp.getDuration()
					/ seekBar.getMax();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			AppContext.mp.seekTo(progress);
		}
	}

	private final void startOrientationListener() {
		mOrientationListener = new OrientationEventListener(mParentActivity) {
			@Override
			public void onOrientationChanged(int rotation) {
				// 设置竖屏
				if (((rotation >= 0) && (rotation <= 30)) || (rotation >= 330)) {
					if (mClick) {
						if (mIsFullScreen && !mClickLand) {
							return;
						} else {
							mClickPort = true;
							mClick = false;
							mIsFullScreen = false;
						}
					} else {
						if (mIsFullScreen) {
							setFullScreen(false);
							mIsFullScreen = false;
							mClick = false;
						}
					}
				}
				// 设置横屏
				else if (((rotation >= 230) && (rotation <= 310))) {
					if (mClick) {
						if (!mIsFullScreen && !mClickPort) {
							return;
						} else {
							mClickLand = true;
							mClick = false;
							mIsFullScreen = true;
						}
					} else {
						if (!mIsFullScreen) {
							setFullScreen(true);
							mIsFullScreen = true;
							mClick = false;
						}
					}
				}
			}
		};
		mOrientationListener.enable();
	}

	/**
	 * 资源释放，如果启用了屏幕方向改变监听则禁用
	 */
	public void onStop() {
		AppContext.mp.release();
		if (mOrientationListener != null)
			mOrientationListener.disable();
		isInited = false;
	}

	/**
	 * 设置中间播放按钮是否显示
	 * 
	 * @param isShow
	 *            treu显示， false不显示
	 */
	public void setIsShowCenterButton(boolean isShow) {
		this.isShowCenterButton = isShow;
		if (!isShow && btnPlayAndPause != null
				&& btnPlayAndPause.getVisibility() != View.GONE)
			btnPlayAndPause.setVisibility(View.GONE);
	}

	/**
	 * 视频是否初始化
	 * 
	 * @return
	 */
	public boolean isInited() {
		return isInited;
	}

	public void setInited(boolean isInited) {
		this.isInited = isInited;
	}

}
