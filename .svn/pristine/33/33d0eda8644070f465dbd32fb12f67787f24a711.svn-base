package com.fullteem.yueba.app.ui;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.fullteem.yueba.R;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.presentmodel.ChangePasswdPresentModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.net.md5.MD5Util;
import com.fullteem.yueba.util.CheckUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.widget.TimeCount;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月4日&emsp;下午2:54:56
 * @use 修改密码主页面
 */
public class ChangePasswdActivity extends BaseActivity {
	private ChangePasswdPresentModel presentMode;
	private TopTitleView topTitle;
	private Button btnValidNum;
	private Button btnChangePasswd;
	private EventListener mListener;

	private TimeCount time;
	private EventHandler handler;

	public ChangePasswdActivity() {
		super(-1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		BinderFactory binderFactory = new BinderFactoryBuilder().build();
		ViewBinder vb = binderFactory.createViewBinder(this, true);
		presentMode = new ChangePasswdPresentModel();
		View rootView = vb.inflateAndBind(R.layout.activity_changepwd,
				presentMode);
		setContentView(rootView);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initViews() {
		topTitle = (TopTitleView) findViewById(R.id.top_title);
		btnValidNum = (Button) findViewById(R.id.btn_validNum);
		btnChangePasswd = (Button) findViewById(R.id.btn_changePasswd);
		initTopTitle();
	}

	private void initTopTitle() {
		topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.perssonal_changepwd), null);
	}

	@Override
	public void initData() {
		mListener = new EventListener();

		// 初始化短信验证
		SMSSDK.initSDK(this, GlobleConstant.APPKEY, GlobleConstant.APPSECRET);
		handler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					// 提交验证码
					if (result == SMSSDK.RESULT_COMPLETE) {
						LogUtil.LogDebug(getClass().getName(), "短信验证成功！", null);
						HttpRequest.getInstance(null).getChangePwd(
								presentMode.getPhoneNum(),
								MD5Util.string2MD5(presentMode.getOldPasswd()),
								MD5Util.string2MD5(presentMode.getNewPasswd()),
								new CustomAsyncResponehandler() {

									@Override
									public void onSuccess(ResponeModel baseModel) {
										if (baseModel != null) {
											if (baseModel.isStatus()) {
												showToastInThread(getString(R.string.changepwd_success));
												finish();
											} else {
												showToastInThread(getString(R.string.changepwd_fail)
														+ "\n"
														+ baseModel.getJson());
											}
										}
										presentMode
												.setErrorSate(getString(R.string.changepwd_fail));
									}

									@Override
									public void onStart() {
										// btnChangePasswd.setEnabled(false);
										// btnChangePasswd.setSelected(true);
									}

									@Override
									public void onFinish() {
										// time.onFinish();
										// time.cancel();
										// btnChangePasswd.setEnabled(true);
										// btnChangePasswd.setSelected(false);
									}

									@Override
									public void onFailure(Throwable error,
											String content) {
										showToastInThread(getString(R.string.changepwd_fail)
												+ "\n" + error.toString());
									}
								});
					}

					else {
						showToastInThread(getString(R.string.getidentifying_error));
					}
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					// 获取验证码成功后的执行动作
					LogUtil.LogDebug(getClass().getName(), "获取成功！", null);

				}
			}
		};
		SMSSDK.registerEventHandler(handler);

	}

	@Override
	public void bindViews() {
		btnValidNum.setOnClickListener(mListener);

		btnChangePasswd.setOnClickListener(mListener);
	}

	private class EventListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == btnValidNum) {
				if (time == null) {
					time = new TimeCount(60000, 1000) {
						@Override
						public void onFinish() {
							btnValidNum
									.setText(getString(R.string.getidentifying));
							btnValidNum.setClickable(true);
							btnValidNum.setSelected(false);
						}

						@Override
						public void onTick(long millisUntilFinished) {
							btnValidNum.setClickable(false);
							btnValidNum.setSelected(true);
							btnValidNum.setText(millisUntilFinished / 1000
									+ "秒");
						}
					};

				}
				if (TextUtils.isEmpty(presentMode.getPhoneNum())) {
					presentMode
							.setErrorSate(getResString(R.string.changepwd_mustnot_empty_phonenum));
				} else {
					presentMode.setErrorSate(null);

					String phoneNum = presentMode.getPhoneNum();
					if (StringUtils.isPhoneNum(phoneNum)) {
						SMSSDK.getVerificationCode("86", phoneNum);
						time.start();
					} else {
						showToast(getString(R.string.not_phoneNum));
						return;
					}
					return;
				}
			}
			if (v == btnChangePasswd) {
				if (TextUtils.isEmpty(presentMode.getOldPasswd())) {
					presentMode
							.setErrorSate(getString(R.string.changepwd_mustnot_empty_oldpwd));
					return;
				}
				if (TextUtils.isEmpty(presentMode.getNewPasswd())
						|| TextUtils.isEmpty(presentMode.getNewPasswdAgain())) {
					presentMode
							.setErrorSate(getString(R.string.changepwd_mustnot_empty_newpwd));
					return;
				}
				if (!CheckUtil.checkUserPassword(presentMode.getNewPasswd())) {
					showToast(getString(R.string.psd_not_legal));
					return;
				}
				if (!presentMode.getNewPasswd().equals(
						presentMode.getNewPasswdAgain())) {
					presentMode
							.setErrorSate(getString(R.string.changepwd_differ_newpwd));
					return;
				}
				if (TextUtils.isEmpty(presentMode.getPhoneNum())) {
					presentMode
							.setErrorSate(getString(R.string.changepwd_mustnot_empty_phonenum));
					return;
				}
				if (TextUtils.isEmpty(presentMode.getValidNum())) {
					presentMode
							.setErrorSate(getString(R.string.changepwd_mustnot_empty_validnum));
					return;
				}
				presentMode.setErrorSate(null);

				// TODO 发送修改密码请求
				SMSSDK.submitVerificationCode("86", presentMode.getPhoneNum(),
						presentMode.getValidNum());

			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		SMSSDK.unregisterEventHandler(handler);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SMSSDK.registerEventHandler(handler);
	}
}
