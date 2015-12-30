package com.fullteem.yueba.app.singer;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.MessageMenuAdapter;
import com.fullteem.yueba.app.adapter.NearBySingerListAdapter;
import com.fullteem.yueba.app.singer.model.SingerListModel;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.engine.push.PushService;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SharePreferenceUtil;
import com.fullteem.yueba.util.TimeUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.TopTitleView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;
import com.fullteem.yueba.widget.filter.NearbySingerPopWindow;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月4日&emsp;下午6:13:59
 * @use 发现模块-附近singer
 */
public class DiscoverNearbySingerActivity extends BaseActivity {
	private TopTitleView topTitle;
	private View rightViewSinger;
	private TextView leftTvSinger;
	private static NearbySingerPopWindow popMenuSinger;// 弹出选择框->歌手界面
	private static PopupWindow mPopupWindow; // 弹出框

	/*
	 * used for top right: when click right top image, will pop up window. User
	 * then select one of the item: whether filter or city
	 */
	private String[] menuTexts;
	private PopupWindow morePopupWindow;
	private View menuView;
	private MessageMenuAdapter mma;

	private List<SingerListModel> listSinger;
	private NearBySingerListAdapter adapter;
	private XListView xListViewFriends;
	private EventListener mListener;
	private View lvLoading;

	private boolean isBack;
	private int pageSize = 10;// 默认10条为1页
	private boolean isLoadMore = false;
	private boolean isRefresh = false;
	private int pageNo = 1;

	public DiscoverNearbySingerActivity() {
		super(R.layout.activity_discover_nearby_singer);
	}

	@Override
	public void initViews() {
		initTitle();

		xListViewFriends = (XListView) findViewById(R.id.listViewSinger);
		// xListViewFriends.setPullLoadEnable(false);

		EmptyView emptyView = new EmptyView(DiscoverNearbySingerActivity.this);
		// emptyView.setVisibility(View.GONE);
		((ViewGroup) xListViewFriends.getParent()).addView(emptyView);
		xListViewFriends.setEmptyView(emptyView);

		lvLoading = findViewById(R.id.lvLoading);
		// lvLoading.setVisibility(View.GONE);
	}

