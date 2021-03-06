package com.fullteem.yueba.app.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.db.DBFriendListDao;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.TimeUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.HintContentPopWindow;
import com.fullteem.yueba.widget.TimePickerItemView;
import com.fullteem.yueba.widget.TimerPickerViewController;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月16日&emsp;上午9:28:04
 * @use 发布约会界面
 */
public class DatePublishActivity extends BaseActivity {
	private EditText et_dateSubject;
	private TextView tv_dateTime;
	private CheckBox ckb_payByYou, ckb_payByMe, ckb_payByAA;
	private CheckBox ckb_genderBoy, ckb_genderGirl, ckb_genderOther;
	private EditText et_dateContent;
	private Button btn_dateAddress, btn_dateWine;
	private TextView tv_dateAddress, tv_dateWine;
	private Button btn_surePublish;
	private EventListener mListener;
	private int barId = -1;
	private int orderId = -1;
	private int favoriteId = -1;// 心仪对象id
	private int payState = 0; // 酒水可支付状态

	private final int DATE_TIME_AFTER = 2; // 约会时间在当前时间几小时后

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	
	private boolean enableClick;//是否能够点击

	public DatePublishActivity() {
		super(R.layout.activity_date_publish);
	}

	@Override
	public void initViews() {
		enableClick = true;
		initTopTitle();
		et_dateSubject = (EditText) findViewById(R.id.et_dateSubject);
		tv_dateTime = (TextView) findViewById(R.id.tv_dateTime);
		ckb_payByYou = (CheckBox) findViewById(R.id.ckb_payByYou);
		ckb_payByMe = (CheckBox) findViewById(R.id.ckb_payByMe);
		ckb_payByAA = (CheckBox) findViewById(R.id.ckb_payByAA);
		ckb_genderBoy = (CheckBox) findViewById(R.id.ckb_genderBoy);
		ckb_genderGirl = (CheckBox) findViewById(R.id.ckb_genderGirl);
		ckb_genderOther = (CheckBox) findViewById(R.id.ckb_genderOther);
		et_dateContent = (EditText) findViewById(R.id.et_dateContent);
		btn_dateAddress = (Button) findViewById(R.id.btn_dateAddress);
		btn_dateWine = (Button) findViewById(R.id.btn_dateWine);
		tv_dateAddress = (TextView) findViewById(R.id.tv_dateAddress);
		tv_dateWine = (TextView) findViewById(R.id.tv_dateWine);
		btn_surePublish = (Button) findViewById(R.id.btn_surePublish);

		favoriteId = getIntent().getIntExtra(GlobleConstant.DATE_FAVORITE_ID,
				-1);
		if (favoriteId != -1) {
			findViewById(R.id.llGender).setVisibility(View.GONE);
			findViewById(R.id.llFavorite).setVisibility(View.VISIBLE);
			((TextView) findViewById(R.id.tv_dateFavorite)).setText(getIntent()
					.getStringExtra(GlobleConstant.DATE_FAVORITE_NAME));
		}

		barId = getIntent().getIntExtra(GlobleConstant.PUB_ID, -1);
		payState = getIntent().getIntExtra(GlobleConstant.ENABLE_PAY, 0);

		String barName = getIntent().getStringExtra(GlobleConstant.PUB_NAME);
		if (barId > 0 && null != barName && "" != barName) {
			tv_dateAddress.setVisibility(View.VISIBLE);
			tv_dateAddress.setText(barName);
			btn_dateAddress.setVisibility(View.GONE);
		}
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.date_publish), null);
	}

	@Override
	public void initData() {
		mListener = new EventListener();
	}

	@Override
	public void bindViews() {
		ckb_genderBoy.setOnTouchListener(mListener);
		ckb_genderGirl.setOnTouchListener(mListener);
		ckb_genderOther.setOnTouchListener(mListener);
		ckb_payByAA.setOnTouchListener(mListener);
		ckb_payByMe.setOnTouchListener(mListener);
		ckb_payByYou.setOnTouchListener(mListener);
		tv_dateTime.setOnClickListener(mListener);
		btn_dateAddress.setOnClickListener(mListener);
		btn_dateWine.setOnClickListener(mListener);
		btn_surePublish.setOnClickListener(mListener);
	}

	private class EventListener implements OnTouchListener, OnClickListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				switch (v.getId()) {
				case R.id.ckb_genderBoy:
					UmengUtil.onEvent(DatePublishActivity.this, "expect_man_button_hits");
					LogUtil.printUmengLog("expect_man_button_hits");
					ckb_genderBoy.setChecked(true);
					ckb_genderGirl.setChecked(false);
					ckb_genderOther.setChecked(false);
					break;
				case R.id.ckb_genderGirl:
					UmengUtil.onEvent(DatePublishActivity.this, "expect_woman_button_hits");
					LogUtil.printUmengLog("expect_woman_button_hits");
					ckb_genderBoy.setChecked(false);
					ckb_genderGirl.setChecked(true);
					ckb_genderOther.setChecked(false);
					break;
				case R.id.ckb_genderOther:
					UmengUtil.onEvent(DatePublishActivity.this, "expect_unlimited_button_hits");
					LogUtil.printUmengLog("expect_unlimited_button_hits");
					ckb_genderBoy.setChecked(false);
					ckb_genderGirl.setChecked(false);
					ckb_genderOther.setChecked(true);
					break;

				case R.id.ckb_payByAA:
					UmengUtil.onEvent(DatePublishActivity.this, "expense_share_button_hits");
					LogUtil.printUmengLog("expense_share_button_hits");
					ckb_payByAA.setChecked(true);
					ckb_payByMe.setChecked(false);
					ckb_payByYou.setChecked(false);
					break;
				case R.id.ckb_payByMe:
					UmengUtil.onEvent(DatePublishActivity.this, "expense_i_treat_button_hits");
					LogUtil.printUmengLog("expense_i_treat_button_hits");
					ckb_payByAA.setChecked(false);
					ckb_payByMe.setChecked(true);
					ckb_payByYou.setChecked(false);
					break;
				case R.id.ckb_payByYou:
					UmengUtil.onEvent(DatePublishActivity.this, "expense_you_treat_button_hits");
					LogUtil.printUmengLog("expense_you_treat_button_hits");
					ckb_payByAA.setChecked(false);
					ckb_payByMe.setChecked(false);
					ckb_payByYou.setChecked(true);
					break;
				}
				
				break;

			}
			
			return true;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_dateAddress:
				Intent intent = new Intent(DatePublishActivity.this,
						SelectNearbyPubActivity.class);
				intent.putExtra(GlobleConstant.ACTION_ADDRESS, true);
				startActivityForResult(intent,
						GlobleConstant.ACTION_ADDRESS_CODE);
				break;

			case R.id.tv_dateTime:
				selectTime();
				break;

			case R.id.btn_surePublish:
				if(enableClick){
					enableClick = false; // 防止多次点击重复发布同一约会
					publishDate();
					
				}
				break;

			case R.id.btn_dateWine:
				if (barId == -1) {
					showToast(getString(R.string.hint_selectBar));
					return;
				}
				Intent intentSelectOrder = new Intent(DatePublishActivity.this,
						SelectWineOrderActivity.class);
				intentSelectOrder.putExtra("BAR_ID", barId);
				intentSelectOrder.putExtra(GlobleConstant.ENABLE_PAY, payState);
				startActivityForResult(intentSelectOrder,
						GlobleConstant.ACTION_ORDER_CODE);
				break;
			}
		}

	}

	/**
	 * 选择时间
	 */
	private void selectTime() {
		final LinearLayout timepickerview = (LinearLayout) View.inflate(
				DatePublishActivity.this, R.layout.view_timepicker, null);
		final TimerPickerViewController timerPickerViewController = new TimerPickerViewController(
				timepickerview, true);
		timerPickerViewController.screenheight = DisplayUtils
				.getScreenHeight(DatePublishActivity.this) - 100;
		String time = tv_dateTime.getText().toString();
		Calendar calendar = Calendar.getInstance();
		if (TimeUtil.isDate(time, "yyyy-MM-dd HH:mm")) {
			try {
				calendar.setTime(dateFormat.parse(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		timerPickerViewController.setStsarYear(year); // 开始时间为当前年,结束时间为当前年后50年
		timerPickerViewController.setEndYear(year + 50);
		timerPickerViewController.initDateTimePicker(year, month, day, hour,
				minute);
		if (DisplayUtils.getScreenWidht(DatePublishActivity.this) < 720) {
			new AlertDialog.Builder(DatePublishActivity.this)
					.setTitle("选择时间")
					.setView(timepickerview)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									tv_dateTime
											.setText(timerPickerViewController
													.getTime());
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
		} else {
			TimePickerItemView itemView;
			for (int i = 0; i < timepickerview.getChildCount(); i++) {
				itemView = (TimePickerItemView) timepickerview.getChildAt(i);
				itemView.setMinimumWidth(DisplayUtils.dp2px(
						DatePublishActivity.this,
						(DisplayUtils.getScreenWidht(DatePublishActivity.this) - DisplayUtils
								.dp2px(DatePublishActivity.this, 20))
								/ timepickerview.getChildCount()));
			}
			final HintContentPopWindow hintContentPopWindow = new HintContentPopWindow(
					DatePublishActivity.this);
			hintContentPopWindow.setTitle("选择时间").setCenterView(timepickerview)
					.showWindow(new OnClickListener() {
						@Override
						public void onClick(View v) {
							tv_dateTime.setText(timerPickerViewController
									.getTime());
							hintContentPopWindow.getPopupWindow().dismiss();
						}
					});
		}
	}
	/**
	 * 发布约会
	 */
	private void publishDate() {
		
		if (TextUtils.isEmpty(et_dateSubject.getText().toString().trim())) {
			showToast(getString(R.string.hint_dateSubjectNotNone));
			enableClick = true;
			return;
		}
		if (TextUtils.isEmpty(tv_dateTime.getText().toString().trim())) {
			showToast(getString(R.string.hint_selectTime));
			enableClick = true;
			return;
		}
		if (TimeUtil.getTimestamp(tv_dateTime.getText().toString()) < (System
				.currentTimeMillis() + DATE_TIME_AFTER * 60 * 60 * 1000)) { // 约会时间>=当前时间2小时
			showToast(StringUtils.formatStrD2Str(context,
					R.string.hint_timeError, DATE_TIME_AFTER));
			enableClick = true;
			return;
		}

		if (TextUtils.isEmpty(et_dateContent.getText().toString().trim())) {
			showToast(getString(R.string.hint_dateContentNotNone));
			enableClick = true;
			return;
		}
		if (getUserAppointmentFee() == -1) {
			showToast(getString(R.string.hint_selectFee));
			enableClick = true;
			return;
		}
		if (findViewById(R.id.llGender).getVisibility() == View.VISIBLE) // 需要期望对象性别
			if (getUserAppointmentObj() == -1) {
				showToast(getString(R.string.hint_selectDateGender));
				enableClick = true;
				return;
			}
		if (barId == -1) {
			showToast(getString(R.string.hint_selectPub));
			enableClick = true;
			return;
		}
		// 如果是男用乎，需要有酒水订单
		if ("男".equals(AppContext.getApplication().getUserInfo().getUserSex())) {
			// TODO 酒水订单id
		}

		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId == null || userId < 0) {
			showToast(getString(R.string.hint_longinFirst));
			enableClick = true;
			return;
		}
		
		//友盟统计
		if(favoriteId > 0){
			//说明是单独约会
			UmengUtil.onEvent(DatePublishActivity.this, "friend_chat_access_post_button_hits");
			LogUtil.printUmengLog("friend_chat_access_post_button_hits");
		}else{
			
			if(btn_dateAddress.getVisibility() == View.GONE){
				//选择酒吧地址按钮不可见，说明从酒吧页面进入
				UmengUtil.onEvent(DatePublishActivity.this, "bar_access_post_button_hits");
				LogUtil.printUmengLog("bar_access_post_button_hits");
			}else{
				UmengUtil.onEvent(DatePublishActivity.this, "post_button_hits");
				LogUtil.printUmengLog("post_button_hits");
			}
		}

		Integer favoriteID = favoriteId == -1 ? null : favoriteId;
		Integer userAppointmentType = favoriteID == null ? 2 : 1;
		Integer userAppointmentObj = favoriteID == null ? getUserAppointmentObj()
				: null;

		HttpRequest.getInstance(DatePublishActivity.this).datePublish(
				userAppointmentType, getUserAppointmentFee(),
				userAppointmentObj, favoriteID,
				et_dateSubject.getText().toString(),
				et_dateContent.getText().toString(),
				tv_dateTime.getText().toString(), barId, userId, null,
				new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						LogUtil.LogDebug(getClass().getName(),
								baseModel.toString(), null);
						if (baseModel != null) {
							if (baseModel.isStatus()) {
								// showToast(getString(R.string.hint_datePublished));
								if (favoriteId != -1)
									sendText();
								finish();
							} else {
								showToast(getString(R.string.hint_datePublishError)
										+ baseModel.getResult());
							}
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onFailure(Throwable error, String content) {
						showToast(getString(R.string.hint_datePublishError)
								+ content);
					}
				});
	};

	/**
	 * @return 约会费用类型(0:你请；1:我请；2:AA)
	 */
	private int getUserAppointmentFee() {
		return ckb_payByYou.isChecked() ? 0 : ckb_payByMe.isChecked() ? 1
				: ckb_payByAA.isChecked() ? 2 : -1;
	}

	/**
	 * @return 约会期望对象(0:男;1:女;2:不限)
	 */
	private int getUserAppointmentObj() {
		return ckb_genderBoy.isChecked() ? 0 : ckb_genderGirl.isChecked() ? 1
				: ckb_genderOther.isChecked() ? 2 : -1;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null)
			return;
		switch (requestCode) {
		case GlobleConstant.ACTION_ADDRESS_CODE:
			String dateAddress = data
					.getStringExtra(GlobleConstant.ACTION_ADDRESS);
			btn_dateAddress.setText(getString(R.string.hint_change));
			tv_dateAddress.setVisibility(View.VISIBLE);
			tv_dateAddress.setText(dateAddress);
			barId = data.getIntExtra(GlobleConstant.PUB_ID, -1);
			payState = data.getIntExtra(GlobleConstant.ENABLE_PAY, 0);
			break;
		case GlobleConstant.ACTION_ORDER_CODE:
			String producteDescription = data
					.getStringExtra("producteDescription");
			btn_dateWine.setText(getString(R.string.hint_change));
			tv_dateWine.setVisibility(View.VISIBLE);
			tv_dateWine.setText(producteDescription);
			// barId = data.getIntExtra(GlobleConstant.PUB_ID, -1);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 发布单独约会需要发送消息通知心仪对象
	 */
	private void sendText() {
		String userMobile = getIntent().getStringExtra(
				GlobleConstant.DATE_FAVORITE_MOBILE);
		if (TextUtils.isEmpty(userMobile))
			return;
		String userName = getIntent().getStringExtra(
				GlobleConstant.DATE_FAVORITE_NAME);
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation(userMobile);

		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		TextMessageBody txtBody = new TextMessageBody(appContext.getUserInfo()
				.getUserName() + " 对 " + userName + " 发起约会，聊聊约会细节吧！");
		// 设置消息body
		message.addBody(txtBody);
		// 设置要发给谁,用户username或者群聊groupid
		message.setReceipt(userMobile);

		// 添加默认的扩展字段,此处设置为他的nickname
		message.setAttribute("nickname", appContext.getUserInfo().getUserName());

		String imgurl;
		if (appContext.getUserInfo().getUserLogoUrl() == null) {
			imgurl = "null";
		} else {
			imgurl = appContext.getUserInfo().getUserLogoUrl();
		}
		message.setAttribute("imgurl", imgurl);
		message.setAttribute("userid", appContext.getUserInfo().getImServerId());
		message.setAttribute("type", GlobleConstant.MESSAGE_TYPE);
		// 把messgage加到conversation中
		// message.isAcked = false;
		conversation.addMessage(message);

		if (getIntent().getBooleanExtra("isExist", false)) // 如果数据在数据库存在则不需要在保存
			return;

		String userImage = getIntent().getStringExtra(
				GlobleConstant.DATE_FAVORITE_URL);

		UserCommonModel user = new UserCommonModel();
		user.setUserMobile(userMobile);
		user.setUserId(favoriteId + "");
		user.setUserName(userName);
		if (TextUtils.isEmpty(userImage))
			userImage = "null";
		user.setUserLogoUrl(userImage);
		saveToDB(user);
	}

	private void saveToDB(final UserCommonModel user) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DBFriendListDao dao = new DBFriendListDao(appContext);
				dao.saveContacter(user);
			}
		}).start();
	}

}
