package com.fullteem.yueba.app.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.BarMessageAdapter;
import com.fullteem.yueba.app.adapter.ExpressionAdapter;
import com.fullteem.yueba.app.adapter.ExpressionPagerAdapter;
import com.fullteem.yueba.app.adapter.OnlineUserAdapter;
import com.fullteem.yueba.app.adapter.PicAdapter;
import com.fullteem.yueba.app.adapter.PubPackageAdapter;
import com.fullteem.yueba.app.adapter.PubSingerAdapter;
import com.fullteem.yueba.app.singer.model.SingerModel;
import com.fullteem.yueba.engine.push.MessageUtil;
import com.fullteem.yueba.engine.push.PushService;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.BarChatModel;
import com.fullteem.yueba.model.BarDynamicModel;
import com.fullteem.yueba.model.BaseModel;
import com.fullteem.yueba.model.CommonModel;
import com.fullteem.yueba.model.OnlineUserInfoModel;
import com.fullteem.yueba.model.OnlineUserModel;
import com.fullteem.yueba.model.PicModle;
import com.fullteem.yueba.model.PubModel;
import com.fullteem.yueba.model.PubPackageModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SmileUtils;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.AutoRollTextView;
import com.fullteem.yueba.widget.ChildViewPager;
import com.fullteem.yueba.widget.ExpandGridView;
import com.fullteem.yueba.widget.PasteEditText;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * 酒吧详情
 * 
 * @author ssy
 * 
 */
public class PubDetailsActivity extends BaseActivity implements OnClickListener {
	// pub env
	private ChildViewPager mPubEnvironment;
	private List<PicModle> listPubEnvironment;
	private PubEnvironmentAdapter pubEnvAdapter;

	// singer: layout, list view, list, adapter
	private LinearLayout llPubSinger;
	private ListView lvSinger;
	private List<SingerModel> listSinger;
	private PubSingerAdapter singerAdapter;

	// wine wineSet, including promotion
	private LinearLayout llWineSet;
	private ListView lvWineSet;

	private ListView lvPromotion;
	private List<PubPackageModel> listPubPackage;
	private List<PicModle> listPromotion;
	private PubPackageAdapter wineSetAdapter;
	private PicAdapter promotionAdapter;

	// pub info:
	private LinearLayout llBarInfo;
	private ImageView ivPubHeader;
	private CheckBox ckbHeart;
	private TextView pubName, tvAddress, tvPhoneNum, tvDetail;

	/*
	 * Start:hot chat
	 */
	private RelativeLayout hotChatLayout;

	// chat content area
	private ListView chatListView;
	private List<BarChatModel> chatMsgList;
	private BarMessageAdapter chatMsgAdapter;
	private NewMessageBroadcastReceiver chatMsgReceiver;// for receive customer
														// msg from jpush server

	/* send msg area */
	// edit text layout: relative
	private RelativeLayout edittext_layout;// contains 3 items as below
	private PasteEditText mEditTextContent;
	private ImageView iv_emoticons_normal;
	private ImageView iv_emoticons_checked;

	// send btn
	private View buttonSend;

	// express icons
	private LinearLayout emojiIconContainer;
	private ViewPager expressionViewpager;
	private List<String> reslist;

	private InputMethodManager manager;

	/*
	 * End of hot chat member definition
	 */

	// online

	private LinearLayout llOnlineUser;
	private ListView lvOnlineUser;
	private List<OnlineUserModel> listOnlineUser;
	private OnlineUserAdapter onlineUserAdapter;

	// function buttons
	private Button btHotChat, btBarDetail, btBarWine, btBarSinger,
			btOnlineUser;

	/*
	 * select the bar as target If in the mode of setting bar address, for the
	 * function buttons, only display 'btBarDetail'
	 */
	private LinearLayout llsetAddress;
	boolean isSetAddressMode;

	// others
	private String pubNameStr;// 酒吧名,发布约会传回去
	private int barId;// 酒id,发布约会传回去
	private int userId;
	private boolean isFollow;
	private boolean isBack;