	private void initTitle() {
		topTitle = (TopTitleView) findViewById(R.id.top_title);

		// left
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// center
		topTitle.showCenterText(getResString(R.string.discover_nearbySinger),
				null);

		// right
		topTitle.showRightText(getString(R.string.filter), null,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						showFilterPopup();
					}
				});

		/*
		 * rightViewSinger = new
		 * LinearLayout(DiscoverNearbySingerActivity.this); ((LinearLayout)
		 * rightViewSinger).setOrientation(LinearLayout.HORIZONTAL); TextView
		 * tvSinger = new TextView(DiscoverNearbySingerActivity.this);
		 * tvSinger.setTextAppearance(DiscoverNearbySingerActivity.this,
		 * R.style.toptitle_right); tvSinger.setText("广州"); LayoutParams
		 * lpSinger = new LayoutParams(LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT); lpSinger.setMargins(0, 0,
		 * DisplayUtils.dp2px(DiscoverNearbySingerActivity.this, 4), 0);
		 * ((LinearLayout) rightViewSinger).addView(tvSinger, lpSinger);
		 * ImageView ivSinger = new
		 * ImageView(DiscoverNearbySingerActivity.this);
		 * ivSinger.setScaleType(ScaleType.CENTER);
		 * ivSinger.setImageResource(R.drawable.down_arrow_icon);
		 * ((LinearLayout) rightViewSinger).addView(ivSinger);
		 * 
		 * topTitle.addViewToRight(rightViewSinger);
		 * 
		 * // left
		 * 
		 * leftTvSinger = new TextView(DiscoverNearbySingerActivity.this);
		 * leftTvSinger.setTextAppearance(DiscoverNearbySingerActivity.this,
		 * R.style.toptitle_left);
		 * leftTvSinger.setText(getString(R.string.filter));
		 * topTitle.addViewToLeft(leftTvSinger);
		 * 
		 * leftTvSinger.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { showFilterPopup(); } });
		 */

	}

	private void showFilterPopup() {
		if (popMenuSinger != null) {
			popMenuSinger.initCkbs();
		} else {
			popMenuSinger = new NearbySingerPopWindow(
					DiscoverNearbySingerActivity.this);

			popMenuSinger.setOnCkbEventListener(
					popMenuSinger.new OnCkbEventListener() {
						@Override
						public void onClick(View v) {
							super.onClick(v);
							onFilterClick(v);
						}
					}, popMenuSinger.new OnCkbEventListener() {
						@Override
						public void onClick(View v) {
							super.onClick(v);
							onFilterClick(v);
						}
					}, popMenuSinger.new OnCkbEventListener() {
						@Override
						public void onClick(View v) {
							super.onClick(v);
							onFilterClick(v);
						}
					});
		}

		mPopupWindow = popMenuSinger.getMenu();
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
		mListener = new EventListener();

		xListViewFriends
				.setAdapter(adapter = adapter == null ? new NearBySingerListAdapter(
						DiscoverNearbySingerActivity.this,
						listSinger = listSinger == null ? new ArrayList<SingerListModel>()
								: listSinger)
						: adapter);

	}

	@Override
	public void bindViews() {
		xListViewFriends.setOnItemClickListener(mListener);
		xListViewFriends.setXListViewListener(mListener);
	}

	private class EventListener implements OnItemClickListener,
			IXListViewListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == 0 || position == listSinger.size() + 1)
				return;
			UmengUtil.onEvent(DiscoverNearbySingerActivity.this, "singer_hits");
			LogUtil.printUmengLog("singer_hits");
			Intent intent = new Intent(DiscoverNearbySingerActivity.this,
					SingerVideoListActivity.class);
			intent.putExtra(GlobleConstant.SINGER_ID,
					listSinger.get(position - 1).getSingerId());
			intent.putExtra(GlobleConstant.SINGER_NAME,
					listSinger.get(position - 1).getSingerName());
			intent.putExtra(GlobleConstant.PUB_NAME,
					listSinger.get(position - 1).getBarName());
			intent.putExtra(GlobleConstant.PUB_ID, listSinger.get(position - 1)
					.getBarId());
			startActivity(intent);
		}

		@Override
		public void onRefresh() {
			isLoadMore = false;
			isRefresh = true;
			pageNo = 1;
			loadData();
			xListViewFriends.setRefreshTime(TimeUtil.getCurrentDateTime());
		}

		@Override
		public void onLoadMore() {
			isLoadMore = true;
			isRefresh = false;
			pageNo++;
			loadData();
		}
	}

	/**
	 * 从服务器获取数据
	 */
	private void loadData() {
		int filterIndex = SharePreferenceUtil.getInstance(
				DiscoverNearbySingerActivity.this).getIntFromShare(
				GlobleConstant.SINGER_FILTER,
				GlobleConstant.SINGER_FILTER_DEFAULT);
		System.out.println("filterIndex:" + filterIndex);
		Integer singerSex = null;
		if (filterIndex == 0)// girl
			singerSex = 2;
		else if (filterIndex == 1)// boy
			singerSex = 1;
		System.out.println("singerSex:" + singerSex);
		HttpRequest.getInstance(DiscoverNearbySingerActivity.this)
				.getSingerList(pageNo, pageSize, singerSex,
						new CustomAsyncResponehandler() {
							@Override
							public void onSuccess(ResponeModel baseModel) {
								if (baseModel != null && baseModel.isStatus()) {
									List<SingerListModel> listModels = (List<SingerListModel>) baseModel
											.getResultObject();
//									System.out.println("baseModel result:"
//											+ baseModel.getResult());
									xListViewFriends
											.setPullLoadEnable(listModels == null ? false
													: listModels.size() >= pageSize);
									if (!isLoadMore)
										listSinger.clear();
									listSinger.addAll(listModels);
									adapter.notifyDataSetChanged();
									isBack = true;
								}
							}

							@Override
							public void onFinish() {
								xListViewFriends.setVisibility(View.VISIBLE);
								lvLoading.setVisibility(View.GONE);
								if (isRefresh) {
									xListViewFriends.stopRefresh();
									isRefresh = false;
								}
							}

							@Override
							public void onStart() {
								if (listSinger == null
										|| listSinger.size() <= 0) {
									lvLoading.setVisibility(View.VISIBLE);
									xListViewFriends.setVisibility(View.GONE);
								}
							}

							@Override
							public void onFailure(Throwable error,
									String content) {
							}
						});
	}

	@Override
	public void onResume() {
		super.onResume();
		UmengUtil.onPageStart(this,"SplashScreen");
		PushService.onResume("singer", this);

		if (!isBack)
			loadData();
	}

	@Override
	public void onPause() {
		super.onPause();
		UmengUtil.onPageEnd(this,"SplashScreen");
		PushService.onPause("singer", this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isBack = false;
	}

	private void onFilterClick(View view) {
		pageNo = 1;
		listSinger.clear();
		adapter.notifyDataSetChanged();
		loadData();
	}

}
