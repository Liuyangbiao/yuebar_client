package com.fullteem.yueba.app.ui;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow.OnDismissListener;

import com.fullteem.yueba.R;
import com.fullteem.yueba.model.presentmodel.ExchangeScorePopWPresentModel;
import com.fullteem.yueba.model.presentmodel.ScoreQueryPresentModel;
import com.fullteem.yueba.widget.HintContentPopWindow;
import com.fullteem.yueba.widget.ScoreExchangePopWindow;
import com.fullteem.yueba.widget.ScoreExchangePopWindow.OnPayButtonClickListener;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月9日&emsp;下午6:13:48
 * @use 积分查询页面
 */
public class ScoreQueryActivity extends BaseActivity {

	private ScoreQueryPresentModel presentMode;
	private int surplusScore = -1; // 剩余积分
	private int surplusGold = -1; // 剩余金币
	private ScoreExchangePopWindow mScoreExchangePopWindow;
	private HintContentPopWindow mHintContentPopWindow;

	public ScoreQueryActivity() {
		super(-1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		BinderFactory binderFactory = new BinderFactoryBuilder().build();
		ViewBinder vb = binderFactory.createViewBinder(this, true);
		presentMode = new ScoreQueryPresentModel(this);
		View rootView = vb.inflateAndBind(R.layout.activity_score_query,
				presentMode);
		setContentView(rootView);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initViews() {
		initTopTitle();
	}

	@Override
	public void initData() {
		mScoreExchangePopWindow = mScoreExchangePopWindow == null ? new ScoreExchangePopWindow(
				ScoreQueryActivity.this, new ExchangeScorePopWPresentModel(
						ScoreQueryActivity.this)) : mScoreExchangePopWindow;
		mHintContentPopWindow = mHintContentPopWindow == null ? new HintContentPopWindow(
				ScoreQueryActivity.this) : mHintContentPopWindow;
		mHintContentPopWindow.setCenterGravity(Gravity.LEFT);
		mHintContentPopWindow.getPopupWindow().setOnDismissListener(
				new OnDismissListener() {
					@Override
					public void onDismiss() {

						// TODO 重新从服务器刷新积分、金币及当前页面
					}
				});

		this.surplusScore = 5000;
		mScoreExchangePopWindow.setNowScore(surplusScore);
	}

	@Override
	public void bindViews() {
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.perssonal_score_query), null);
		topTitle.showRightText(getString(R.string.score_exchange), null,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (surplusScore == -1) {
							showToast(getString(R.string.hint_dataLoadingSuccessAgain));
							return;
						}
						if (surplusScore < 1000) {
							showToast(getString(R.string.hint_scoreExchange));
							return;
						}

						mScoreExchangePopWindow
								.showWindow(new OnPayButtonClickListener() {
									@Override
									public void onPayClick(
											View v,
											ExchangeScorePopWPresentModel presentModel) {
										if (TextUtils.isEmpty(presentModel
												.getExchangeScore())
												|| Integer.valueOf(presentModel
														.getExchangeScore()) > Integer
														.valueOf(presentModel
																.getNowScore())
												|| Integer.valueOf(presentModel
														.getExchangeScore()) < 1000) { // 1000起兑
											presentModel
													.setErrorState(getString(R.string.hint_inputValidValue));
											return;
										}
										presentModel.setExchangeGold(""
												+ (Integer.valueOf(presentModel
														.getExchangeScore()) / 10));

										// TODO 积分兑换金币接口
										mScoreExchangePopWindow
												.getPopupWindow().dismiss();

										String strHint = getString(R.string.hint_scoreExchangeSusscess);
										String hintContent = String.format(
												strHint,
												presentModel.getExchangeGold(),
												Integer.valueOf(presentModel
														.getExchangeGold())
														+ surplusGold);
										int startIndex = hintContent
												.indexOf("为") + 1;
										SpannableStringBuilder tvContent = new SpannableStringBuilder(
												hintContent);
										tvContent
												.setSpan(
														new ForegroundColorSpan(
																getResources()
																		.getColor(
																				R.color.girl_red)),
														startIndex,
														tvContent.length(),
														Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

										mHintContentPopWindow
												.showWindow(tvContent);
									}
								});
					}
				});
	}
}
