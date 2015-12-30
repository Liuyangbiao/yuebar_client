package com.fullteem.yueba.app.ui;

import android.view.View;
import android.view.View.OnClickListener;

import com.fullteem.yueba.R;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2015年2月2日&emsp;下午4:53:45
 * @use 等级、魅力值说明界面
 */
public class LevelAndCharmExplainActivity extends BaseActivity {

	public LevelAndCharmExplainActivity() {
		super(R.layout.activity_level_charm_explain);
	}

	@Override
	public void initViews() {
		initTopTitle();
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.levelAndCharmExplain), null);
	}

	@Override
	public void initData() {
	}

	@Override
	public void bindViews() {
	}

}
