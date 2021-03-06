package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.MessageMenuAdapter;
import com.fullteem.yueba.engine.push.PushService;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.Group;
import com.fullteem.yueba.model.NearbyGroupModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.BitmapManager;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SharePreferenceUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.CircleImageView;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.TopTitleView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月4日&emsp;下午6:13:59
 * @use 发现模块-附近群组
 */
public class DiscoverNearbyGroupActivity extends BaseActivity {

	private TopTitleView topTitle;
	private XListView lvNearbyGroup;
	private List<Group> listNearbyGroup;
	private int pageSize = 10;
	private int pageNo = 1;
	private AdapterNearbyGroup adapter;
	private boolean isRefresh;
	private boolean isSrlRefresh;
	private View viewLoad;
	private SwipeRefreshLayout srlRefresh;

	// below is something about morePopupWindow
	private PopupWindow morePopupWindow;
	private View menuView;
	private ListView listviewMsgMenus = null;
	private MessageMenuAdapter mma;
	private String[] menuTexts;// top right is a list.

	public DiscoverNearbyGroupActivity() {
		super(R.layout.activity_discover_nearby_group);
	}

	@Override
	public void initViews() {
		menuTexts = new String[3];
		menuTexts[0] = getString(R.string.group_create);
		menuTexts[1] = getString(R.string.group_mycreate);
		menuTexts[2] = getString(R.string.group_imin);
		topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showRightImag(BitmapManager.getBitmapFromDrawable(
				DiscoverNearbyGroupActivity.this, R.drawable.chat_more_icon),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// Intent intent = new
						// Intent(DiscoverNearbyGroupActivity.this,
						// CreateGroupActivity.class);
						// startActivity(intent);
						moreWindowShow();
					}
				});

		lvNearbyGroup = (XListView) findViewById(R.id.lv_nearbyGroup);
		srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srlRefresh);

		EmptyView emptyView = new EmptyView(DiscoverNearbyGroupActivity.this);
		((ViewGroup) lvNearbyGroup.getParent()).addView(emptyView);
		lvNearbyGroup.setEmptyView(emptyView);
		lvNearbyGroup.setVisibility(View.GONE);

		viewLoad = findViewById(R.id.lvLoading);
		initTopTitle();
	}

	protected void moreWindowShow() {
		// TODO Auto-generated method stub
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
					UmengUtil.onEvent(DiscoverNearbyGroupActivity.this, "create_group_hits");
					LogUtil.printUmengLog("create_group_hits");
					Intent intent = new Intent(
							DiscoverNearbyGroupActivity.this,
							CreateGroupActivity.class);
					startActivity(intent);
				} else if (position == 1) {
					Intent intent = new Intent(
							DiscoverNearbyGroupActivity.this,
							GroupMyCreatedActivity.class);
					startActivity(intent);
				} else if (position == 2) {
					Intent intent = new Intent(
							DiscoverNearbyGroupActivity.this,
							GroupImInActivity.class);
					startActivity(intent);
				}

			}

		});

	}

	@Override
	public void initData() {
		listNearbyGroup = new ArrayList<Group>();
		// --------------------------examples data--------------------------
		// for (int i = 0; i < 17; i++) {
		// listNearbyGroup.add(new NearbyGroupModel("drawable://"
		// + R.drawable.ico_loginmain_sina, "小狼人" + i, "2.2km", i + 5,
		// i + 12, "有钱，任性！没钱，认命!"));
		// }
		adapter = new AdapterNearbyGroup();
		lvNearbyGroup.setAdapter(adapter);
		// --------------------------examples data--------------------------

		// 发送附近群组查询信息
		getImInGroups();

		// 加载和刷新
		lvNearbyGroup.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				isRefresh = true;
				pageNo = 1;
				getImInGroups();
			}

			@Override
			public void onLoadMore() {
				isRefresh = false;
				pageNo++;
				getImInGroups();
			}
		});

	}

	@Override
	public void bindViews() {
		lvNearbyGroup.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0 || position == listNearbyGroup.size() + 1)
					return;
				UmengUtil.onEvent(DiscoverNearbyGroupActivity.this, "group_hits");
				LogUtil.printUmengLog("group_hits");
				Intent intent = new Intent(DiscoverNearbyGroupActivity.this,
						GroupDetailActivity.class);
				intent.putExtra("GROUP_ID", listNearbyGroup.get(position - 1)
						.getGroupId());
				startActivity(intent);
			}
		});

		srlRefresh.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		srlRefresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				isSrlRefresh = true;
				getImInGroups();
			}
		});
	}

	private void initTopTitle() {
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getResString(R.string.discover_nearbyGroup),
				null);
	}

	private class AdapterNearbyGroup extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mHolder;
			if (convertView == null) {
				mHolder = new ViewHolder();
				convertView = View.inflate(DiscoverNearbyGroupActivity.this,
						R.layout.adapter_nearby_group_xlistview, null);
				mHolder.ivGroupHeader = (CircleImageView) convertView
						.findViewById(R.id.iv_groupHeader);
				mHolder.tvDistance = (TextView) convertView
						.findViewById(R.id.tv_distance);
				mHolder.tvGroupName = (TextView) convertView
						.findViewById(R.id.tv_groupName);
				mHolder.tvPeopleNum = (TextView) convertView
						.findViewById(R.id.tv_peopleNum);
				mHolder.tvMood = (TextView) convertView
						.findViewById(R.id.tv_mood);
				convertView.setTag(mHolder);
			} else
				mHolder = (ViewHolder) convertView.getTag();

			ImageLoaderUtil.getImageLoader().displayImage(
					DisplayUtils.getAbsolutelyUrl(listNearbyGroup.get(position)
							.getGroupIcon()), mHolder.ivGroupHeader);
			mHolder.tvGroupName.setText(listNearbyGroup.get(position)
					.getGroupName());
			// 将米转为千米，保留两位小数
			Double distanceInt = Double.parseDouble(listNearbyGroup.get(
					position).getDistance());
			String distance = new java.text.DecimalFormat("#0.00")
					.format(distanceInt / 1000) + "千米";
			mHolder.tvDistance.setText(distance);
			mHolder.tvPeopleNum.setText(StringUtils.formatStrD2Str(context,
					R.string.nearby_group_peoples, listNearbyGroup
							.get(position).getNum()));
			mHolder.tvMood
					.setText(listNearbyGroup.get(position).getGroupDesc());
			return convertView;
		}

		@Override
		public int getCount() {
			return listNearbyGroup.size();
		}

		@Override
		public Object getItem(int position) {
			return listNearbyGroup.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

	}

	private class ViewHolder {
		CircleImageView ivGroupHeader;
		TextView tvGroupName;
		TextView tvDistance;
		TextView tvPeopleNum;
		TextView tvMood;
	}

	/**
	 * 附近群组
	 */
	private void getImInGroups() {
		JSONObject ob = new JSONObject();
		ob.put("userId", appContext.getUserInfo().getUserId());
		// ob.put("pageNumber", AppUtil.getPageNum(listNearbyGroup.size(),
		// pageSize));
		ob.put("pageNumber", pageNo);

		// pagesize固定为10
		ob.put("pageSize", pageSize);

		String city = SharePreferenceUtil.getInstance(
				AppContext.getApplication()).getSettingCityId();
		ob.put("cityId", city);

		HttpRequestFactory.getInstance().getRequest(Urls.NEAR_BY_GROUPS, ob,
				nearByHandler);
	}

	/**
	 * 附近群组
	 */
	AsyncHttpResponseHandler nearByHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			// showLoadingDialog();
			if (listNearbyGroup == null || listNearbyGroup.size() <= 0) {
				viewLoad.setVisibility(View.VISIBLE);
				lvNearbyGroup.setVisibility(View.GONE);
			}
		};

		@Override
		public void onSuccess(String content) {
			NearbyGroupModel gm = new NearbyGroupModel();
			gm = JsonUtil.convertJsonToObject(content, NearbyGroupModel.class);
			if (gm != null
					&& GlobleConstant.REQUEST_SUCCESS.equalsIgnoreCase(gm
							.getCode())) {
				List<Group> gp = gm.getResult();
				lvNearbyGroup.setPullLoadEnable(gp == null ? false
						: gp.size() >= pageSize);
				if (gp == null)
					return;
				if (isRefresh)
					listNearbyGroup.clear();
				listNearbyGroup.addAll(gp);
				adapter.notifyDataSetChanged();
			}
		};

		@Override
		public void onFailure(Throwable error) {
			lvNearbyGroup.setPullLoadEnable(true);
			if (listNearbyGroup == null || listNearbyGroup.size() <= 0) {
				lvNearbyGroup.setVisibility(View.GONE);
				srlRefresh.setVisibility(View.VISIBLE);
				EmptyView emptyView = (EmptyView) findViewById(R.id.emptyText);
				emptyView.setText("下拉重新刷新");
			}
			System.out.println("加载错误" + error.toString());
		};

		@Override
		public void onFinish() {
			// dismissLoadingDialog();
			if (listNearbyGroup != null && listNearbyGroup.size() >= 1) {
				lvNearbyGroup.setVisibility(View.VISIBLE);
				srlRefresh.setVisibility(View.GONE);
			}
			if (viewLoad.getVisibility() != View.GONE)
				viewLoad.setVisibility(View.GONE);
			if (isRefresh) {
				lvNearbyGroup.stopRefresh();
				isRefresh = false;
			}
			lvNearbyGroup.stopLoadMore();
			if (isSrlRefresh) {
				srlRefresh.setRefreshing(false);
				isSrlRefresh = false;
			}
		};

	};

	@Override
	public void onResume() {
		super.onResume();
		PushService.onResume("group", this);
		UmengUtil.onPageStart(this,"SplashScreen");
	}

	@Override
	public void onPause() {
		super.onPause();
		PushService.onPause("group", this);
		UmengUtil.onPageEnd(this,"SplashScreen");
	}
}
