package com.fullteem.yueba.app.singer;

import android.view.View;
import android.view.View.OnClickListener;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.singer.VideoPlayerView.IPlayerCallback;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.widget.TopTitleView;

public class SimpleVideoPlayActivity extends BaseActivity implements
		IPlayerCallback {
	private VideoPlayerView viewVideoPlay;
	private TopTitleView topTitle;

	public SimpleVideoPlayActivity() {
		super(R.layout.activity_simple_video_play);
	}

	@Override
	public void initViews() {
		initTopTitle();
		viewVideoPlay = (VideoPlayerView) findViewById(R.id.viewVideoPlay);

	}

	private void initTopTitle() {
		topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppContext.mp.release();
				finish();
			}
		});
		// topTitle.showCenterText(getString(R.string.play), null);

		topTitle.setVisibility(View.GONE);

	}

	@Override
	public void initData() {
		String logoPath = getIntent().getStringExtra("logoUrl");
		String videoPath = getIntent().getStringExtra("videoUrl");

		LogUtil.printSingerLog("logoUrl:" + logoPath + "\n videoUrl:"
				+ videoPath);
		if (!viewVideoPlay.isInited()) {
			viewVideoPlay.init(SimpleVideoPlayActivity.this,
					SimpleVideoPlayActivity.this, videoPath, logoPath);

		}

	}

	@Override
	protected void onDestroy() {
		if (viewVideoPlay != null)
			viewVideoPlay.onStop();
		super.onDestroy();
	}

	@Override
	public void bindViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExitPlay() {
		viewVideoPlay.onStop();
		this.finish();
	}

}
