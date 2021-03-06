package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.MessageMenuAdapter;
import com.fullteem.yueba.app.adapter.NearByFriendsListAdapter;
import com.fullteem.yueba.db.DBFriendListDao;
import com.fullteem.yueba.engine.push.PushService;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.NearbyPubFriendsModel;
import com.fullteem.yueba.model.NearbyPubFriendsModel.PubFriendsModel;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.AppUtil;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SharePreferenceUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.TopTitleView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;
import com.fullteem.yueba.widget.filter.NearbyBarFriendsPopWindow;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月4日&emsp;下午6:13:59
 * @use 发现模块-附近friend
 */
public class DiscoverNearbyFriendActivity extends BaseActivity {

	public DiscoverNearbyFriendActivity() {
		super(R.layout.activity_discover_nearby_friends);
	}

	// for title
	private static NearbyBarFriendsPopWindow popMenuFriends;// 弹出选择框->吧有界面
	private static PopupWindow mPopupWindow; // 弹出框
	private TopTitleView topTitle;
	// private View rightViewFriends;
	// private TextView leftTvFriends;
	// private TextView tvFriends;

	private String[] menuTexts;// top right is a list.
	private PopupWindow morePopupWindow;
	private View menuView;
	private ListView listviewMsgMenus = null;
	private MessageMenuAdapter mma;

	private List<PubFriendsModel> listFriends;
	private XListView xListViewFriends;
	private static final int BAR_FRIENDS_BACK = 0X123;
	private NearByFriendsListAdapter nearBylistAdapter;
	private NearbyPubFriendsModel npf;
	private AppContext appContext;
	private View lvLoading;

	private int pageSize = 10;// 默认10条为1页
	private boolean isRefresh = false;
	private int currentPosition;

