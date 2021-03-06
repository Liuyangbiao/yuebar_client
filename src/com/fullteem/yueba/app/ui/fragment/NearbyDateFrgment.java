package com.fullteem.yueba.app.ui.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.ui.NearbyDateDetailsAcitvity;
import com.fullteem.yueba.app.ui.PerssonalInfoAcitivity;
import com.fullteem.yueba.app.ui.fragment.NearbyMainFragment.IFilterListener;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.DateModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SharePreferenceUtil;
import com.fullteem.yueba.util.TimeUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.ChildViewPager;
import com.fullteem.yueba.widget.ChildViewPager.OnSingleTouchListener;
import com.fullteem.yueba.widget.CircleImageView;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.NearbyDateItemView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月26日&emsp;上午11:58:20
 * @use 附近模块->约会
 */
public class NearbyDateFrgment extends Fragment implements IFilterListener {

	private View headerView;
	// successful date
	private List<View> listSuccessfulDateView;// 成功约会的显示容器
	private List<DateModel> listSuccessful;
	private AdapterVpDate adapterVpDate;
	private ChildViewPager mViewPager;
	private LinearLayout llIndicator;// indicator

	// can add date
	private XListView lvAddendus;// lvAddendus可加入
	private List<DateModel> listAddendus;
	private AdapterLvDate adapterAddendus;

	// loading view
	private View lvLoading;

	private EventListener mListener;

	// private ImageView[] ivIndicator;

	private int pageSize = 5;// 默认10条为1页
	private int pageNo = 1;

	private boolean isLoadMore = false;
	private boolean isRefresh = false;

	public NearbyDateFrgment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_nearby_date, null);
		headerView = inflater.inflate(R.layout.fragment_nearby_date_header,
				null);

		initViews(rootView);
		initData();
		bindViews();
		return rootView;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	private void initViews(View rootView) {
		mViewPager = (ChildViewPager) headerView
				.findViewById(R.id.vp_date_successful);
		llIndicator = (LinearLayout) headerView.findViewById(R.id.llIndicator);

		lvAddendus = (XListView) rootView.findViewById(R.id.lv_date_addendus);
		lvAddendus.setPullLoadEnable(false);

		EmptyView emptyView = new EmptyView(getActivity());
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lvAddendus.getParent()).addView(emptyView);
		lvAddendus.setEmptyView(emptyView);
		lvAddendus.setVisibility(View.GONE);
		