	private int pageNo = 1;
	private int pageSize = 10;
	private List<PubModel> listPub;
	private AutoRollTextView pubDynamic;

	public enum Column {
		CHAT, DETAIL, WINE, SINGER, ONLINE
	}

	private Column column;

	public PubDetailsActivity() {
		super(R.layout.activity_pubdetail);
	}

	@Override
	public void initViews() {
		// for title
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.toptile);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				LogUtil.printPushLog("PubDetails Back clicked");
				finish();
			}
		});

		final String barName = getIntent().getStringExtra(
				GlobleConstant.PUB_NAME);
		topTitle.showCenterText(barName, null);

		topTitle.showRightImag(R.drawable.img_toptitle_right_date,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						UmengUtil.onEvent(PubDetailsActivity.this, "bar_access_post_date_button_hits");
						int barId = getIntent().getIntExtra(GlobleConstant.PUB_ID, -1);
						Intent intent = new Intent(PubDetailsActivity.this,
								DatePublishActivity.class);
						intent.putExtra(GlobleConstant.PUB_NAME, barName);
						intent.putExtra(GlobleConstant.PUB_ID, barId);
						intent.putExtra(GlobleConstant.ENABLE_PAY, getIntent().getIntExtra(GlobleConstant.ENABLE_PAY, 0));
						startActivity(intent);
					}
				});

		// pub env 动态设置为屏幕高度的1/4
		mPubEnvironment = (ChildViewPager) findViewById(R.id.vp_pub_environment);
		pubDynamic = (AutoRollTextView) findViewById(R.id.tv_pub_dynamic);
		int width = DisplayUtils.getScreenWidht(this);
		LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, width * 3 / 5);
		mPubEnvironment.setLayoutParams(params);

		// mode
		/*
		 * current design is let user do whatever even in SetAddressMode. So
		 * currently just set isSetAddressMode to be false.
		 */
		isSetAddressMode = false;
		/*
		 * isSetAddressMode = getIntent().getBooleanExtra(
		 * GlobleConstant.ACTION_ADDRESS, false);
		 */

		// function buttons
		btHotChat = (Button) findViewById(R.id.btHotChat);
		btBarDetail = (Button) findViewById(R.id.btBarDetail);
		btBarWine = (Button) findViewById(R.id.btBarWine);
		btBarSinger = (Button) findViewById(R.id.btBarSinger);
		btOnlineUser = (Button) findViewById(R.id.btOnlineUser);

		// singer
		llPubSinger = (LinearLayout) findViewById(R.id.llPubSinger);
		lvSinger = (ListView) findViewById(R.id.lvSinger);

		// wine set
		llWineSet = (LinearLayout) findViewById(R.id.llWineSet);
		lvWineSet = (ListView) findViewById(R.id.lvWineSet);
		lvPromotion = (ListView) findViewById(R.id.lvPromotion);// promotion

		// pub info
		llBarInfo = (LinearLayout) findViewById(R.id.llBarInfo);
		ivPubHeader = (ImageView) findViewById(R.id.ivPubHeader);
		ckbHeart = (CheckBox) findViewById(R.id.ckbHeart);
		pubName = (TextView) findViewById(R.id.pubName);
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		tvPhoneNum = (TextView) findViewById(R.id.tvPhoneNum);
		tvDetail = (TextView) findViewById(R.id.tvDetail);

		// used for bar selection
		llsetAddress = (LinearLayout) findViewById(R.id.llsetAddress);

		// hot chat
		initChatView();

		// online user
		initOnlineUserView();

		// visibility setting according to mode
		if (isSetAddressMode) {
			btHotChat.setVisibility(View.GONE);
			btBarWine.setVisibility(View.GONE);
			btBarSinger.setVisibility(View.GONE);
			btOnlineUser.setVisibility(View.GONE);

			llPubSinger.setVisibility(View.GONE);
			llWineSet.setVisibility(View.GONE);
			//hotChatLayout.setVisibility(View.GONE);
			mHotChatList.setVisibility(View.GONE);
			mHotChatSend.setVisibility(View.GONE);
			
			llOnlineUser.setVisibility(View.GONE);

			btBarDetail.setClickable(false);
			llBarInfo.setVisibility(View.VISIBLE);
		} else {
			// llsetAddress.setVisibility(View.GONE);
			llOnlineUser.setVisibility(View.VISIBLE);
		}

		boolean addressVisible = getIntent().getBooleanExtra(
				GlobleConstant.ACTION_ADDRESS, false);
		if (addressVisible) {
			llsetAddress.setVisibility(View.VISIBLE);
		} else {
			llsetAddress.setVisibility(View.GONE);
		}
	}

	@Override
	public void initData() {

		if (!isBack)
			loadData(barId = getIntent().getIntExtra(GlobleConstant.PUB_ID, -1));

		// bar/pub env
		listPubEnvironment = new ArrayList<PicModle>();
		pubEnvAdapter = new PubEnvironmentAdapter();
		mPubEnvironment
				.setAdapter(pubEnvAdapter = pubEnvAdapter == null ? new PubEnvironmentAdapter()
						: pubEnvAdapter);

		if (!isSetAddressMode) {
			// singer
			lvSinger.setAdapter(singerAdapter = singerAdapter == null ? new PubSingerAdapter(
					PubDetailsActivity.this,
					listSinger = listSinger == null ? new ArrayList<SingerModel>()
							: listSinger)
					: singerAdapter);

			// wine
			lvPromotion
					.setAdapter(promotionAdapter = promotionAdapter == null ? new PicAdapter(
							listPromotion = listPromotion == null ? new ArrayList<PicModle>()
									: listPromotion)
							: promotionAdapter);
			lvWineSet
					.setAdapter(wineSetAdapter = wineSetAdapter == null ? new PubPackageAdapter(
							PubDetailsActivity.this,
							listPubPackage = listPubPackage == null ? new ArrayList<PubPackageModel>()
									: listPubPackage, getIntent().getIntExtra(GlobleConstant.ENABLE_PAY, 0),true)
							: wineSetAdapter);
			listPub = new ArrayList<PubModel>();
			// chat
			initChatData();

			// online user
			initOnlineUserData();

		}
	}

	@Override
	public void bindViews() {
		llsetAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView iv = (ImageView) findViewById(R.id.ivSetAddress);
				iv.setBackgroundResource(R.drawable.icon_date_pressed);
				DisplayUtils.resetBack(iv, R.drawable.icon_date);

				if (TextUtils.isEmpty(pubNameStr)) {
					showToast(getString(R.string.hint_pubDateLoading));
					return;
				}
				Intent intent = new Intent();
				intent.putExtra(GlobleConstant.ACTION_ADDRESS, pubNameStr);
				intent.putExtra(GlobleConstant.PUB_ID, barId);
				setResult(GlobleConstant.ACTION_ADDRESS_CODE, intent);
				finish();
			}
		});

		ckbHeart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(pubNameStr)) {
					ckbHeart.setChecked(!ckbHeart.isChecked());
					showToast(getString(R.string.hint_pubDateLoading));
					return;
				}

				HttpRequest.getInstance(PubDetailsActivity.this).operateFollow(
						1, barId, userId, null, null, isFollow,
						new CustomAsyncResponehandler() {

							@Override
							public void onSuccess(ResponeModel baseModel) {
								if (baseModel != null && baseModel.isStatus())
									loadData(barId);
								else {
									ckbHeart.setChecked(!ckbHeart.isChecked());
									showToast(getString(R.string.hint_operationError));
								}
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
								ckbHeart.setChecked(!ckbHeart.isChecked());
								showToast(getString(R.string.hint_operationError));
							}
						});

			}
		});
	}

	/**
	 * 从服务器获取数据
	 */
	private void loadData(int barId) {
		if (isBack)
			return;

		this.userId = Integer.valueOf(
				AppContext.getApplication().getUserInfo().getUserId())
				.intValue();
		if (barId == -1 || userId == -1) {
			showToast(getString(R.string.hint_getPubDetailError));
			finish();
		}

		HttpRequest.getInstance(PubDetailsActivity.this).getPubDetait(barId,
				userId, new CustomAsyncResponehandler() {
					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel == null
								|| baseModel.getResultObject() == null
								|| baseModel.getDataResult() == null) {
							showToast(getString(R.string.hint_getPubDetailError));
							return;
						}
						List<BarDynamicModel> barDynamicList = (List<BarDynamicModel>) baseModel
								.getListObject("barDynamicList",
										BarDynamicModel.class);
						if (null != barDynamicList && barDynamicList.size() > 0
								&& null != barDynamicList.get(0)) {
							pubDynamic.setText(barDynamicList.get(0)
									.getDynamicContent());
						} else {
							pubDynamic
									.setText(getString(R.string.default_pub_dynamic));
						}
						// bar info
						final PubModel pubModel = (PubModel) baseModel
								.getObject("barMap", PubModel.class);
						ImageLoaderUtil.getImageLoader().displayImage(
								DisplayUtils.getAbsolutelyUrl(pubModel
										.getBarLogoUrl()),
								ivPubHeader,
								ImageLoaderUtil.getOptions(
										R.drawable.img_loading_default_copy,
										R.drawable.img_loading_empty_copy,
										R.drawable.img_loading_fail_copy));
						pubName.setText(pubNameStr = pubModel.getBarName());
						tvAddress.setText(pubModel.getBarAddress());
						tvPhoneNum.setText(pubModel.getBarTelephone());
						tvDetail.setText(pubModel.getBarIntroduction());

						tvPhoneNum.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent callIntent = new Intent(
										Intent.ACTION_CALL, Uri.parse("tel:"
												+ pubModel.getBarTelephone()));
								startActivity(callIntent);
							}
						});

						String isFollowStr = baseModel
								.getTreeInResult("isAttention");
						pubModel.setIsFollow(isFollow = "true"
								.equals(isFollowStr));

						ckbHeart.setChecked(isFollow);

						if (pubModel.isSingingBar())
							findViewById(R.id.ivSingingBar).setVisibility(
									View.VISIBLE);
						Integer barTypeRes = pubModel.getBarType() == 1 ? R.drawable.img_pub_type1
								: pubModel.getBarType() == 2 ? R.drawable.img_pub_type2
										: pubModel.getBarType() == 3 ? R.drawable.img_pub_type3
												: null;// 1、清吧；2迪吧、3演绎
						if (barTypeRes != null) {
							ImageView ivBarType = (ImageView) findViewById(R.id.ivBarType);
							ivBarType.setBackgroundResource(barTypeRes);
							ivBarType.setVisibility(View.VISIBLE);
						}

						// after get bar name, wellcome display
						displayWellcomeMessage();

						// bar env
						List<PicModle> barPics = ((List<PicModle>) baseModel
								.getListObject("barPhotoList", PicModle.class));
						System.out.println("barPics长度：" + barPics.size());
						if (barPics == null || barPics.size() <= 0
								|| barPics.get(0) == null) {
							pubEnvAdapter = null;
						} else {
							if (!listPubEnvironment.isEmpty())
								listPubEnvironment.clear();
							listPubEnvironment.addAll(barPics);
							pubEnvAdapter.notifyDataSetChanged();
						}

						if (!isSetAddressMode) {
							// singer
							List<SingerModel> barSingers = ((List<SingerModel>) baseModel
									.getListObject("barSingerList",
											SingerModel.class));
							if (barSingers == null || barSingers.size() <= 0
									|| barSingers.get(0) == null) {
								llPubSinger.setVisibility(View.GONE);
								singerAdapter = null;
								listSinger = null;
							} else {
								if (!listSinger.isEmpty())
									listSinger.clear();
								listSinger.addAll(barSingers);
								singerAdapter.notifyDataSetChanged();
							}

							// 套餐选择
							List<PubPackageModel> barPackage = ((List<PubPackageModel>) baseModel
									.getListObject("barCouponList",
											PubPackageModel.class));
							if (barPackage == null || barPackage.size() <= 0
									|| barPackage.get(0) == null) {
								llWineSet.setVisibility(View.GONE);
								wineSetAdapter = null;
								listPubPackage = null;
							} else {
								if (!listPubPackage.isEmpty())
									listPubPackage.clear();
								listPubPackage.addAll(barPackage);
								wineSetAdapter.notifyDataSetChanged();
							}

							// promotion
							List<PicModle> picModel = (List<PicModle>) baseModel
									.getListObject("barCxPhotoList",
											PicModle.class);
							if (picModel != null && picModel.size() > 0
									&& picModel.get(0) != null) {
								if (!listPromotion.isEmpty())
									listPromotion.clear();
								listPromotion.addAll(picModel);
								if (listPromotion.size() <= 0) {
									promotionAdapter = null;
									listPromotion = null;
								} else
									promotionAdapter.notifyDataSetChanged();
							} else {
								promotionAdapter = null;
								listPromotion = null;
							}

						}

					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.printPushLog("load data failed"
								+ error.toString());
					}
				});
	}

	// response to function buttons
	public void showHotChat(View view) {
		LogUtil.printPushLog("button is clicked" + view.getId());
		Column newColumn = Column.CHAT;
		handleColumnSwitch(newColumn);
	}

	public void showBarDetail(View view) {
		LogUtil.printPushLog("button is clicked" + view.getId());
		UmengUtil.onEvent(this, "bar_detail_button_hits");
		Column newColumn = Column.DETAIL;
		handleColumnSwitch(newColumn);
	}

	public void showWineSet(View view) {
		LogUtil.printPushLog("button is clicked" + view.getId());
		UmengUtil.onEvent(this, "bar_drinks_button_hits");
		Column newColumn = Column.WINE;
		handleColumnSwitch(newColumn);
	}

	public void showSinger(View view) {
		LogUtil.printPushLog("button is clicked" + view.getId());
		UmengUtil.onEvent(this, "bar_singer_button_hits");
		Column newColumn = Column.SINGER;
		handleColumnSwitch(newColumn);
	}

	public void showOnlineUser(View view) {
		LogUtil.printPushLog("button is clicked" + view.getId());
		UmengUtil.onEvent(this, "bar_online_friend_button_hits");
		Column newColumn = Column.ONLINE;
		handleColumnSwitch(newColumn);
		getOnlineUserRequest();
	}

	private void handleColumnSwitch(Column newColumn) {
		if (column == newColumn) {
			return;
		} else {
			column = newColumn;
		}

		llPubSinger.setVisibility(View.GONE);
		llWineSet.setVisibility(View.GONE);
		llBarInfo.setVisibility(View.GONE);
		//hotChatLayout.setVisibility(View.GONE);
		mHotChatList.setVisibility(View.GONE);
		mHotChatSend.setVisibility(View.GONE);
		
		llOnlineUser.setVisibility(View.GONE);

		btHotChat.setBackgroundResource(R.drawable.bg_hot_chat_normal);
		btBarDetail.setBackgroundResource(R.drawable.bg_detail_normal);
		btBarWine.setBackgroundResource(R.drawable.bg_wine_normal);
		btBarSinger.setBackgroundResource(R.drawable.bg_singer_normal);
		btOnlineUser.setBackgroundResource(R.drawable.bg_online_normal);
		btOnlineUser.setTextColor(getResColor(R.color.pub_online_normal));

		switch (column) {
		case CHAT:
			//hotChatLayout.setVisibility(View.VISIBLE);
			mHotChatList.setVisibility(View.VISIBLE);
			mHotChatSend.setVisibility(View.VISIBLE);
			btHotChat.setBackgroundResource(R.drawable.bg_hot_chat_selected);
			break;
		case DETAIL:
			llBarInfo.setVisibility(View.VISIBLE);
			btBarDetail.setBackgroundResource(R.drawable.bg_detail_selected);
			break;
		case WINE:
			llWineSet.setVisibility(View.VISIBLE);
			btBarWine.setBackgroundResource(R.drawable.bg_wine_selected);
			break;
		case SINGER:
			llPubSinger.setVisibility(View.VISIBLE);
			btBarSinger.setBackgroundResource(R.drawable.bg_singer_selected);
			break;
		case ONLINE:
			llOnlineUser.setVisibility(View.VISIBLE);
			btOnlineUser.setBackgroundResource(R.drawable.bg_online_selected);
			btOnlineUser.setTextColor(getResColor(R.color.pub_online_selected));
			break;
		}
	}

	class PubEnvironmentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return listPubEnvironment.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 通过图片url显示酒吧环境图片
			ImageView imageView = new ImageView(context);
			String barImgUrl = listPubEnvironment.get(position).getBarImgUrl();
			ImageLoaderUtil.getImageLoader().displayImage(
					DisplayUtils.getAbsolutelyUrl(barImgUrl),
					imageView,
					ImageLoaderUtil.getOptions(R.drawable.pub_default_img,
							R.drawable.pub_default_img,
							R.drawable.pub_default_img));
			container.addView(imageView);
			return imageView;
		}
	}

	/*
	 * 
	 * Below is chat related code segment
	 */
	class NewMessageBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtil.printPushLog("received new message");

			if (MessageUtil.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {

				BarChatModel model = MessageUtil.parseMessage(intent);
				String userNum = model.getUserNum();
				setOnlineUserNum(userNum);

				if (model.isReceiver()
						|| !MessageUtil.ENABLE_LOCAL_SEND_OUT_MESSAGE) {
					chatMsgList.add(model);

					// 通知chatMsgAdapter有新消息，更新ui
					chatMsgAdapter.refresh();
					chatListView.setSelection(chatListView.getCount() - 1);
				}

			}
		}
	}

	private void initChatView() {
		//hotChatLayout = (RelativeLayout) findViewById(R.id.hotChatLayout);
		
		
		mHotChatList = (RelativeLayout) findViewById(R.id.rl_chat_listview);
		mHotChatSend = (LinearLayout) findViewById(R.id.bar_bottom);

		chatListView = (ListView) findViewById(R.id.list);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);

		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);

		buttonSend = findViewById(R.id.btn_send);

		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_normal.setVisibility(View.VISIBLE);

		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
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

		emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);

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
				emojiIconContainer.setVisibility(View.GONE);

			}
		});
		// 监听文字框
		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					buttonSend.setVisibility(View.VISIBLE);
				} else {
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

	private void initChatData() {
		// set the first function to be red.
		btHotChat.setTextColor(Color.RED);

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

		// msg register and tag setting
		registerMessageReceiver();
		PushService.setPushTag(this.barId + "");
		addBarUserRequest();
	}

	private void registerMessageReceiver() {
		chatMsgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MessageUtil.MESSAGE_RECEIVED_ACTION);
		registerReceiver(chatMsgReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		LogUtil.printPushLog("PubDetails onDestory");
		super.onDestroy();

		if (!isSetAddressMode) {
			// 注销广播
			try {
				LogUtil.printPushLog("clean up");

				// set
				PushService.pauseBarPush();
				removeBarUserRequest();

				unregisterReceiver(chatMsgReceiver);
				chatMsgReceiver = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onResume() {
		LogUtil.printPushLog("PubDetails onDestory");

		super.onResume();
		if (!isSetAddressMode) {
			chatMsgAdapter.refresh();
		}

	}

	/**
	 * 消息图标点击事件:btn_send, iv_emoticons_normal, iv_emoticons_checked
	 * 
	 * @param view
	 */
	@Override
	public void onClick(View view) {

		int id = view.getId();
		if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
			if (emojiIconContainer.getVisibility() == View.VISIBLE) {
				restoreEmotionState();
			}

			String s = mEditTextContent.getText().toString();
			if (s.length() > 0) {
				sendText(s);
			}

		} else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框

			iv_emoticons_normal.setVisibility(View.INVISIBLE);
			iv_emoticons_checked.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.VISIBLE);

			hideKeyboard();
		} else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
			restoreEmotionState();
		}
	}

	private void restoreEmotionState() {
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		emojiIconContainer.setVisibility(View.GONE);
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
		UmengUtil.onEvent(this, "chat_content_sum");
		LogUtil.printUmengLog("chat_content_sum");
		BarChatModel model = MessageUtil.createNewMessage(this.barId + "",
				MessageUtil.MESSAGE_TYPE_TXT, content);
		if (MessageUtil.ENABLE_LOCAL_SEND_OUT_MESSAGE) {
			MessageUtil.sendNewMessageRequest(model);
		}

		// add the message immediately
		chatMsgList.add(model);

		// 通知chatMsgAdapter有消息变动，chatMsgAdapter会根据加入的这条message显示消息和调用sdk的发送方法
		chatMsgAdapter.refresh();
		chatListView.setSelection(chatListView.getCount() - 1);
		mEditTextContent.setText("");
	}

	private void displayWellcomeMessage() {
		String userName = getString(R.string.system);
		String content = getString(R.string.welcomeToBar) + this.pubNameStr;
		BarChatModel model = MessageUtil
				.createWelcomeMessage(userName, content);

		// add the message immediately
		chatMsgList.add(model);
		chatMsgAdapter.refresh();
	}

	/**
	 * 点击文字输入框
	 * 
	 * @param v
	 */
	public void editClick(View v) {
		chatListView.setSelection(chatListView.getCount() - 1);

		if (emojiIconContainer.getVisibility() == View.VISIBLE) {
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		}

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
									PubDetailsActivity.this,
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

	private List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;
			reslist.add(filename);
		}
		return reslist;
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

	/*
	 * Below is online segment
	 */

	private void initOnlineUserView() {

		llOnlineUser = (LinearLayout) findViewById(R.id.llOnlineUser);
		lvOnlineUser = (ListView) findViewById(R.id.lvOnlineUser);
	}

	private void initOnlineUserData() {
		lvOnlineUser
				.setAdapter(onlineUserAdapter = onlineUserAdapter == null ? new OnlineUserAdapter(
						PubDetailsActivity.this,
						listOnlineUser = listOnlineUser == null ? new ArrayList<OnlineUserModel>()
								: listOnlineUser)
						: onlineUserAdapter);

		lvOnlineUser.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LogUtil.printPushLog("item clicked:" + position);
				if (position >= listOnlineUser.size()) {
					return;
				}

				Intent intent = new Intent(PubDetailsActivity.this,
						PerssonalInfoAcitivity.class);
				intent.putExtra("userId", listOnlineUser.get(position)
						.getUserId());
				context.startActivity(intent);

			}
		});

	}

	/* add user to bar chat */
	private void addBarUserRequest() {
		LogUtil.printPushLog("enter addBarUserRequest");

		JSONObject ob = new JSONObject();
		ob.put("barId", this.barId);
		ob.put("userId", this.userId);

		String userName = AppContext.getApplication().getUserInfo()
				.getUserName();
		String userLogoUrl = AppContext.getApplication().getUserInfo()
				.getUserLogoUrl();
		String userSex = AppContext.getApplication().getUserInfo().getUserSex();
		String userAge = AppContext.getApplication().getUserInfo().getUserAge();
		if ("男".equalsIgnoreCase(userSex)) {
			ob.put("userSex", 1);
		} else {
			ob.put("userSex", 2);
		}

		ob.put("userName", userName);
		ob.put("userLogoUrl", userLogoUrl);
		ob.put("userAge", userAge);

		HttpRequestFactory.getInstance().postRequest(Urls.BAR_CHAT_ADD_USER,
				ob, addBarUserHandler);
	}

	AsyncHttpResponseHandler addBarUserHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(String content) {
			LogUtil.printPushLog("on addBarUserHandler success");

			CommonModel rm = new CommonModel();
			rm = JSON.parseObject(content, CommonModel.class);

			if (rm != null && rm.getCode() != null
					&& "200".equalsIgnoreCase(rm.getCode())) {
				// update onlineUser
				getOnlineUserRequest();

			} else {
				LogUtil.printPushLog("code is not correct");
			}

		};

		@Override
		public void onFailure(Throwable error) {
			LogUtil.printPushLog("on addBarUserHandler failure");

		};
	};

	/* remove user from bar chat */
	private void removeBarUserRequest() {
		LogUtil.printPushLog("enter removeBarUserRequest");

		JSONObject ob = new JSONObject();
		ob.put("barId", this.barId);
		ob.put("userId", this.userId);

		HttpRequestFactory.getInstance().postRequest(Urls.BAR_CHAT_ADD_USER,
				ob, removeBarUserHandler);
	}

	AsyncHttpResponseHandler removeBarUserHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(String content) {
			LogUtil.printPushLog("on removeBarUserHandler success");

			super.onSuccess(content);

			CommonModel rm = new CommonModel();
			rm = JSON.parseObject(content, CommonModel.class);

			if (rm != null && rm.getCode() != null
					&& "200".equalsIgnoreCase(rm.getCode())) {
				LogUtil.printPushLog("succeeded to remove user");

			} else {
				LogUtil.printPushLog("code is not correct");
			}
		};

		@Override
		public void onFailure(Throwable error) {
			LogUtil.printPushLog("on removeBarUserHandler failure");

		};
	};

	/* get all online users */
	private void getOnlineUserRequest() {
		LogUtil.printPushLog("enter getOnlineUserRequest");
		JSONObject ob = new JSONObject();

		ob.put("barId", this.barId);
		HttpRequestFactory.getInstance().postRequest(Urls.BAR_CHAT_GET_USER,
				ob, getOnlineUserHandler);
	}

	AsyncHttpResponseHandler getOnlineUserHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			// showLoadingDialog();
		};

		@Override
		public void onSuccess(String content) {
			LogUtil.printPushLog("on getOnlineUserRequest success");
			
			BaseModel<OnlineUserInfoModel> userInfoModel = JSON.parseObject(
					content,
					new TypeReference<BaseModel<OnlineUserInfoModel>>() {
					});
			if (userInfoModel != null
					&& userInfoModel.getCode().equalsIgnoreCase(
							GlobleConstant.REQUEST_SUCCESS)) {

				String userNum = userInfoModel.getResult().getUserNum();
				setOnlineUserNum(userNum);

				List<OnlineUserModel> userList = userInfoModel.getResult()
						.getUserList();
				if (userList == null || userList.size() <= 0
						|| userList.get(0) == null) {
					LogUtil.printPushLog("no user in the bar");
					return;
				} else {
					LogUtil.printPushLog("user num:" + userList.size());
					listOnlineUser.clear();
					for (Iterator<OnlineUserModel> it = userList.iterator(); it
							.hasNext();) {
						OnlineUserModel model = it.next();
						listOnlineUser.add(model);
					}

					onlineUserAdapter.notifyDataSetChanged();
					LogUtil.printPushLog("notify data changed");
				}

			} else {
				LogUtil.printPushLog("code is not correct");
			}
		};

		@Override
		public void onFailure(Throwable error) {
			LogUtil.printPushLog("on getOnlineUserRequest failure");

		};

		@Override
		public void onFinish() {
			// dismissLoadingDialog();
		};
	};
	private RelativeLayout mHotChatList;
	private LinearLayout mHotChatSend;

	private void setOnlineUserNum(String userNum) {
		if (TextUtils.isEmpty(userNum) || userNum.equals("Null")) {
			userNum = "1";
		}

		String str = getString(R.string.online);
		// btOnlineUser.setText(str + "(" + userNum + ")");
		btOnlineUser.setText(userNum);
	}

}
