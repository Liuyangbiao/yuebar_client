package com.fullteem.yueba.app.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BarMessageAdapter;
import com.fullteem.yueba.app.adapter.ExpressionAdapter;
import com.fullteem.yueba.app.adapter.ExpressionPagerAdapter;
import com.fullteem.yueba.engine.push.MessageUtil;
import com.fullteem.yueba.engine.push.PushService;
import com.fullteem.yueba.model.BarChatModel;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SmileUtils;
import com.fullteem.yueba.widget.ExpandGridView;
import com.fullteem.yueba.widget.PasteEditText;

/**
 * 聊天页面
 * 
 */
@SuppressWarnings("deprecation")
public class BarChatActivity extends BaseActivity implements OnClickListener {

	private ListView chatListView;
	private PasteEditText mEditTextContent;

	private View buttonSend;

	private ViewPager expressionViewpager;
	private InputMethodManager manager;
	private List<String> reslist;

	public static BarChatActivity activityInstance = null;

	private ImageView iv_emoticons_normal;
	private ImageView iv_emoticons_checked;
	private RelativeLayout edittext_layout;

	private Button btnMore;

	private String barId;
	private BarMessageAdapter chatMsgAdapter;
	private List<BarChatModel> chatMsgList;

	// for receive customer msg from jpush server
	private NewMessageBroadcastReceiver chatMsgReceiver;

	public static boolean isForeground = false;

	class NewMessageBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtil.printPushLog("received new message");

			if (MessageUtil.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {

				BarChatModel model = MessageUtil.parseMessage(intent);
				if (model.isReceiver()) {// not sender
					chatMsgList.add(model);

					// 通知chatMsgAdapter有新消息，更新ui
					chatMsgAdapter.refresh();
					chatListView.setSelection(chatListView.getCount() - 1);
				}

			}
		}
	}

	public BarChatActivity() {
		super(R.layout.activity_bar_chat);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if (null != intent) {
			Bundle bundle = getIntent().getExtras();
			this.barId = bundle.getString("barId");
		}

		initView();
		setUpView();

		// to receive message
		registerMessageReceiver();
		PushService.setPushTag(this.barId);
	}

	/**
	 * initView
	 */
	protected void initView() {

		chatListView = (ListView) findViewById(R.id.list);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);

		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);

		buttonSend = findViewById(R.id.btn_send);
		btnMore = (Button) findViewById(R.id.btn_more);

		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);

		edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);

		// 表情list
		reslist = getExpressionRes(35);
		// 初始化表情viewpager
		List<View> views = new ArrayList<View>();
		View gv1 = getGridChildView(1);
		View gv2 = getGridChildView(2);
		views.add(gv1);
		views.add(gv2);

		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
		edittext_layout.requestFocus();

		mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					edittext_layout
							.setBackgroundResource(R.drawable.input_bar_bg_active);
				} else {
					edittext_layout
							.setBackgroundResource(R.drawable.input_bar_bg_normal);
				}

			}
		});
		mEditTextContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edittext_layout
						.setBackgroundResource(R.drawable.input_bar_bg_active);

				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);

			}
		});
		// 监听文字框
		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					btnMore.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				} else {
					// btnMore.setVisibility(View.VISIBLE);
					buttonSend.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	private void setUpView() {

		activityInstance = this;
		iv_emoticons_normal.setOnClickListener(this);
		iv_emoticons_checked.setOnClickListener(this);

		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		chatMsgList = new ArrayList<BarChatModel>();
		chatMsgAdapter = new BarMessageAdapter(this, chatMsgList);

		// 显示消息
		chatListView.setAdapter(chatMsgAdapter);
		chatListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard();

				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);

				return false;
			}
		});

	}

	/**
	 * 消息图标点击事件
	 * 
	 * @param view
	 */
	@Override
	public void onClick(View view) {

		int id = view.getId();
		if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
			String s = mEditTextContent.getText().toString();
			sendText(s);
		} else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框

			iv_emoticons_normal.setVisibility(View.INVISIBLE);
			iv_emoticons_checked.setVisibility(View.VISIBLE);

			hideKeyboard();
		} else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);

		}
	}

	/**
	 * 发送文本消息
	 * 
	 * @param content
	 *            message content
	 * @param isResend
	 *            boolean resend
	 */
	private void sendText(String content) {

		if (content.length() > 0) {

			BarChatModel model = MessageUtil.createNewMessage(this.barId,
					MessageUtil.MESSAGE_TYPE_TXT, content);
			MessageUtil.sendNewMessageRequest(model);

			// add the message immediately
			chatMsgList.add(model);

			// 通知chatMsgAdapter有消息变动，chatMsgAdapter会根据加入的这条message显示消息和调用sdk的发送方法
			chatMsgAdapter.refresh();
			chatListView.setSelection(chatListView.getCount() - 1);
			mEditTextContent.setText("");
		}
	}

	/**
	 * 点击文字输入框
	 * 
	 * @param v
	 */
	public void editClick(View v) {
		chatListView.setSelection(chatListView.getCount() - 1);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);

	}

	private void registerMessageReceiver() {
		chatMsgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MessageUtil.MESSAGE_RECEIVED_ACTION);
		registerReceiver(chatMsgReceiver, filter);
	}

	/**
	 * 获取表情的gridview的子view
	 * 
	 * @param i
	 * @return
	 */
	private View getGridChildView(int i) {
		View view = View.inflate(this, R.layout.expression_gridview, null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
				1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					// 文字输入框可见时，才可输入表情
					// 按住说话可见，不让输入表情
					if (true) {

						if (filename != "delete_expression") { // 不是删除键，显示表情
							// 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
							Class<?> clz = Class
									.forName("com.fullteem.yueba.util.SmileUtils");
							Field field = clz.getField(filename);
							mEditTextContent.append(SmileUtils.getSmiledText(
									BarChatActivity.this,
									(String) field.get(null)));
						} else { // 删除文字或者表情
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent
										.getSelectionStart();// 获取光标的位置
								if (selectionStart > 0) {
									String body = mEditTextContent.getText()
											.toString();
									String tempStr = body.substring(0,
											selectionStart);
									int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
									if (i != -1) {
										CharSequence cs = tempStr.substring(i,
												selectionStart);
										if (SmileUtils.containsKey(cs
												.toString()))
											mEditTextContent.getEditableText()
													.delete(i, selectionStart);
										else
											mEditTextContent.getEditableText()
													.delete(selectionStart - 1,
															selectionStart);
									} else {
										mEditTextContent.getEditableText()
												.delete(selectionStart - 1,
														selectionStart);
									}
								}
							}

						}
					}
				} catch (Exception e) {
					System.out.println(e.toString());
				}

			}
		});
		return view;
	}

	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;
			reslist.add(filename);
		}
		return reslist;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;

		// 注销广播
		try {
			unregisterReceiver(chatMsgReceiver);
			chatMsgReceiver = null;
		} catch (Exception e) {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isForeground = true;
		chatMsgAdapter.refresh();
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();

	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		finish();
	}

	/**
	 * 覆盖手机返回键
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public String getBarId() {
		return barId;
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void bindViews() {
		// TODO Auto-generated method stub

	}

}
