package com.fullteem.yueba.entry;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.CommonModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.net.md5.MD5Util;
import com.fullteem.yueba.util.CheckUtil;
import com.fullteem.yueba.widget.TimeCount;
import com.fullteem.yueba.widget.TopTitleView;

public class ForgetActivity extends BaseActivity {
	private TopTitleView toptitle;
	private EditText edtPhoneNum, edtIdentifying, edtInputPsd, edtInputAgain;
	private TextView tvGetidentifying;
	private TimeCount time;
	private EventHandler handler;
	private Button btnSure;

	public ForgetActivity() {
		super(R.layout.activity_forgetpsd);
	}

	@Override
	public void initViews() {
		edtPhoneNum = (EditText) findViewById(R.id.edtPhoneNum);
		edtIdentifying = (EditText) findViewById(R.id.edtIdentifying);
		edtInputPsd = (EditText) findViewById(R.id.edtInputPsd);
		edtInputAgain = (EditText) findViewById(R.id.edtInputAgain);
		tvGetidentifying = (TextView) findViewById(R.id.tvGetidentifying);
		btnSure = (Button) findViewById(R.id.btnSure);
	}

	@Override
	public void initData() {
		btnSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doForgetPsd();
			}
		});
		// 初始化短信验证
		SMSSDK.initSDK(this, GlobleConstant.APPKEY, GlobleConstant.APPSECRET);
		tvGetidentifying.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phoneNum = edtPhoneNum.getText().toString();
				if (!TextUtils.isEmpty(phoneNum)) {
					if (CheckUtil.checkPhoneNum(phoneNum))
						SMSSDK.getVerificationCode("86", phoneNum);
					else {
						showToast(getString(R.string.not_phoneNum));
						return;
					}
				} else {
					showToast(getString(R.string.phoneNum_null));
					return;
				}
				time.start();
			}
		});

		// 构造CountDownTimer对象
		time = new TimeCount(60000, 1000) {
			@Override
			public void onFinish() {
				tvGetidentifying.setText(getString(R.string.getidentifying));
				tvGetidentifying.setClickable(true);
			}

			@Override
			public void onTick(long millisUntilFinished) {
				tvGetidentifying.setClickable(false);
				tvGetidentifying.setText(millisUntilFinished / 1000 + "秒");
			}

		};

		handler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					// 提交验证码
					if (result == SMSSDK.RESULT_COMPLETE) {
						Log.e("register", "验证成功");
						JSONObject ob = new JSONObject();
						ob.put("userMobile", edtPhoneNum.getText().toString());
						ob.put("userPassword", MD5Util.string2MD5(edtInputPsd
								.getText().toString()));
						HttpRequestFactory.getInstance().getRequest(
								Urls.forgetpsd_action, ob,
								new AsyncHttpResponseHandler() {

									// 成功
									@Override
									public void onSuccess(String content) {
										super.onSuccess(content);
										CommonModel cm = new CommonModel();
										cm = JSON.parseObject(content,
												CommonModel.class);
										if (cm.getCode() != null
												&& "200".equalsIgnoreCase(cm
														.getCode())) {
											showToastInThread(getString(R.string.newpsd_submit_success));
											dismissLoadingDialog();
											finish();
										} else {
											showToastInThread(getString(R.string.newpsd_submit_fail));
										}
									}

									// 失败
									@Override
									public void onFailure(Throwable error,
											String content) {
										showToastInThread(content);
									};

									@Override
									public void onFinish() {
										dismissLoadingDialog();
									};

								}

						);
					} else {
						showToastInThread(getString(R.string.getidentifying_error));
						dismissLoadingDialog();
					}
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					// 获取验证码成功后的执行动作
					Log.e("register", "获取成功");
					dismissLoadingDialog();

				}
			}
		};

		SMSSDK.registerEventHandler(handler);

	}

	/**
	 * 忘记密码
	 */
	private void doForgetPsd() {
		showLoadingDialog();
		if (!TextUtils.isEmpty(edtInputPsd.getText())
				&& !TextUtils.isEmpty(edtInputAgain.getText())) {
			if (edtInputPsd.getText().toString()
					.equalsIgnoreCase(edtInputAgain.getText().toString())) {
				SMSSDK.submitVerificationCode("86", edtPhoneNum.getText()
						.toString(), edtIdentifying.getText().toString());
			} else {
				showToast(getString(R.string.psd_not_same));
			}
		} else {
			showToast(getString(R.string.please_input_all));
		}
	}

	@Override
	public void bindViews() {
		toptitle = (TopTitleView) findViewById(R.id.toptile);
		toptitle.showLeftImag(R.drawable.back, new OnClickListener() {

			@Override
			public void onClick(View v) {
				ForgetActivity.this.finish();
			}
		});
		toptitle.showCenterText(getString(R.string.forget), null);
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
