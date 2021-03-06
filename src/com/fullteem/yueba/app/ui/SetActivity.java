package com.fullteem.yueba.app.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.fullteem.yueba.R;
import com.fullteem.yueba.download.UpdateManager;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.VersionUpdateModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.CheckUtil;
import com.fullteem.yueba.util.SharePreferenceUtil;
import com.fullteem.yueba.widget.HintConfirmationPopWindow;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月10日&emsp;下午5:05:16
 * @use 类说明
 */
public class SetActivity extends BaseActivity {
	private Button btnSetVoice, btnUpdate, btnLevelCharmExplain;
	private CheckBox ckbSetVoice, ckbSetVibrate;
	private RelativeLayout rlSetViocePrompt;
	private TextView tvSetViocePrompt;
	private LinearLayout llSetVoiceControl;
	private Drawable[] drawableArrow; // drawableArrow[0]
										// down_arrow,drawableArrow[1] up_arrow,
	private EventListener mListener;

	/**
	 * 通知声音
	 * 
	 * @author jun
	 * 
	 */
	public enum NotifyRing {
		FollowSystem, Beryllium, Krypton, Mira, OnTheHunt, SpaceSeed, Tejat
	}

	public SetActivity() {
		super(R.layout.activity_set);
	}

	@Override
	public void initViews() {
		initTopTitle();
		btnSetVoice = (Button) findViewById(R.id.btn_setVoice);
		btnUpdate = (Button) findViewById(R.id.btn_update);
		btnLevelCharmExplain = (Button) findViewById(R.id.btn_levelCharmExplain);
		ckbSetVoice = (CheckBox) findViewById(R.id.ckb_setVoice);
		ckbSetVibrate = (CheckBox) findViewById(R.id.ckb_setVibrate);
		rlSetViocePrompt = (RelativeLayout) findViewById(R.id.rl_setViocePrompt);
		tvSetViocePrompt = (TextView) findViewById(R.id.tv_setViocePrompt);
		llSetVoiceControl = (LinearLayout) findViewById(R.id.ll_setvoiceControl);

		ckbSetVoice.setChecked(SharePreferenceUtil.getInstance(this)
				.getSettingMsgSound());
		ckbSetVibrate.setChecked(SharePreferenceUtil.getInstance(this)
				.getSettingMsgVibrate());

	}

	@Override
	public void initData() {
		drawableArrow = new Drawable[2];
		drawableArrow[0] = getResources().getDrawable(
				R.drawable.down_arrow_icon);
		drawableArrow[1] = getResources().getDrawable(R.drawable.up_arrow_icon);
		drawableArrow[0].setBounds(0, 0, drawableArrow[0].getMinimumWidth(),
				drawableArrow[0].getMinimumHeight());
		drawableArrow[1].setBounds(0, 0, drawableArrow[1].getMinimumWidth(),
				drawableArrow[1].getMinimumHeight());

		mListener = new EventListener();
	}