//		EmptyView emptyViewSuc = new EmptyView(getActivity());
//		emptyViewSuc.setVisibility(View.GONE);
//		((ViewGroup) mViewPager.getParent()).addView(emptyView);
//		//mViewPager.setEmptyView(emptyViewSuc);

		lvLoading = rootView.findViewById(R.id.lvLoading);
		lvLoading.setVisibility(View.GONE);

		// add as header
		lvAddendus.addHeaderView(headerView);
	}

	private void initData() {

		mListener = new EventListener();

		listAddendus = listAddendus == null ? new LinkedList<DateModel>()
				: listAddendus;
		lvAddendus
				.setAdapter(adapterAddendus = adapterAddendus == null ? new AdapterLvDate()
						: adapterAddendus);
		listSuccessful = new LinkedList<DateModel>();
		listSuccessfulDateView = new ArrayList<View>();
		mViewPager
				.setAdapter(adapterVpDate = adapterVpDate == null ? new AdapterVpDate()
						: adapterVpDate);
		
		// ------------------------------------示例数据------------------------------------
		// listSuccessful = new ArrayList<NearbyDateModel>(5);
		// listAddendus = new ArrayList<NearbyDateModel>(5);
		//
		// for (int i = 0; i < 7; i++) {
		// listSuccessful.add(new NearbyDateModel(null, "保罗酒吧", new
		// Date().toString(), 50 + i, 2 + i, "不限", "drawable://" +
		// R.drawable.pub_icon, "小欣欣", "drawable://"
		// + R.drawable.pub_icon));
		// }
		//
		// for (int i = 0; i < 7; i++) {
		// listAddendus.add(new NearbyDateModel(null, "保罗酒吧", new
		// Date().toString(), 50 + i, 2 + i, "不限", "drawable://" +
		// R.drawable.pub_icon, "小欣欣", "drawable://"
		// + R.drawable.pub_icon));
		// }
		// lvAddendus.setAdapter(new AdapterLvDate());

		// ------------------------------------示例数据------------------------------------

		// if (listSuccessful.size() <= 0 || listSuccessful == null) {
		//
		// // 如果已成功没有约会，则不显示“已成功约会布局”
		// presentMode.setSuccessfulVisibility(View.GONE);
		// } else {
		//
		// // 否则显示并且初始化 viewpager adapter
		// presentMode.setSuccessfulVisibility(View.VISIBLE);
		// listSuccessfulDateView = new ArrayList<View>(listSuccessful.size());
		// for (int i = 0; i < listSuccessful.size(); i++) {
		// NearbyDateItemView successfulItemView = new
		// NearbyDateItemView(getActivity());
		// listSuccessfulDateView.add(successfulItemView.createItemView(listSuccessful.get(i)));
		// }
		// mViewPager.setAdapter(new AdapterVpDate());
		// }

	}

	private void bindViews() {
		mViewPager.setOnSingleTouchListener(mListener);
		lvAddendus.setOnItemClickListener(mListener);
		lvAddendus.setXListViewListener(mListener);
	}
	
	private class EventListener implements OnSingleTouchListener,
			OnItemClickListener, IXListViewListener, OnPageChangeListener {
		AppContext appContext = (AppContext) getActivity().getApplication();
		@Override
		public void onSingleTouch(int position) {
			UmengUtil.onEvent(getActivity(), "successful_date_hits");
			LogUtil.printUmengLog("successful_date_hits");
			if(appContext.isSuccessfulDateClickable()){
				UmengUtil.onEvent(getActivity(), "successful_date_users");
				LogUtil.printUmengLog("successful_date_users");
				appContext.setSuccessfulDateClickable(false);
			}
			Intent intent = new Intent(getActivity(),
					NearbyDateDetailsAcitvity.class);
			DateModel dateModel = listSuccessful.get(position);
			dateModel.setSuccessful(true);
			Bundle bundle = new Bundle();
			bundle.putSerializable(GlobleConstant.DATE_MODEL, dateModel);
			intent.putExtras(bundle);
			startActivity(intent);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			UmengUtil.onEvent(getActivity(), "available_date_hits");
			LogUtil.printUmengLog("available_date_hits");
			if(appContext.isAvailableDateClickable()){
				UmengUtil.onEvent(getActivity(), "available_date_users");
				LogUtil.printUmengLog("available_date_users");
				appContext.setAvailableDateClickable(false);
			}
			
			Intent intent = new Intent(getActivity(),
					NearbyDateDetailsAcitvity.class);
			LogUtil.printPushLog("position:" + position);
			if (position < 2 || position == listAddendus.size() + 2)
				return;
			DateModel dateModel = listAddendus.get(position - 2);
			dateModel.setSuccessful(false);
			Bundle bundle = new Bundle();
			bundle.putSerializable(GlobleConstant.DATE_MODEL, dateModel);
			intent.putExtras(bundle);
			startActivity(intent);
		}

		@Override
		public void onRefresh() {
			isLoadMore = false;
			isRefresh = true;
			pageNo = 1;
			loadDataAddendus();
			lvAddendus.setRefreshTime(TimeUtil.getCurrentDateTime());
		}

		@Override
		public void onLoadMore() {
			isLoadMore = true;
			isRefresh = false;
			pageNo++;
			loadDataAddendus();
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			if (llIndicator != null && llIndicator.getChildCount() >= arg0) {
				for (int i = 0; i < llIndicator.getChildCount(); i++) {
					if (i == arg0) {
						// ivIndicator[i].setImageResource(R.drawable.img_indicator_select);
						((ImageView) (llIndicator.getChildAt(i)))
								.setImageResource(R.drawable.point_enable);
						continue;
					}
					// ivIndicator[i].setImageResource(R.drawable.img_indicator_normal);
					((ImageView) (llIndicator.getChildAt(i)))
							.setImageResource(R.drawable.point_normal);
				}
			}
		}

	}

	/**
	 * 从服务器获取数据
	 */
	private void loadDataSuccessful() {
		HttpRequest.getInstance(getActivity()).getDateSuccessfulList(new CustomAsyncResponehandler() {
			@Override
			public void onSuccess(ResponeModel baseModel) {
				if (baseModel != null && baseModel.getResultObject() != null) {
					List<DateModel> listResult = (List<DateModel>) baseModel.getListObject("userAppointmentJoinList", DateModel.class);
					//System.out.println("可参与人数："+listResult.get(0).getCount()+"  已参与人数："+listResult.get(0).getHasCount());
					if (listResult == null || listResult.size() <= 0){
						listSuccessfulDateView.clear();
						adapterVpDate.notifyDataSetChanged();
						// 如果已成功没有约会，则不显示“已成功约会布局”
						return;
					}
						
					else {
						if (!listSuccessful.isEmpty())
							listSuccessful.clear();
						if (llIndicator.getChildCount() > 0)
							llIndicator.removeAllViews();
						listSuccessful.addAll(listResult);
						if(listSuccessfulDateView.isEmpty())
						listSuccessfulDateView = new ArrayList<View>();
						else
							listSuccessfulDateView.clear();
						//						ivIndicator = new ImageView[listSuccessful.size()];
						ImageView ivChildIndicator;
						for (int i = 0; i < listSuccessful.size(); i++) {
							NearbyDateItemView successfulItemView = new NearbyDateItemView(getActivity());
							listSuccessfulDateView.add(successfulItemView.createItemView(listSuccessful.get(i)));
							ivChildIndicator = new ImageView(getActivity());
							ivChildIndicator.setImageResource(i == 0 ? R.drawable.point_enable : R.drawable.point_normal);
							LinearLayout.LayoutParams params = new LayoutParams(10, 10);
							if(i != 0){
								params.leftMargin = 5;
							}
							ivChildIndicator.setLayoutParams(params);
							llIndicator.addView(ivChildIndicator);
						}
						adapterVpDate.notifyDataSetChanged();
						// mViewPager.setAdapter(new AdapterVpDate());
					}
				}
			}

			@Override
			public void onStart() {
			}

			@Override
			public void onFailure(Throwable error, String content) {
			}

			@Override
			public void onFinish() {
			}
		});
	}

	private void loadDataAddendus() {
		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId == null) {
			LogUtil.LogError(getClass().getName(), "获取可加入约会。。。userid为null",
					null);
			return;
		}

		int filterIndex = SharePreferenceUtil.getInstance(getActivity())
				.getIntFromShare(GlobleConstant.DATE_FILTER,
						GlobleConstant.DATE_FILTER_DEFAULT);
		Integer userAppointmentObj = null;// all
		if (filterIndex == 2)
			userAppointmentObj = 0;// only boy
		if (filterIndex == 1)
			userAppointmentObj = 1;// only girl
		HttpRequest.getInstance(getActivity()).getDateAddendusList(userId,
				pageNo, pageSize, userAppointmentObj,
				new CustomAsyncResponehandler() {
					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel != null && baseModel.isStatus()) {
							if (!isLoadMore)
								listAddendus.clear();
							List<DateModel> result = (List<DateModel>) baseModel
									.getResultObject();
							if (result == null || result.size() <= 0
									|| result.get(0) == null)
								return;

							lvAddendus.setPullLoadEnable(result == null ? false
									: result.size() >= pageSize);
							listAddendus.addAll(result);
							adapterAddendus.notifyDataSetChanged();
						}
					}

					@Override
					public void onFinish() {
						if (isRefresh) {
							lvAddendus.stopRefresh();
							isRefresh = false;
						}
						if (lvAddendus.getVisibility() != View.VISIBLE)
							lvAddendus.setVisibility(View.VISIBLE);
						if (lvLoading.getVisibility() != View.GONE)
							lvLoading.setVisibility(View.GONE);
					}

					@Override
					public void onStart() {
						if (listAddendus == null || listAddendus.size() <= 0) {
							lvAddendus.setVisibility(View.GONE);
							lvLoading.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});

	}

	private class AdapterVpDate extends PagerAdapter {
		@Override
		public int getCount() {
			return listSuccessfulDateView.size();
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//container.removeView(listSuccessfulDateView.get(position));
			container.removeView((View)object);
		}

		@Override
		public Object instantiateItem(ViewGroup view, final int position) {
			view.addView(listSuccessfulDateView.get(position));
			ImageView ivSponsorHeader = (ImageView) listSuccessfulDateView.get(
					position).findViewById(R.id.iv_sponsor_header);

			ivSponsorHeader.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String userId = listSuccessful.get(position).getUserId()
							+ "";
					Intent intent = new Intent(getActivity(),
							PerssonalInfoAcitivity.class);
					intent.putExtra("userId", userId);
					startActivity(intent);
				}
			});
			return listSuccessfulDateView.get(position);
		}
		
		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}
	}

	/**
	 * @author jun
	 * @use 可加入约会listview的adapter
	 */
	private class AdapterLvDate extends BaseAdapter {
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder mHolder;
			final DateModel model = listAddendus.get(position);

			if (convertView == null) {
				mHolder = new ViewHolder();
				convertView = View.inflate(getActivity(),
						R.layout.adapter_date_elistview, null);
				mHolder.ivPubHeader = (CircleImageView) convertView
						.findViewById(R.id.ImgViewHeader);
				mHolder.tvPubName = (TextView) convertView
						.findViewById(R.id.tv_pubname);
				mHolder.tvDateTitle = (TextView) convertView
						.findViewById(R.id.tv_date_title);
				mHolder.tvSponsor = (TextView) convertView
						.findViewById(R.id.tv_sponsor);
				mHolder.ivSponsorHeader = (ImageView) convertView
						.findViewById(R.id.iv_sponsor_header);
				mHolder.tvTime = (TextView) convertView
						.findViewById(R.id.tv_time);
				mHolder.tvAttendPeoples = (TextView) convertView
						.findViewById(R.id.tv_attendpeoples);
				mHolder.llInvolved = (LinearLayout) convertView
						.findViewById(R.id.ll_involved);
				mHolder.llInvolved.setVisibility(View.VISIBLE);
				mHolder.tvInvolved = (TextView) convertView
						.findViewById(R.id.tv_involved);
				mHolder.tvExpect = (TextView) convertView
						.findViewById(R.id.tv_expect);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}
			mHolder.ivPubHeader.setBackgroundColor(Color.TRANSPARENT);
			mHolder.ivSponsorHeader.setBackgroundColor(Color.TRANSPARENT);
			ImageLoaderUtil.getImageLoader().displayImage(
					DisplayUtils.getAbsolutelyUrl(model.getBarLogoUrl()),
					mHolder.ivPubHeader,
					ImageLoaderUtil.getOptions(
							R.drawable.img_loading_default_copy,
							R.drawable.img_loading_empty_copy,
							R.drawable.img_loading_fail_copy));
			ImageLoaderUtil.getImageLoader().displayImage(
					DisplayUtils.getAbsolutelyUrl(model.getUserLogoUrl()),
					mHolder.ivSponsorHeader,
					ImageLoaderUtil.getOptions(R.drawable.img_loading_default,
							R.drawable.img_loading_empty,
							R.drawable.img_loading_fail));
			mHolder.tvDateTitle.setText(model.getUserAppointmentTitle());
			mHolder.tvPubName.setText(model.getBarName());
			mHolder.tvSponsor.setTextColor(getActivity().getResources()
					.getColor(R.color.girl_red));
			mHolder.tvSponsor.setText(model.getUserName());
			mHolder.tvTime.setText(model.getUserAppointmentDate());
			mHolder.tvAttendPeoples.setText("" + model.getCount());
			mHolder.tvInvolved.setText("" + model.getHasCount());
			mHolder.tvExpect.setText(model.getUserAppointmentObj());
			// 点击约会发起人图标显示约会发起人详情
			mHolder.ivSponsorHeader.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String userId = model.getUserId() + "";
					Intent intent = new Intent(getActivity(),
							PerssonalInfoAcitivity.class);
					intent.putExtra("userId", userId);
					startActivity(intent);
				}
			});

			return convertView;
		}

		@Override
		public int getCount() {
			return listAddendus.size();
		}

		@Override
		public Object getItem(int position) {
			return listAddendus.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	private class ViewHolder {
		/** 酒吧头像 */
		protected CircleImageView ivPubHeader;
		/** 约会主题 */
		protected TextView tvDateTitle;
		/** 发起人 */
		protected TextView tvSponsor;
		/** 发起人头像 */
		protected ImageView ivSponsorHeader;
		/** 酒吧名称 */
		protected TextView tvPubName;
		/** 约会时间 */
		protected TextView tvTime;
		/** 参与人数 */
		protected TextView tvAttendPeoples;
		/** 已参与线性布局 */
		protected LinearLayout llInvolved;
		/** 已参与 */
		protected TextView tvInvolved;
		/** 期望 */
		protected TextView tvExpect;
	}

	@Override
	public void onResume() {
		LogUtil.printDateLog("enter onResume");
		super.onResume();
		UmengUtil.onPageStart(getActivity(),"MainScreen");
		if(null == getActivity()){
			return;
		}
		
		isLoadMore = false;
		adapterAddendus.notifyDataSetChanged();
		adapterVpDate.notifyDataSetChanged();
		loadDataAddendus();
		loadDataSuccessful();
		mViewPager.setOnPageChangeListener(mListener);
	}

	@Override
	public void onPause() {
		super.onPause();
		UmengUtil.onPageEnd(getActivity(),"MainScreen");
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onFilterClick(View view) {
		refresh();
	}

	public void refresh() {
		pageNo = 1;
		listAddendus.clear();
		adapterAddendus.notifyDataSetChanged();
		loadDataAddendus();
	}

}