	@Override
	public void initViews() {
		initTitle();

		xListViewFriends = (XListView) findViewById(R.id.listViewBarFriends);

		EmptyView emptyView = new EmptyView(this);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) xListViewFriends.getParent()).addView(emptyView);
		xListViewFriends.setEmptyView(emptyView);

		lvLoading = findViewById(R.id.lvLoading);
		lvLoading.setVisibility(View.GONE);
	}

	private void initTitle() {
		topTitle = (TopTitleView) findViewById(R.id.top_title);

		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// center
		topTitle.showCenterText(getResString(R.string.discover_nearbyFriend),
				null);

		// right
		topTitle.showRightImag(R.drawable.chat_more_icon,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						moreWindowShow();
					}
				});

		/*
		 * // right rightViewFriends = new
		 * LinearLayout(DiscoverNearbyFriendActivity.this); ((LinearLayout)
		 * rightViewFriends).setOrientation(LinearLayout.HORIZONTAL);
		 * 
		 * tvFriends = new TextView(DiscoverNearbyFriendActivity.this);
		 * tvFriends.setTextAppearance(DiscoverNearbyFriendActivity.this,
		 * R.style.toptitle_right);
		 * 
		 * String cityName =
		 * SharePreferenceUtil.getInstance(DiscoverNearbyFriendActivity
		 * .this).getSettingCityName(); tvFriends.setText(cityName);
		 * 
		 * LayoutParams lpFriends = new LayoutParams(LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT); lpFriends.setMargins(0, 0,
		 * DisplayUtils.dp2px(DiscoverNearbyFriendActivity.this, 4), 0);
		 * ((LinearLayout) rightViewFriends).addView(tvFriends, lpFriends);
		 * 
		 * ImageView ivFriends = new
		 * ImageView(DiscoverNearbyFriendActivity.this);
		 * ivFriends.setScaleType(ScaleType.CENTER);
		 * ivFriends.setImageResource(R.drawable.down_arrow_icon);
		 * ((LinearLayout) rightViewFriends).addView(ivFriends);
		 * 
		 * // left leftTvFriends = new
		 * TextView(DiscoverNearbyFriendActivity.this);
		 * leftTvFriends.setTextAppearance(DiscoverNearbyFriendActivity.this,
		 * R.style.toptitle_left);
		 * leftTvFriends.setText(getString(R.string.filter));
		 * 
		 * leftTvSinger.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { showFilterPopup(); } });
		 * 
		 * tvFriends.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { //startActivity(new
		 * Intent(getActivity(), CityActivity.class)); Intent intent = new
		 * Intent(DiscoverNearbyFriendActivity.this, CityActivity.class);
		 * startActivityForResult(intent, GlobleConstant.ACTION_HOT_CITY); } });
		 * 
		 * // add topTitle.addViewToLeft(leftTvFriends);
		 * topTitle.addViewToRight(rightViewFriends);
		 */

		// popwindow默认标题
		menuTexts = new String[2];
		menuTexts[0] = getString(R.string.filter);
		// menuTexts[1] =
		// SharePreferenceUtil.getInstance(DiscoverNearbyFriendActivity.this).getSettingCityName();//getString(R.string.city_title);
		menuTexts[1] = getString(R.string.discover_myFollow);
	}

	private void showFilterPopup() {
		if (popMenuFriends != null) {
			popMenuFriends.initCkbs();
		} else {
			popMenuFriends = new NearbyBarFriendsPopWindow(
					DiscoverNearbyFriendActivity.this);

			popMenuFriends.setOnCkbEventListener(
					popMenuFriends.new OnCkbEventListener() {
						@Override
						public void onClick(View v) {
							super.onClick(v);
							onFilterClick(v);
						}
					}, popMenuFriends.new OnCkbEventListener() {
						@Override
						public void onClick(View v) {
							super.onClick(v);
							onFilterClick(v);
						}
					}, popMenuFriends.new OnCkbEventListener() {
						@Override
						public void onClick(View v) {
							super.onClick(v);
							onFilterClick(v);
						}
					});
		}

		mPopupWindow = popMenuFriends.getMenu();
		mPopupWindow.setOutsideTouchable(true);
		if (mPopupWindow == null) {
			return;
		}

		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			return;
		}
		mPopupWindow.showAtLocation(topTitle, Gravity.CENTER, 0, 0);
	}

	@Override
	public void initData() {
		appContext = (AppContext) this.getApplication();
		listFriends = new ArrayList<NearbyPubFriendsModel.PubFriendsModel>();

		nearBylistAdapter = new NearByFriendsListAdapter(this, listFriends);
		xListViewFriends.setAdapter(nearBylistAdapter);

		// 发送请求
		getPubFriendsRequest();
	}

	@Override
	public void bindViews() {
		xListViewFriends.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				listFriends.clear();
				isRefresh = true;
				getPubFriendsRequest();
			}

			@Override
			public void onLoadMore() {
				isRefresh = false;
				getPubFriendsRequest();
			}
		});
		xListViewFriends.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0 || position == listFriends.size() + 1)
					return;
				UmengUtil.onEvent(DiscoverNearbyFriendActivity.this, "friend_chat_hits");
				LogUtil.printUmengLog("friend_chat_hits");
				currentPosition = position - 1;
				Intent intent = new Intent(DiscoverNearbyFriendActivity.this,
						ChatActivity.class);
				intent.putExtra("userId", listFriends.get(currentPosition)
						.getImServerId());
				intent.putExtra("nickname", listFriends.get(currentPosition)
						.getUserName());
				intent.putExtra("imgurl", listFriends.get(currentPosition)
						.getUserLogoUrl());

				// 直接在跳转之前就在数据库插入信息
				new Thread(new Runnable() {
					@Override
					public void run() {
						DBFriendListDao dao = new DBFriendListDao(
								DiscoverNearbyFriendActivity.this);
						UserCommonModel user = new UserCommonModel();
						user.setUserMobile(listFriends.get(currentPosition)
								.getUserMobile());
						user.setImServerId(listFriends.get(currentPosition)
								.getImServerId());
						user.setUserId(listFriends.get(currentPosition)
								.getUserId());
						user.setUserName(listFriends.get(currentPosition)
								.getUserName());
						String imgUrl;
						if (listFriends.get(currentPosition).getUserLogoUrl() == null) {
							imgUrl = "null";
						} else {
							imgUrl = listFriends.get(currentPosition)
									.getUserLogoUrl();
						}
						user.setUserLogoUrl(imgUrl);
						dao.saveContacter(user);
					}
				}).start();
				startActivity(intent);

			}
		});

	}

	/**
	 * 展示window
	 */
	private void moreWindowShow() {
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int windowWidth = windowManager.getDefaultDisplay().getWidth();

		if (morePopupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			menuView = layoutInflater.inflate(R.layout.popwindow_msgmenu, null);
			listviewMsgMenus = (ListView) menuView
					.findViewById(R.id.listviewMsgPop);
			mma = new MessageMenuAdapter(this, menuTexts);
			listviewMsgMenus.setAdapter(mma);
			// 创建一个PopuWidow对象
			morePopupWindow = new PopupWindow(menuView, windowWidth / 2, 500);
		}

		// 使其聚集
		morePopupWindow.setFocusable(true);
		// 设置允许在外点击消失
		morePopupWindow.setOutsideTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		morePopupWindow.setBackgroundDrawable(new BitmapDrawable());

		// 显示的位置为:
		int xPos = windowWidth / 2;

		Log.i("coder", "xPos:" + xPos);

		morePopupWindow.showAsDropDown(topTitle, xPos, 0);
		listviewMsgMenus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				if (morePopupWindow != null) {
					morePopupWindow.dismiss();
				}

				if (position == 0) {
					showFilterPopup();
				} else if (position == 1) {
					// Intent intent = new
					// Intent(DiscoverNearbyFriendActivity.this,
					// CityActivity.class);
					// startActivityForResult(intent,
					// GlobleConstant.ACTION_HOT_CITY);
					Intent intent = new Intent(
							DiscoverNearbyFriendActivity.this,
							MyBarFriendListActivity.class);
					startActivity(intent);
				}
			}

		});

	}

	/**
	 * 获取吧友请求
	 */
	private void getPubFriendsRequest() {
		// ((BaseActivity) this).showLoadingDialog("正在获取");
		LogUtil.printPushLog("enter getPubFriendsRequest");

		JSONObject job = new JSONObject();
		// 城市ID目前写死
		//job.put("cityId", 1);
		job.put("userId", appContext.getUserInfo().getUserId());
		job.put("pageNumber", AppUtil.getPageNum(listFriends.size(), pageSize));
		job.put("pageSize", pageSize);
		job.put("lat", appContext.getLocation().getLatitude());
		job.put("lng", appContext.getLocation().getLongitude());
		// 吧友列表查询 性别参数 userSex (1:男；2女,不限就不传递任何参数)
		int filterIndex = SharePreferenceUtil.getInstance(this)
				.getIntFromShare(GlobleConstant.BARFRIENDS_FILTER,
						GlobleConstant.BARFRIENDS_FILTER_DEFAULT);
		if (filterIndex == 0)
			job.put("userSex", 2);
		else if (filterIndex == 1)
			job.put("userSex", 1);

		String city = SharePreferenceUtil.getInstance(
				AppContext.getApplication()).getSettingCityId();
		job.put("cityId", city);

		HttpRequestFactory.getInstance().getRequest(Urls.BAR_FRIENDS, job,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

						LogUtil.printPushLog("onSuccess getPubFriendsRequest");
						npf = new NearbyPubFriendsModel();
						// npfm = npf.new PubFriendsModel();
						npf = JsonUtil.convertJsonToObject(content,
								NearbyPubFriendsModel.class);
						if (npf != null && npf.getResult() != null) {
							listFriends.addAll(npf.getResult());

						}
						LogUtil.printPushLog("listFriends size:"
								+ listFriends.size());
						UIhandler.sendEmptyMessage(BAR_FRIENDS_BACK);
					}

					@Override
					public void onFailure(Throwable error) {
						LogUtil.printPushLog("onFailure getPubFriendsRequest");
						super.onFailure(error);
					}

					@Override
					public void onStart() {
						if (listFriends == null || listFriends.size() <= 0) {
							lvLoading.setVisibility(View.VISIBLE);
							xListViewFriends.setVisibility(View.GONE);
						}
					};

					@Override
					public void onFinish() {
						super.onFinish();
						LogUtil.printPushLog("onFinish getPubFriendsRequest");

						lvLoading.setVisibility(View.GONE);
						xListViewFriends.setVisibility(View.VISIBLE);
						if (isRefresh) {
							// xListViewFriends.stopRefresh();
						}

					}

				});

	}

	Handler UIhandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case BAR_FRIENDS_BACK:
				LogUtil.printPushLog("handleMessage. case BAR_FRIENDS_BACK");
				nearBylistAdapter.notifyDataSetChanged();
				// 设置加载更多是否可见
				xListViewFriends.setPullLoadEnable(StringUtils
						.isLoadMoreVisible(listFriends.size(), pageSize));
				break;

			default:
				break;
			}
		};
	};

	private void onFilterClick(View view) {
		listFriends.clear();
		getPubFriendsRequest();
	}

	@Override
	public void onResume() {
		super.onResume();
		PushService.onResume("friend", this);
		UmengUtil.onPageStart(this,"SplashScreen");
	}

	@Override
	public void onPause() {
		super.onPause();
		PushService.onPause("friend", this);
		UmengUtil.onPageEnd(this,"SplashScreen");
	}

}