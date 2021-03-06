package com.fullteem.yueba.entry;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.util.BitmapManager;
import com.fullteem.yueba.widget.TopTitleView;

public class RegisterAgreementActivity extends BaseActivity {

	private WebView mWebView;
	private TopTitleView topTitle;

	public RegisterAgreementActivity() {
		super(R.layout.activity_register_agreement);
	}

	@SuppressLint("NewApi")
	@Override
	public void initViews() {
		
		initTopTitle();
		
		mWebView = (WebView) findViewById(R.id.wb_agreement_content);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDefaultTextEncodingName("gb2312");
        //找到Html文件，也可以用网络上的文件  
		mWebView.loadUrl("file:///android_asset/agreementcontent.html"); 

	}

	private void initTopTitle() {
		
		topTitle = (TopTitleView) findViewById(R.id.toptile);
		topTitle.showCenterButton(getString(R.string.user_agreement), null, null,
				null);

		topTitle.showLeftImag(
				BitmapManager.getBitmapFromDrawable(this, R.drawable.back),
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
	}

	@Override
	public void initData() {

	}

	@Override
	public void bindViews() {

	}

}
