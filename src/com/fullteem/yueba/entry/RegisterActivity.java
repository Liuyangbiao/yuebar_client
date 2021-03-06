package com.fullteem.yueba.entry;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.engine.upload.PhotoUtil;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.RegisterModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.UploadModel;
import com.fullteem.yueba.model.UploadModel.UploadDataModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.net.md5.MD5Util;
import com.fullteem.yueba.util.BitmapManager;
import com.fullteem.yueba.util.CheckUtil;
import com.fullteem.yueba.util.CuttingPicturesUtil;
import com.fullteem.yueba.util.FileUtils;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.CircleImageView;
import com.fullteem.yueba.widget.HintContentPopWindow;
import com.fullteem.yueba.widget.TimeCount;
import com.fullteem.yueba.widget.TopTitleView;

public class RegisterActivity extends BaseActivity {
	private EditText edtNickName;
	private EditText edtPhoneNum;
	private EditText edtIdentifying;
	private EditText edtInputPsd;
	private EditText edtInputAgain;
	private CheckBox checkboxBoy, checkboxGirl;
	private Button btnRegister;
	private TextView tvGetidentifying;
	private CircleImageView imgViewHeader;

	private TimeCount time;

	private EventHandler handler;

	private Bitmap userPhoto;

	/**
	 * 当前选中项,1：男 2：女
	 */
	private int currentChecked = -1;

	private TopTitleView topTitle;
	private String logoUrlStr;
	private TextView mRegisterAgreement;

	public RegisterActivity() {
		super(R.layout.activity_register);
	}

