package com.fullteem.yueba.app.ui.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.MessageMenuAdapter;
import com.fullteem.yueba.app.ui.CityActivity;
import com.fullteem.yueba.app.ui.DateManageActivity;
import com.fullteem.yueba.app.ui.LuckyMeetActivity;
import com.fullteem.yueba.app.ui.OrderManageActivity;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SharePreferenceUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.TimeCount;
import com.fullteem.yueba.widget.TopTitleView;
import com.fullteem.yueba.widget.filter.NearbyDatePopWindow;
import com.fullteem.yueba.widget.filter.NearbyPubPopWindow;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月26日&emsp;下午3:21:25
 * @use 主界面->附近
 */
public class NearbyMainFragment extends Fragment {

	private static NearbyMainFragment instance;

	public static NearbyMainFragment getInstance() {
		if (instance == null) {
			instance = new NearbyMainFragment();
		}
		return instance;
	}
	
	public NearbyMainFragment() {
	}

	private ViewPager mViewPager;
	private List<Fragment> mNearbyFragments;
	private NewabyFragmentsAdapter mAdapter;
	private TopTitleView topTitle;
	private EventListener mListener;
	// private List<Button> btnTitles;// Button组实现的标题栏
	private int lastIndex = -1; // 上次选中的位置

	// for title
	// private static NearbyBarFriendsPopWindow popMenuFriends;// 弹出选择框->吧有界面
	private static NearbyPubPopWindow popMenuPub;// 弹出选择框->酒吧界面
	// private static NearbySingerPopWindow popMenuSinger;// 弹出选择框->歌手界面
	private static NearbyDatePopWindow popMenuDate;// 弹出选择框->约会界面
	private static PopupWindow mPopupWindow; // 弹出框
	private View rightViewPub, rightViewDate;
	// private View rightViewFriends,rightViewSinger;
	private TextView tvDateUpdateHint; // 约会小提醒
	private TextView leftTvDate, leftTvPub, rightTvDate;
	// private TextView leftTvSinger, leftTvFriends;

	// date, pub
	private TextView centerTvDate, centerTvPub;

	/** 附近模块几个页面主页一次性请求分页大小 */
	private final int PAGE_SIZE = 10;
	
	private final int DATE_PAGE_SIZE = 5;

	private static TimeCount mTimeCount;
	// private LinearLayout mPointGroup;
	// private int previousPoint;
	private String[] titleArrays;
	private ImageView rightShowMore;
	// private TextView tvFriends;