	@Override
	public void bindViews() {
		// 初始默认声音控制打开，设置声音背景为灰色
		btnSetVoice.setBackgroundResource(R.drawable.content_top_bg);
		btnSetVoice.setOnClickListener(mListener);
		btnUpdate.setOnClickListener(mListener);
		btnLevelCharmExplain.setOnClickListener(mListener);
		ckbSetVoice.setOnCheckedChangeListener(mListener);
		ckbSetVibrate.setOnCheckedChangeListener(mListener);
		tvSetViocePrompt.setOnClickListener(mListener);
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.perssonal_set), null);
	}

	private class EventListener implements OnClickListener,
			OnCheckedChangeListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_setVoice:
				if (llSetVoiceControl.getVisibility() == View.VISIBLE) {
					btnSetVoice.setCompoundDrawables(
							btnSetVoice.getCompoundDrawables()[0], null,
							drawableArrow[0], null);
					llSetVoiceControl.setVisibility(View.GONE);
					btnSetVoice
							.setBackgroundResource(R.drawable.date_item_content);
				} else if (llSetVoiceControl.getVisibility() == View.GONE) {
					btnSetVoice.setCompoundDrawables(
							btnSetVoice.getCompoundDrawables()[0], null,
							drawableArrow[1], null);
					llSetVoiceControl.setVisibility(View.VISIBLE);
					btnSetVoice
							.setBackgroundResource(R.drawable.content_top_bg);
				}
				break;
			case R.id.btn_levelCharmExplain:
				startActivity(new Intent(SetActivity.this,
						LevelAndCharmExplainActivity.class));
				break;
			/*
			 * case R.id.tv_setViocePrompt: String[] vioceArr =
			 * getResources().getStringArray(R.array.vioce_list); List<String>
			 * vioceList = new LinkedList<String>(); for (String string :
			 * vioceArr) { vioceList.add(string); }
			 * ListObjectPopupWindow<String> listPopupWindow = new
			 * ListObjectPopupWindow<String>(SetActivity.this, vioceList);
			 * listPopupWindow.setWidth(tvSetViocePrompt.getWidth());
			 * listPopupWindow.showAsDropDown(tvSetViocePrompt, 0, 0,
			 * listPopupWindow.new IEventListener() {
			 * 
			 * @Override public void onItemClick(String obj, int positon) {
			 * tvSetViocePrompt.setText(obj);
			 * SharePreferenceUtil.getInstance(SetActivity
			 * .this).saveNotifyRing(positon == 1?NotifyRing.Beryllium : positon
			 * == 2? NotifyRing.Krypton : positon == 3 ? NotifyRing.Mira :
			 * positon == 4 ?NotifyRing.OnTheHunt : positon ==5 ?
			 * NotifyRing.SpaceSeed : positon == 6 ? NotifyRing.Tejat :
			 * NotifyRing.FollowSystem); Intent intent = new Intent();
			 * intent.setAction(GlobleConstant.UPDATE_EMCHAT_NOTIFY);
			 * sendBroadcast(intent);
			 * SharePreferenceUtil.getInstance(SetActivity
			 * .this).saveStringToShare("VibrateNotify", obj); } }); break ;
			 */
			case R.id.btn_update:
				HttpRequest.getInstance().checkVersion(
						new CustomAsyncResponehandler() {
							HintConfirmationPopWindow hintConfirmationPopWindow = new HintConfirmationPopWindow(
									SetActivity.this);;

							@Override
							public void onStart() {
								hintConfirmationPopWindow
										.setBottomButtonVisibility(View.GONE);
								hintConfirmationPopWindow.setCenterView(View
										.inflate(
												SetActivity.this,
												R.layout.base_load_empty_layout,
												null));
								hintConfirmationPopWindow.showWindow();
							}

							@Override
							public void onSuccess(ResponeModel baseModel) {
								hintConfirmationPopWindow.getPopupWindow()
										.dismiss();
								if (baseModel != null && baseModel.isStatus()) {
									hintConfirmationPopWindow = new HintConfirmationPopWindow(
											SetActivity.this);

									final VersionUpdateModel vpm = (VersionUpdateModel) baseModel
											.getResultObject();

									// 有版本升级
									if (vpm != null
											&& CheckUtil.isUpdate(
													SetActivity.this,
													vpm.getVersionNum())) {
										hintConfirmationPopWindow.setTitle(vpm
												.getAppTitle());
										hintConfirmationPopWindow
												.setCenterText(vpm
														.getAppContent());
										hintConfirmationPopWindow
												.setSureButton("取消",R.drawable.gray_shape_frame,null)
												.setCancelButton("更新",R.drawable.blue_shape_frame,new OnClickListener() {
															@Override
															public void onClick(View v) {
																hintConfirmationPopWindow.getPopupWindow().dismiss();
																System.out.println("更新apk路径： "+vpm.getAppUrl());
																UpdateManager manager = new UpdateManager(SetActivity.this, vpm.getAppUrl());
																manager.checkUpdate();
															}
														});
									}
									// 没有版本升级
									else {
										hintConfirmationPopWindow
												.setCenterText("暂无版本更新")
												.setSureButton(null, null)
												.setCancelButton(null, null);
									}
								}
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
								hintConfirmationPopWindow.getPopupWindow()
										.dismiss();
								hintConfirmationPopWindow = new HintConfirmationPopWindow(
										SetActivity.this);
								hintConfirmationPopWindow
										.setCenterText("网络发生错误")
										.setSureButton(null, null)
										.setCancelButton(null, null);
							}

							@Override
							public void onFinish() {
								hintConfirmationPopWindow.showWindow();
							}
						});

				break;
			}
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// 获取到EMChatOptions对象
			EMChatOptions options = EMChatManager.getInstance()
					.getChatOptions();

			switch (buttonView.getId()) {
			case R.id.ckb_setVoice:
				SharePreferenceUtil.getInstance(SetActivity.this)
						.setSettingMsgSound(ckbSetVoice.isChecked());
				options.setNoticeBySound(ckbSetVoice.isChecked());
				break;
			case R.id.ckb_setVibrate:
				SharePreferenceUtil.getInstance(SetActivity.this)
						.setSettingMsgVibrate(ckbSetVibrate.isChecked());
				options.setNoticedByVibrate(ckbSetVibrate.isChecked());
				break;
			}

			/*
			 * Intent intent = new Intent();
			 * intent.setAction(GlobleConstant.UPDATE_EMCHAT_NOTIFY);
			 * sendBroadcast(intent);
			 */
		}
	}

}