	@Override
	public void initViews() {
		topTitle = (TopTitleView) findViewById(R.id.toptile);
		imgViewHeader = (CircleImageView) findViewById(R.id.imgViewHeader);
		imgViewHeader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (CuttingPicturesUtil.isSDCardExisd()) {
					CuttingPicturesUtil.searhcAlbum(RegisterActivity.this,
							CuttingPicturesUtil.LOCAL_PHOTO);
				} else {
					showToast(getString(R.string.sdcard_not_exsit));
				}
			}
		});
		edtNickName = (EditText) findViewById(R.id.edtNickName);
		edtPhoneNum = (EditText) findViewById(R.id.edtPhoneNum);
		edtIdentifying = (EditText) findViewById(R.id.edtIdentifying);
		edtInputPsd = (EditText) findViewById(R.id.edtInputPsd);
		edtInputAgain = (EditText) findViewById(R.id.edtInputAgain);
		checkboxGirl = (CheckBox) findViewById(R.id.checkboxGirl);
		checkboxBoy = (CheckBox) findViewById(R.id.checkboxBoy);
		checkboxSelect();
		btnRegister = (Button) findViewById(R.id.btnRegister);
		mRegisterAgreement = (TextView) findViewById(R.id.tv_register_agreement);
		tvGetidentifying = (TextView) findViewById(R.id.tvGetidentifying);
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
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doRegister();
			}
		});
		
		mRegisterAgreement.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this, RegisterAgreementActivity.class);
				startActivity(intent);
				
			}
		});

	}

	@Override
	public void initData() {
		// 初始化短信验证
		SMSSDK.initSDK(this, GlobleConstant.APPKEY, GlobleConstant.APPSECRET);
		handler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					// 提交验证码
					if (result == SMSSDK.RESULT_COMPLETE) {
						Log.e("register", "验证成功");
						sendRegisterRequest();

					} else {
						showToastInThread(getString(R.string.getidentifying_error));
						dismissLoadingDialog();
					}
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					// 获取验证码成功后的执行动作
					Log.e("register", "获取成功");

				}
			}
		};

		SMSSDK.registerEventHandler(handler);
		this.logoUrlStr = "";
	}

	private void sendRegisterRequest() {
		LogUtil.printUserEntryLog("start to send register request");

		JSONObject ob = new JSONObject();
		ob.put("userName", edtNickName.getText().toString());

		// bill encrypt password
		ob.put("userPassword",
				MD5Util.string2MD5(edtInputPsd.getText().toString()));
		ob.put("userSex", String.valueOf(currentChecked));
		ob.put("userMobile", edtPhoneNum.getText().toString());
		ob.put("lng", appContext.getLocation().getLongitude());
		ob.put("lat", appContext.getLocation().getLatitude());

		PhotoUtil.printTrace("user logo url:" + logoUrlStr);
		ob.put("userLogoUrl", logoUrlStr);

		HttpRequestFactory.getInstance().doHttpsPostRequest(Urls.register_action, ob,
				new AsyncHttpResponseHandler() {

					// 注册成功
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

						RegisterModel rm = new RegisterModel();
						rm = JsonUtil.convertJsonToObject(content,
								RegisterModel.class);
						try {
							org.json.JSONObject json = new org.json.JSONObject(
									content);
							String code = json.getString("code");
							if (rm != null && rm.getCode() != null
									&& "200".equalsIgnoreCase(rm.getCode())) {
								LogUtil.printUserEntryLog("succeed to register");
								appContext.setUserInfo(rm.getResult());
								String id = appContext.getUserInfo()
										.getUserAccount();
								LogUtil.printUserEntryLog("id is:" + id);
								showToastInThread(getString(R.string.register_success)
										+ id + getString(R.string.login));
								finish();
							} else if (code != null
									&& "1004".equalsIgnoreCase(code)) {
								showToastInThread(getString(R.string.register_repeat));
							} else {
								LogUtil.printUserEntryLog("failed to register");
								showToastInThread(getString(R.string.register_fail));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					// 注册失败
					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.printUserEntryLog("on register failure");
						showToastInThread(content);
					};

					@Override
					public void onFinish() {
						LogUtil.printUserEntryLog("on register finish");
						dismissLoadingDialog();
					};

				});

	}

	@Override
	public void bindViews() {
		topTitle.showCenterButton(getString(R.string.register), null, null,
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

	private void checkboxSelect() {
		checkboxBoy.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					checkboxGirl.setChecked(false);
					currentChecked = 1;
					new HintContentPopWindow(RegisterActivity.this)
							.showWindow(getString(R.string.hint_sureGender));
				}
			}
		});

		checkboxGirl.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					checkboxBoy.setChecked(false);
					currentChecked = 2;
					new HintContentPopWindow(RegisterActivity.this)
							.showWindow(getString(R.string.hint_sureGender));
				}
			}
		});

	}

	private void doRegister() {

		// edtNickName.setText("nick");
		// edtInputPsd.setText("123456");
		// edtInputAgain.setText("123456");
		// currentChecked = 1;
		// edtPhoneNum.setText("13135687906");
		if (!TextUtils.isEmpty(edtNickName.getText())
				&& !TextUtils.isEmpty(edtPhoneNum.getText())
				&& !TextUtils.isEmpty(edtIdentifying.getText())
				&& !TextUtils.isEmpty(edtInputPsd.getText())
				&& !TextUtils.isEmpty(edtInputAgain.getText())) {
			if (currentChecked == -1) {
				showToast(getString(R.string.hint_selectGender));
				return;
			}

			if (!CheckUtil.checkUserPassword(edtInputPsd.getText().toString()
					.trim())) {
				showToast(getString(R.string.psd_not_legal));
				return;
			}

			if (edtInputPsd.getText().toString()
					.equalsIgnoreCase(edtInputAgain.getText().toString())) {
				showLoadingDialog();
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
	protected void onPause() {
		super.onPause();
		SMSSDK.unregisterEventHandler(handler);
		UmengUtil.onPageEnd(this,"SplashScreen");
	}

	@Override
	protected void onResume() {
		super.onResume();
		SMSSDK.registerEventHandler(handler);
		UmengUtil.onPageStart(this,"SplashScreen");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			switch (requestCode) {
			case CuttingPicturesUtil.CAMERA:
				File temp = new File(GlobleConstant.USERPHOTO_DIR
						+ GlobleConstant.USERPHOTO_NAME);
				startActivityForResult(CuttingPicturesUtil.startPhotoZoom(
						RegisterActivity.this, Uri.fromFile(temp)),
						CuttingPicturesUtil.ZOOMPHOTO);
				break;
			case CuttingPicturesUtil.ZOOMPHOTO:
				Log.e("REGISTER", "start zoom");
				// userOldPhoto = userPhoto;
				userPhoto = data.getExtras().getParcelable("data");
				imgViewHeader.setImageBitmap(userPhoto);
				try {
					FileUtils.saveBitmapToPath(userPhoto,
							GlobleConstant.USERPHOTO_DIR,
							GlobleConstant.USERPHOTO_NAME);
					upLoadPic();
				} catch (IOException e) {
					Log.e("REGISTER", e.toString());
					e.printStackTrace();
				}
				break;
			case CuttingPicturesUtil.LOCAL_PHOTO:
				startActivityForResult(CuttingPicturesUtil.startPhotoZoom(
						RegisterActivity.this, data.getData()),
						CuttingPicturesUtil.ZOOMPHOTO);
				break;
			}
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * 上传图片
	 */
	public void upLoadPic() {
		File file = new File(PhotoUtil.USER_PHOTO_PATH);
		HttpRequestFactory.getInstance().uploadFile(RegisterActivity.this,
				file, new CustomAsyncResponehandler() {

					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

					}

					@Override
					public void onFailure(Throwable error, String content) {
						PhotoUtil.printTrace("upload pic: on failure:"
								+ content);
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onSuccess(ResponeModel baseModel) {
						PhotoUtil.printTrace("upload pic: on success");

						UploadModel uploadModel = new UploadModel();
						uploadModel = JsonUtil.convertJsonToObject(
								baseModel.getJson(), UploadModel.class);

						if (uploadModel != null
								&& uploadModel.getResult() != null) {
							PhotoUtil.printTrace("uploadModel not null");
							UploadDataModel upModel = uploadModel.getResult();
							logoUrlStr = upModel.getSmallImageFile();
						}
					}

				});
	}
}