	// below is something about morePopupWindow
	private PopupWindow morePopupWindow;
	private View menuView;
	private ListView listviewMsgMenus = null;
	private MessageMenuAdapter mma;
	private String[] menuTexts;// top right is a list.

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_nearby_main, container,
				false);
		initViews(view);
		initData();
		bindViews();
		return view;
	}

	private void initViews(View view) {
		menuTexts = new String[2];
		menuTexts[0] = getString(R.string.group_create);
		menuTexts[1] = SharePreferenceUtil.getInstance(getActivity())
				.getSettingCityName();// getString(R.string.city_title);

		mViewPager = (ViewPager) view.findViewById(R.id.vp_fragment_container);
		topTitle = (TopTitleView) view.findViewById(R.id.toptile);

		/*
		 * ================================修改代码：改为小红点============================
		 * ============
		 */

		/*
		 * int btnId[] = { R.id.btn_nearby_date, R.id.btn_nearby_pub}; btnTitles
		 * = new ArrayList<Button>(btnId.length); for (int i = 0; i <
		 * btnId.length; i++) { Button btn = (Button)
		 * view.findViewById(btnId[i]); btnTitles.add(btn); }
		 */

		// mPointGroup = (LinearLayout) view.findViewById(R.id.ll_point_group);

		initTopTitle();

		/*
		 * previousPoint = 0;
		 * mPointGroup.getChildAt(previousPoint).setEnabled(true);
		 */

		/*
		 * ================================修改代码：改为小红点============================
		 * ============
		 */
	}

	private void initData() {
		/*
		 * ================================修改代码：约会放在第一项，去掉歌手======================
		 * ==================
		 */
		mNearbyFragments = new ArrayList<Fragment>();
		NearbyDateFrgment nearbyDateFrgment = new NearbyDateFrgment();
		nearbyDateFrgment.setPageSize(DATE_PAGE_SIZE);
		mNearbyFragments.add(nearbyDateFrgment);

		NearbyPubFrgment nearbyPubFrgment = new NearbyPubFrgment();
		nearbyPubFrgment.setPageSize(PAGE_SIZE);
		mNearbyFragments.add(nearbyPubFrgment);

		/*
		 * remove NearbySingerFrgment and nearbyFriendsFrgment and place them
		 * into 2nd level.
		 */
		/*
		 * NearbySingerFrgment nearbySingerFrgment = new NearbySingerFrgment();
		 * nearbySingerFrgment.setPageSize(PAGE_SIZE);
		 * mNearbyFragments.add(nearbySingerFrgment);
		 * 
		 * NearbyFriendsFrgment nearbyFriendsFrgment = new
		 * NearbyFriendsFrgment(); nearbyFriendsFrgment.setPageSize(PAGE_SIZE);
		 * mNearbyFragments.add(nearbyFriendsFrgment);
		 */

		mAdapter = new NewabyFragmentsAdapter(getChildFragmentManager());
		mViewPager.setAdapter(mAdapter);

		mListener = new EventListener();
		/*
		 * ================================修改代码：约会放在第一项，去掉歌手======================
		 * ==================
		 */

	}

	private void bindViews() {
		mViewPager.setOnPageChangeListener(mListener);
		/*
		 * for (int i = 0; i < btnTitles.size(); i++) {
		 * btnTitles.get(i).setOnClickListener(mListener); }
		 */
	}

	private void initTopCenter() {
		// topTitle.showCenterText(titleArrays[previousPoint], null);
		View centerView = new LinearLayout(getActivity());
		((LinearLayout) centerView).setOrientation(LinearLayout.HORIZONTAL);

		centerTvDate = new TextView(getActivity());
		centerTvDate.setTextAppearance(getActivity(), R.style.toptitle_center);
		centerTvDate.setText(titleArrays[0]);

		centerTvPub = new TextView(getActivity());
		centerTvPub.setTextAppearance(getActivity(), R.style.toptitle_center);
		centerTvPub.setText(titleArrays[1]);

		LayoutParams lpParam = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lpParam.setMargins(0, 0, DisplayUtils.dp2px(getActivity(), 20), 0);

		((LinearLayout) centerView).addView(centerTvDate, lpParam);
		((LinearLayout) centerView).addView(centerTvPub, lpParam);

		topTitle.addViewToCenter(centerView);

		centerTvDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cehekedStateChange(0);
				mViewPager.setCurrentItem(0, false);
			}
		});

		centerTvPub.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cehekedStateChange(1);
				mViewPager.setCurrentItem(1, false);
			}
		});
	}

	private void initTopTitle() {
		/*
		 * ================================修改代码：初始化滑动小红点==========================
		 * ==============
		 */
		titleArrays = getActivity().getResources().getStringArray(
				R.array.nearby_top_title);

		/*
		 * View mPointView; for (int i = 0; i < titleArrays.length; i++) {
		 * mPointView = new View(getActivity());
		 * mPointView.setBackgroundResource(R.drawable.point_bg);
		 * LinearLayout.LayoutParams params = new LayoutParams(10, 10); if(i !=
		 * 0){ params.leftMargin = 8; } mPointView.setLayoutParams(params);
		 * mPointView.setEnabled(false); mPointGroup.addView(mPointView); }
		 */

		/*
		 * ================================修改代码：初始化滑动小红点==========================
		 * ==============
		 */

		// 附近吧友
		/*
		 * // right rightViewFriends = new LinearLayout(getActivity());
		 * ((LinearLayout)
		 * rightViewFriends).setOrientation(LinearLayout.HORIZONTAL); tvFriends
		 * = new TextView(getActivity());
		 * tvFriends.setTextAppearance(getActivity(), R.style.toptitle_right);
		 * String cityName =
		 * SharePreferenceUtil.getInstance(getActivity()).getSettingCityName();
		 * tvFriends.setText(cityName); LayoutParams lpFriends = new
		 * LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 * lpFriends.setMargins(0, 0, DisplayUtils.dp2px(getActivity(), 4), 0);
		 * ((LinearLayout) rightViewFriends).addView(tvFriends, lpFriends);
		 * ImageView ivFriends = new ImageView(getActivity());
		 * ivFriends.setScaleType(ScaleType.CENTER);
		 * ivFriends.setImageResource(R.drawable.down_arrow_icon);
		 * ((LinearLayout) rightViewFriends).addView(ivFriends);
		 * 
		 * // left leftTvFriends = new TextView(getActivity());
		 * leftTvFriends.setTextAppearance(getActivity(),
		 * R.style.toptitle_left);
		 * leftTvFriends.setText(getString(R.string.filter));
		 */

		// 酒吧
		// right
		rightViewPub = new TextView(getActivity());
		((TextView) rightViewPub).setTextAppearance(getActivity(),
				R.style.toptitle_right);
		((TextView) rightViewPub).setText(getString(R.string.myWine));

		// left
		leftTvPub = new TextView(getActivity());
		leftTvPub.setTextAppearance(getActivity(), R.style.toptitle_left);
		leftTvPub.setText(getString(R.string.filter));

		// 歌手
		/*
		 * // right rightViewSinger = new LinearLayout(getActivity());
		 * ((LinearLayout)
		 * rightViewSinger).setOrientation(LinearLayout.HORIZONTAL); TextView
		 * tvSinger = new TextView(getActivity());
		 * tvSinger.setTextAppearance(getActivity(), R.style.toptitle_right);
		 * tvSinger.setText("广州"); LayoutParams lpSinger = new
		 * LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 * lpSinger.setMargins(0, 0, DisplayUtils.dp2px(getActivity(), 4), 0);
		 * ((LinearLayout) rightViewSinger).addView(tvSinger, lpSinger);
		 * ImageView ivSinger = new ImageView(getActivity());
		 * ivSinger.setScaleType(ScaleType.CENTER);
		 * ivSinger.setImageResource(R.drawable.down_arrow_icon);
		 * ((LinearLayout) rightViewSinger).addView(ivSinger);
		 * 
		 * // left leftTvSinger = new TextView(getActivity());
		 * leftTvSinger.setTextAppearance(getActivity(), R.style.toptitle_left);
		 * leftTvSinger.setText(getString(R.string.filter));
		 */

		// 约会
		// right
		/*
		 * rightViewDate = View.inflate(getActivity(),
		 * R.layout.top_nearby_date_right, null); tvDateUpdateHint = (TextView)
		 * rightViewDate.findViewById(R.id.tv_datehint); LinearLayout
		 * lvDateManage = (LinearLayout)
		 * rightViewDate.findViewById(R.id.lv_date_manage);
		 */
		// ImageView ivDateRelease = (ImageView)
		// rightViewDate.findViewById(R.id.iv_date_release);
		rightTvDate = new TextView(getActivity());
		rightTvDate.setTextAppearance(getActivity(), R.style.toptitle_right);
		rightTvDate.setText(getString(R.string.date_manager));

		/*
		 * rightShowMore = new ImageView(getActivity());
		 * rightShowMore.setScaleType(ScaleType.CENTER);
		 * rightShowMore.setBackgroundResource(R.drawable.chat_more_icon);
		 */

		// left
		leftTvDate = new TextView(getActivity());
		leftTvDate.setTextAppearance(getActivity(), R.style.toptitle_left);
		leftTvDate.setText(getString(R.string.filter));

		// center
		initTopCenter();

		// add left and right

		// topTitle.addViewToLeft(leftTvFriends);
		topTitle.addViewToLeft(leftTvPub);
		// topTitle.addViewToLeft(leftTvSinger);
		topTitle.addViewToLeft(leftTvDate);

		// topTitle.addViewToRight(rightViewFriends);
		topTitle.addViewToRight(rightViewPub);
		// topTitle.addViewToRight(rightViewSinger);
		topTitle.addViewToRight(rightTvDate);

		// 点击事件
		/*
		 * tvFriends.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { //startActivity(new
		 * Intent(getActivity(), CityActivity.class)); Intent intent = new
		 * Intent(getActivity(), CityActivity.class);
		 * startActivityForResult(intent, GlobleConstant.ACTION_HOT_CITY); } });
		 */
		// TODO:
		// 约会管理
		rightTvDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UmengUtil.onEvent(getActivity(), "date_management_hits");
				LogUtil.printUmengLog("date_management_hits");
				startActivity(new Intent(getActivity(),
						DateManageActivity.class));
				// moreWindowShow();
			}
		});
		// 发布约会
		// ivDateRelease.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// startActivity(new Intent(getActivity(), DatePublishActivity.class));
		// }
		// });

		/*
		 * // 吧友筛选 leftTvFriends.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { popMenuFriends =
		 * popMenuFriends == null ? new NearbyBarFriendsPopWindow(getActivity())
		 * : popMenuFriends;
		 * popMenuFriends.setOnCkbEventListener(popMenuFriends.new
		 * OnCkbEventListener() {
		 * 
		 * @Override public void onClick(View v) { super.onClick(v);
		 * ((NearbyFriendsFrgment) mNearbyFragments.get(3)).onFilterClick(v); }
		 * }, popMenuFriends.new OnCkbEventListener() {
		 * 
		 * @Override public void onClick(View v) { super.onClick(v);
		 * ((NearbyFriendsFrgment) mNearbyFragments.get(3)).onFilterClick(v); }
		 * }, popMenuFriends.new OnCkbEventListener() {
		 * 
		 * @Override public void onClick(View v) { super.onClick(v);
		 * ((NearbyFriendsFrgment) mNearbyFragments.get(3)).onFilterClick(v); }
		 * }, popMenuFriends.new OnCkbEventListener() {
		 * 
		 * @Override public void onClick(View v) { super.onClick(v);
		 * ((NearbyFriendsFrgment) mNearbyFragments.get(3)).onFilterClick(v); }
		 * }); mPopupWindow = popMenuFriends.getMenu();
		 * mPopupWindow.setOutsideTouchable(true); if (mPopupWindow == null) {
		 * return; }
		 * 
		 * if (mPopupWindow.isShowing()) { mPopupWindow.dismiss(); return; }
		 * mPopupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0); } });
		 */
		// 酒吧筛选
		leftTvPub.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UmengUtil.onEvent(getActivity(), "bar_sift_button_hits");
				LogUtil.printUmengLog("bar_sift_button_hits");
				if (popMenuPub != null) {
					popMenuPub.initCkbs();
				} else {
					popMenuPub = new NearbyPubPopWindow(getActivity());

					popMenuPub.setOnCkbEventListener(
							popMenuPub.new OnCkbEventListener() {
								@Override
								public void onClick(View v) {
									super.onClick(v);
									UmengUtil.onEvent(getActivity(), "silent_bar_hits");
									LogUtil.printUmengLog("silent_bar_hits");
									((NearbyPubFrgment) mNearbyFragments.get(1))
											.onFilterClick(v);
								}
							}, popMenuPub.new OnCkbEventListener() {
								@Override
								public void onClick(View v) {
									super.onClick(v);
									UmengUtil.onEvent(getActivity(), "disco_bar_hits");
									LogUtil.printUmengLog("disco_bar_hits");
									((NearbyPubFrgment) mNearbyFragments.get(1))
											.onFilterClick(v);
								}
							}, popMenuPub.new OnCkbEventListener() {
								@Override
								public void onClick(View v) {
									super.onClick(v);
									UmengUtil.onEvent(getActivity(), "performance_bar_hits");
									LogUtil.printUmengLog("performance_bar_hits");
									((NearbyPubFrgment) mNearbyFragments.get(1))
											.onFilterClick(v);
								}
							}, popMenuPub.new OnCkbEventListener() {
								@Override
								public void onClick(View v) {
									super.onClick(v);
									UmengUtil.onEvent(getActivity(), "all_bar_hits");
									LogUtil.printUmengLog("all_bar_hits");
									((NearbyPubFrgment) mNearbyFragments.get(1))
											.onFilterClick(v);
								}
							});
				}

				mPopupWindow = popMenuPub.getMenu();
				mPopupWindow.setOutsideTouchable(true);
				if (mPopupWindow == null) {
					return;
				}

				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
					return;
				}
				mPopupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
			}
		});
		// 我的酒水
		rightViewPub.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),
						OrderManageActivity.class));
			}
		});

		/*
		 * // 歌手筛选 leftTvSinger.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { popMenuSinger = popMenuSinger
		 * == null ? new NearbySingerPopWindow(getActivity()) : popMenuSinger;
		 * popMenuSinger.setOnCkbEventListener(popMenuSinger.new
		 * OnCkbEventListener() {
		 * 
		 * @Override public void onClick(View v) { super.onClick(v);
		 * ((NearbySingerFrgment) mNearbyFragments.get(2)).onFilterClick(v); }
		 * }, popMenuSinger.new OnCkbEventListener() {
		 * 
		 * @Override public void onClick(View v) { super.onClick(v);
		 * ((NearbySingerFrgment) mNearbyFragments.get(2)).onFilterClick(v); }
		 * }, popMenuSinger.new OnCkbEventListener() {
		 * 
		 * @Override public void onClick(View v) { super.onClick(v);
		 * ((NearbySingerFrgment) mNearbyFragments.get(2)).onFilterClick(v); }
		 * }, popMenuSinger.new OnCkbEventListener() {
		 * 
		 * @Override public void onClick(View v) { super.onClick(v);
		 * ((NearbySingerFrgment) mNearbyFragments.get(2)).onFilterClick(v); }
		 * }); mPopupWindow = popMenuSinger.getMenu();
		 * mPopupWindow.setOutsideTouchable(true); if (mPopupWindow == null) {
		 * return; }
		 * 
		 * if (mPopupWindow.isShowing()) { mPopupWindow.dismiss(); return; }
		 * mPopupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0); } });
		 */

		// 约会筛选
		leftTvDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UmengUtil.onEvent(getActivity(), "date_sift_button_hits");
				LogUtil.printUmengLog("date_sift_button_hits");
				if (popMenuDate != null) {
					popMenuDate.initCkbs();
				} else {
					popMenuDate = new NearbyDatePopWindow(getActivity());

					popMenuDate.setOnCkbEventListener(
							popMenuDate.new OnCkbEventListener() {
								@Override
								public void onClick(View v) {
									super.onClick(v);
									UmengUtil.onEvent(getActivity(), "city_hits");
									LogUtil.printUmengLog("city_hits");
									// for city setting
									Intent intent = new Intent(
											NearbyMainFragment.this
													.getActivity(),
											CityActivity.class);
									startActivityForResult(intent,
											GlobleConstant.ACTION_HOT_CITY);
								}
							}, popMenuDate.new OnCkbEventListener() {
								@Override
								public void onClick(View v) {
									super.onClick(v);
									UmengUtil.onEvent(getActivity(), "only_woman_hits");
									LogUtil.printUmengLog("only_woman_hits");
									((NearbyDateFrgment) mNearbyFragments
											.get(0)).onFilterClick(v);
								}
							}, popMenuDate.new OnCkbEventListener() {
								@Override
								public void onClick(View v) {
									super.onClick(v);
									UmengUtil.onEvent(getActivity(), "only_man_hits");
									LogUtil.printUmengLog("only_man_hits");
									((NearbyDateFrgment) mNearbyFragments
											.get(0)).onFilterClick(v);
								}
							}, popMenuDate.new OnCkbEventListener() {
								@Override
								public void onClick(View v) {
									super.onClick(v);
									UmengUtil.onEvent(getActivity(), "unlimited_hits");
									LogUtil.printUmengLog("unlimited_hits");
									((NearbyDateFrgment) mNearbyFragments
											.get(0)).onFilterClick(v);
								}
							});
				}

				mPopupWindow = popMenuDate.getMenu();
				mPopupWindow.setOutsideTouchable(true);
				if (mPopupWindow == null) {
					return;
				}

				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
					return;
				}
				mPopupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
			}
		});

		topTitleChange(0);
	}

	/**
	 * save city after changed, and reset other filter default values
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null)
			return;
		switch (requestCode) {
		case GlobleConstant.ACTION_HOT_CITY:
			// set city
			String cityName = data.getStringExtra("cityName");
			SharePreferenceUtil.getInstance(getActivity()).setSettingCityName(
					cityName);

			String cityId = data.getStringExtra("cityId");
			SharePreferenceUtil.getInstance(getActivity()).setSettingCityId(
					cityId);

			LogUtil.printDateLog("in onActivityResult, city name:" + cityName
					+ "city id:" + cityId);

			// reset filters
			SharePreferenceUtil.getInstance(getActivity()).saveIntToShare(
					GlobleConstant.DATE_FILTER,
					GlobleConstant.DATE_FILTER_DEFAULT);
			SharePreferenceUtil.getInstance(getActivity()).saveIntToShare(
					GlobleConstant.PUB_FILTER,
					GlobleConstant.PUB_FILTER_DEFAULT);
			SharePreferenceUtil.getInstance(getActivity()).saveIntToShare(
					GlobleConstant.BARFRIENDS_FILTER,
					GlobleConstant.BARFRIENDS_FILTER_DEFAULT);
			SharePreferenceUtil.getInstance(getActivity()).saveIntToShare(
					GlobleConstant.SINGER_FILTER,
					GlobleConstant.SINGER_FILTER_DEFAULT);

			// fetch data and refresh
			((NearbyDateFrgment) mNearbyFragments.get(0)).refresh();
			((NearbyPubFrgment) mNearbyFragments.get(1)).refresh();
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 点击更多弹出popwindow()
	 */
	/*
	 * protected void moreWindowShow() { // TODO Auto-generated method stub
	 * 
	 * WindowManager windowManager = (WindowManager)
	 * getActivity().getSystemService(Context.WINDOW_SERVICE); int windowWidth =
	 * windowManager.getDefaultDisplay().getWidth();
	 * 
	 * if (morePopupWindow == null) { LayoutInflater layoutInflater =
	 * (LayoutInflater)
	 * getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE); menuView
	 * = layoutInflater.inflate(R.layout.popwindow_msgmenu, null);
	 * listviewMsgMenus = (ListView) menuView
	 * .findViewById(R.id.listviewMsgPop); mma = new
	 * MessageMenuAdapter(getActivity(), menuTexts);
	 * listviewMsgMenus.setAdapter(mma); // 创建一个PopuWidow对象 morePopupWindow =
	 * new PopupWindow(menuView, windowWidth/2, 500); }
	 * 
	 * // 使其聚集 morePopupWindow.setFocusable(true); // 设置允许在外点击消失
	 * morePopupWindow.setOutsideTouchable(true);
	 * 
	 * // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
	 * morePopupWindow.setBackgroundDrawable(new BitmapDrawable());
	 * 
	 * // 显示的位置为: int xPos = windowWidth/2;
	 * 
	 * Log.i("coder", "xPos:" + xPos);
	 * 
	 * morePopupWindow.showAsDropDown(topTitle, xPos, 0);
	 * listviewMsgMenus.setOnItemClickListener(new OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> adapterView, View view,
	 * int position, long id) {
	 * 
	 * if (morePopupWindow != null) { morePopupWindow.dismiss(); }
	 * 
	 * if (position == 0) { //showFilterPopup(); startActivity(new
	 * Intent(getActivity(), DateManageActivity.class)); } else if (position ==
	 * 1) { Intent intent = new Intent(getActivity(), CityActivity.class);
	 * startActivityForResult(intent, GlobleConstant.ACTION_HOT_CITY); }
	 * 
	 * }
	 * 
	 * }); }
	 */

	// public void setDateUpdateNum(int num) {
	// if (tvDateUpdateHint == null)
	// return;
	// if (num <= 0) {
	// tvDateUpdateHint.setVisibility(View.GONE);
	// return;
	// }
	// tvDateUpdateHint.setText(num > 99 ? "..." : "" + num);
	// }
	/**
	 * 显示吧友所在地
	 * 
	 * @Override public void onActivityResult(int requestCode, int resultCode,
	 *           Intent data) { if (data == null) return; switch (requestCode) {
	 *           case GlobleConstant.ACTION_HOT_CITY: String cityName =
	 *           data.getStringExtra("cityName"); tvFriends.setText(cityName);
	 *           SharePreferenceUtil
	 *           .getInstance(getActivity()).setSettingCityName(cityName);
	 *           break; } super.onActivityResult(requestCode, resultCode, data);
	 *           }
	 */

	private class EventListener implements OnPageChangeListener,
			OnClickListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			topTitleChange(position);

			// cehekedStateChange(position);
			/*
			 * ================================修改代码：页面改变时，对应的红点改变================
			 * ========================
			 */

			/*
			 * mPointGroup.getChildAt(currentpoint).setEnabled(true);
			 * mPointGroup.getChildAt(previousPoint).setEnabled(false);
			 * previousPoint = currentpoint;
			 * topTitle.showCenterText(titleArrays[currentpoint], null);
			 */
			/*
			 * ================================修改代码：页面改变时，对应的红点改变================
			 * ========================
			 */
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			/*
			 * case R.id.btn_nearby_friends: cehekedStateChange(0);
			 * mViewPager.setCurrentItem(0,false); break; case
			 * R.id.btn_nearby_singer: cehekedStateChange(2);
			 * mViewPager.setCurrentItem(2,false); break;
			 */

			case R.id.btn_nearby_pub:
				cehekedStateChange(1);
				mViewPager.setCurrentItem(1, false);
				break;

			case R.id.btn_nearby_date:
				cehekedStateChange(0);
				mViewPager.setCurrentItem(0, false);
				break;
			}
		}

	}

	private void cehekedStateChange(int index) {
		// if (index == lastIndex) {
		// return;
		// }
		topTitleChange(index);

		/*
		 * for (int i = 0; i < btnTitles.size(); i++) { if (i == index) {
		 * btnTitles
		 * .get(i).setTextColor(getResources().getColor(R.color.btn_nearby_selected
		 * )); int indexResId = i == 0 ? R.drawable.btn_group_left_selected : i
		 * == btnTitles.size() - 1 ? R.drawable.btn_group_right_selected :
		 * R.drawable.btn_group_center_selected;
		 * btnTitles.get(i).setBackgroundResource(indexResId); lastIndex = i;
		 * continue; }
		 * btnTitles.get(i).setTextColor(getResources().getColor(R.color
		 * .btn_nearby_unselected)); int lastResId = i == 0 ?
		 * R.drawable.btn_group_left : i == btnTitles.size() - 1 ?
		 * R.drawable.btn_group_right : R.drawable.btn_group_center;
		 * btnTitles.get(i).setBackgroundResource(lastResId); }
		 */

		// if (lastIndex == -1) {
		// lastIndex = 0;
		// }
		// btnTitles.get(lastIndex).setTextColor(getResources().getColor(R.color.btn_nearby_unselected));
		// int lastResId = lastIndex == 0 ? R.drawable.btn_group_left :
		// lastIndex == btnTitles.size() - 1 ? R.drawable.btn_group_right :
		// R.drawable.btn_group_center;
		// btnTitles.get(lastIndex).setBackgroundResource(lastResId);
		// btnTitles.get(index).setTextColor(getResources().getColor(R.color.btn_nearby_selected));
		// int indexResId = index == 0 ? R.drawable.btn_group_left_selected :
		// index == btnTitles.size() - 1 ? R.drawable.btn_group_right_selected
		// : R.drawable.btn_group_center_selected;
		// btnTitles.get(index).setBackgroundResource(indexResId);
		// lastIndex = index;
	}

	private class NewabyFragmentsAdapter extends FragmentPagerAdapter {

		public NewabyFragmentsAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return mNearbyFragments.get(position);
		}

		@Override
		public int getCount() {
			return mNearbyFragments.size();
		}
	}

	private void topTitleChange(int index) {
		if (index == 0) {
			// 约会界面
			// rightViewFriends.setVisibility(View.GONE);
			rightViewPub.setVisibility(View.GONE);
			// rightViewSinger.setVisibility(View.GONE);
			rightTvDate.setVisibility(View.VISIBLE);

			// leftTvFriends.setVisibility(View.GONE);
			leftTvPub.setVisibility(View.GONE);
			// leftTvSinger.setVisibility(View.GONE);
			leftTvDate.setVisibility(View.VISIBLE);

			/*
			 * centerTvDate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			 * centerTvDate.setTextColor(Color.WHITE);
			 * centerTvPub.getPaint().setFlags(Paint.LINEAR_TEXT_FLAG);
			 */

			centerTvDate
					.setBackgroundResource(R.drawable.nearby_top_center_selected);
			centerTvPub
					.setBackgroundResource(R.drawable.nearby_top_center_unselected);
			centerTvDate.setTextColor(getResources().getColor(
					R.color.nearby_top_center_selected));
			centerTvPub.setTextColor(getResources().getColor(
					R.color.nearby_top_center_unselected));

		} else if (index == 1) {
			// 酒吧界面
			// rightViewFriends.setVisibility(View.GONE);
			rightViewPub.setVisibility(View.VISIBLE);
			// rightViewSinger.setVisibility(View.GONE);
			rightTvDate.setVisibility(View.GONE);

			// leftTvFriends.setVisibility(View.GONE);
			leftTvPub.setVisibility(View.VISIBLE);
			// leftTvSinger.setVisibility(View.GONE);
			leftTvDate.setVisibility(View.GONE);

			/*
			 * centerTvPub.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			 * centerTvPub.setTextColor(Color.WHITE);
			 * centerTvDate.setPaintFlags(Paint.LINEAR_TEXT_FLAG);
			 */
			centerTvDate
					.setBackgroundResource(R.drawable.nearby_top_center_unselected);
			centerTvPub
					.setBackgroundResource(R.drawable.nearby_top_center_selected);
			centerTvDate.setTextColor(getResources().getColor(
					R.color.nearby_top_center_unselected));
			centerTvPub.setTextColor(getResources().getColor(
					R.color.nearby_top_center_selected));
		}/*
		 * else if (index == 2) { // 歌手界面
		 * //rightViewFriends.setVisibility(View.GONE);
		 * rightViewPub.setVisibility(View.GONE); //
		 * rightViewSinger.setVisibility(View.VISIBLE);
		 * rightTvDate.setVisibility(View.GONE);
		 * 
		 * //leftTvFriends.setVisibility(View.GONE);
		 * leftTvPub.setVisibility(View.GONE);
		 * //leftTvSinger.setVisibility(View.VISIBLE);
		 * leftTvDate.setVisibility(View.GONE);
		 * 
		 * } else if (index == 3) { // 吧友界面
		 * //rightViewFriends.setVisibility(View.VISIBLE);
		 * rightViewPub.setVisibility(View.GONE); //
		 * rightViewSinger.setVisibility(View.GONE);
		 * rightTvDate.setVisibility(View.GONE);
		 * 
		 * //leftTvFriends.setVisibility(View.VISIBLE);
		 * leftTvPub.setVisibility(View.GONE);
		 * //leftTvSinger.setVisibility(View.GONE);
		 * leftTvDate.setVisibility(View.GONE); }
		 */
	}

	/**
	 * 筛选按钮点击事件接口，传给子Fragment
	 * 
	 * @author jun
	 * 
	 */
	public interface IFilterListener {
		void onFilterClick(View view);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// 手动reset mChildFragmentManager,防止抛出java.lang.IllegalStateException: No
		// activity
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 幸运相遇
	 */
	public void onLuckyMeet() {
		if (SharePreferenceUtil.getInstance(getActivity()).getBooleanFromShare(
				"IS_LUCKY_MEET", false)) {
			if (mTimeCount != null) {
				mTimeCount.cancel();
				mTimeCount = null;
			}

			Date dNow = new Date();

			Date dMorn = new Date();
			dMorn.setHours(10);
			dMorn.setMinutes(0);
			dMorn.setSeconds(0);

			Date dNight = new Date();
			dNight.setHours(22);
			dNight.setMinutes(0);
			dNight.setSeconds(0);

			if (dNow.before(dNight) && dNow.after(dMorn)) {
				LogUtil.LogDebug(getClass().getSimpleName(), "当前时间在晚上22点前，还有"
						+ (dNight.getTime() - dNow.getTime()) + "毫秒后幸运相遇", null);
				mTimeCount = new TimeCount(dNight.getTime() - dNow.getTime(),
						dNight.getTime() - dNow.getTime()) {
					@Override
					public void onFinish() {
						startActivity(new Intent(getActivity(),
								LuckyMeetActivity.class));
					}
				};
				mTimeCount.start();
				return;
			}

			if (dNow.before(dMorn)) {
				LogUtil.LogDebug(getClass().getSimpleName(), "当前时间在早上10点前，还有"
						+ (dMorn.getTime() - dNow.getTime()) + "毫秒后幸运相遇", null);
				mTimeCount = new TimeCount(dMorn.getTime() - dNow.getTime(),
						dMorn.getTime() - dNow.getTime()) {
					@Override
					public void onFinish() {
						startActivity(new Intent(getActivity(),
								LuckyMeetActivity.class));
					}
				};
				mTimeCount.start();
				return;
			}

			if (dNow.equals(dMorn) || dNow.equals(dNight)) {
				LogUtil.LogDebug(getClass().getSimpleName(), "幸运相遇", null);
				startActivity(new Intent(getActivity(), LuckyMeetActivity.class));
				return;
			}

		}
	}

	/**
	 * 取消幸运相遇
	 */
	public void onCancelLuckyMeet() {
		if (mTimeCount != null) {
			mTimeCount.cancel();
			mTimeCount = null;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		UmengUtil.onPageStart(getActivity(),"MainScreen");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		 UmengUtil.onPageEnd(getActivity(),"MainScreen");
	}
}
