package com.fullteem.yueba.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.NearByPubListAdapter;
import com.fullteem.yueba.app.ui.PubDetailsActivity;
import com.fullteem.yueba.app.ui.fragment.NearbyMainFragment.IFilterListener;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.PubModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SharePreferenceUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.TimeUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月26日&emsp;上午11:58:20
 * @use 附近模块->酒吧
 */

// 当该页面获得焦点时从服务器获取数据，然后记录该页面已获取数据再次获得焦点则不需要重复获取
public class NearbyPubFrgment extends Fragment implements IFilterListener {

	private View view;
	private List<PubModel> listPubModel;
	private NearByPubListAdapter adapterNearyPub;
	private XListView xListViewPubs;
	private boolean isBack;
	private EventListener mListener;
	private View lvLoading;

	private int pageNo = 1;

	private boolean isLoadMore = false;
	private boolean isRefresh = false;
	private int pageSize = 10;// 默认10条为1页

	private static final int NEARBY_PUB_BACK = 0X123;

	public NearbyPubFrgment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_nearby_pub, container, false);
		initData();
		initView();
		bindViews();
		return view;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	private void initData() {
		mListener = new EventListener();
	}

	private void initView() {
		xListViewPubs = (XListView) view.findViewById(R.id.listViewPubs);
		xListViewPubs
				.setAdapter(adapterNearyPub = adapterNearyPub == null ? new NearByPubListAdapter(
						listPubModel = listPubModel == null ? new ArrayList<PubModel>()
								: listPubModel)
						: adapterNearyPub);
		xListViewPubs.setPullLoadEnable(false);

		EmptyView emptyView = new EmptyView(getActivity());
		emptyView.setVisibility(View.GONE);
		((ViewGroup) xListViewPubs.getParent()).addView(emptyView);
		xListViewPubs.setEmptyView(emptyView);
		lvLoading = view.findViewById(R.id.lvLoading);
		lvLoading.setVisibility(View.GONE);
	}

	private void bindViews() {
		xListViewPubs.setOnItemClickListener(mListener);
		xListViewPubs.setXListViewListener(mListener);
	}

	/**
	 * 从服务器获取数据
	 */
	private void loadData() {
		int filterIndex = SharePreferenceUtil.getInstance(getActivity())
				.getIntFromShare(GlobleConstant.PUB_FILTER,
						GlobleConstant.PUB_FILTER_DEFAULT);
		if (filterIndex != 3)
			HttpRequest.getInstance(getActivity()).getPubList(pageNo, pageSize,
					++filterIndex, httpResponseHandler);
		else
			HttpRequest.getInstance(getActivity()).getPubList(pageNo, pageSize,
					null, httpResponseHandler);
	}

	private CustomAsyncResponehandler httpResponseHandler = new CustomAsyncResponehandler() {
		@Override
		public void onSuccess(com.fullteem.yueba.model.ResponeModel baseModel) {
			if (baseModel != null && baseModel.getResultObject() != null) {
				List<PubModel> resul = (List<PubModel>) baseModel
						.getResultObject();
				xListViewPubs.setPullLoadEnable(resul == null ? false : resul
						.size() >= pageSize);
				System.out.println("酒吧的model的长度：" + resul.size());
				if (!isLoadMore)
					listPubModel.clear();
				listPubModel.addAll(resul);
				adapterNearyPub.notifyDataSetChanged();
				isBack = true;
				UIhandler.sendEmptyMessage(NEARBY_PUB_BACK);
			}
		};

		@Override
		public void onFinish() {
			if (isRefresh) {
				xListViewPubs.stopRefresh();
				isRefresh = false;
			}
			if (xListViewPubs.getVisibility() != View.VISIBLE)
				xListViewPubs.setVisibility(View.VISIBLE);
			if (lvLoading.getVisibility() != View.GONE)
				lvLoading.setVisibility(View.GONE);
		}

		@Override
		public void onStart() {
			if (listPubModel == null || listPubModel.size() <= 0) {
				xListViewPubs.setVisibility(View.GONE);
				lvLoading.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
		}

	};

	@Override
	public void onResume() {
		super.onResume();
		UmengUtil.onPageStart(getActivity(),"MainScreen");
		if (!isBack)
			loadData();
	}

	@Override
	public void onPause() {
		super.onPause();
		UmengUtil.onPageEnd(getActivity(),"MainScreen");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isBack = false;
	}

	private class EventListener implements OnItemClickListener,
			IXListViewListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == 0 || position == listPubModel.size() + 1) // xlistview
																		// 的头部和底部
				return;
			UmengUtil.onEvent(getActivity(), "bar_hits");
			LogUtil.printUmengLog("bar_hits");
			AppContext appContext = (AppContext) getActivity().getApplication();
			if(appContext.isBarClickable()){
				UmengUtil.onEvent(getActivity(), "bar_users");
				LogUtil.printUmengLog("bar_users");
				appContext.setBarClickable(false);
			}
			Intent intent = new Intent(getActivity(), PubDetailsActivity.class);
			intent.putExtra(GlobleConstant.PUB_ID,
					listPubModel.get(position - 1).getBarId()); // 减去xlistview的header
			intent.putExtra(GlobleConstant.PUB_NAME,
					listPubModel.get(position - 1).getBarName());
			intent.putExtra(GlobleConstant.ENABLE_PAY,
					listPubModel.get(position - 1).getOnOff());
			startActivity(intent);
		}

		@Override
		public void onRefresh() {
			isLoadMore = false;
			isRefresh = true;
			pageNo = 1;
			loadData();
			xListViewPubs.setRefreshTime(TimeUtil.getCurrentDateTime());
		}

		@Override
		public void onLoadMore() {
			isLoadMore = true;
			isRefresh = false;
			pageNo++;
			loadData();
		}

	}

	@Override
	public void onFilterClick(View view) {
		refresh();
	}

	public void refresh() {
		pageNo = 1;
		listPubModel.clear();
		adapterNearyPub.notifyDataSetChanged();
		loadData();
	}

	Handler UIhandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NEARBY_PUB_BACK:
				adapterNearyPub.notifyDataSetChanged();
				// 设置加载更多是否可见
				xListViewPubs.setPullLoadEnable(StringUtils.isLoadMoreVisible(
						listPubModel.size(), pageSize));
				break;

			default:
				break;
			}
		};
	};

}
