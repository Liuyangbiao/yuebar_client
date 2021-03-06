package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.NearByPubListAdapter;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.PubModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.TimeUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.TopTitleView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月26日&emsp;上午11:58:20
 * @use 发布约会时选择酒吧界面
 */
public class SelectNearbyPubActivity extends BaseActivity {
	private List<PubModel> listPubModel;
	private NearByPubListAdapter adapterNearyPub;
	private XListView xListViewPubs;
	private boolean isBack;
	private EventListener mListener;

	private int pageNo = 1;

	private boolean isLoadMore = false;
	private boolean isRefresh = false;
	private int pageSize = 10;// 默认10条为1页

	public SelectNearbyPubActivity() {
		super(R.layout.activity_select_nearbypub);
	}

	@Override
	public void initViews() {
		xListViewPubs = (XListView) findViewById(R.id.listViewPubs);
		xListViewPubs
				.setAdapter(adapterNearyPub = adapterNearyPub == null ? new NearByPubListAdapter(
						listPubModel = listPubModel == null ? new ArrayList<PubModel>()
								: listPubModel)
						: adapterNearyPub);
		xListViewPubs.setEmptyView(findViewById(R.id.lvEmpty));
		xListViewPubs.setPullLoadEnable(false);
		initTopTitle();
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.pub_list), null);
	}

	@Override
	public void initData() {
		mListener = new EventListener();
	}

	@Override
	public void bindViews() {
		xListViewPubs.setOnItemClickListener(mListener);
		xListViewPubs.setXListViewListener(mListener);
	}

	/**
	 * 从服务器获取数据
	 */
	private void loadData() {
		HttpRequest.getInstance(SelectNearbyPubActivity.this).getPubList(
				pageNo, pageSize, null, new CustomAsyncResponehandler() {
					@Override
					public void onSuccess(
							com.fullteem.yueba.model.ResponeModel baseModel) {
						if (baseModel != null
								&& baseModel.getResultObject() != null) {
							List<PubModel> resul = (List<PubModel>) baseModel
									.getResultObject();
							xListViewPubs
									.setPullLoadEnable(resul == null ? false
											: resul.size() >= pageSize);
							if (!isLoadMore)
								listPubModel.clear();
							listPubModel.addAll(resul);
							adapterNearyPub.notifyDataSetChanged();
							isBack = true;
						}
					};

					@Override
					public void onFinish() {
						if (isRefresh) {
							xListViewPubs.stopRefresh();
							isRefresh = false;
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}
				});
	}

	private class EventListener implements OnItemClickListener,
			IXListViewListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == 0 || position == listPubModel.size() + 1) // xlistview
				return;
			
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("barName",listPubModel.get(position - 1).getBarName());
			UmengUtil.onEvent(SelectNearbyPubActivity.this, "select_bar_name", map);
			LogUtil.printUmengLog("select_bar_name");
			
			Intent intent = new Intent();
			intent.putExtra(GlobleConstant.PUB_ID,
					listPubModel.get(position - 1).getBarId()); // 减去xlistview的header
			intent.putExtra(GlobleConstant.ACTION_ADDRESS,
					listPubModel.get(position - 1).getBarName());
			intent.putExtra(GlobleConstant.ENABLE_PAY,
					listPubModel.get(position - 1).getOnOff());

			setResult(GlobleConstant.ACTION_ADDRESS_CODE, intent);
			finish();
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
	protected void onResume() {
		super.onResume();
		if (!isBack)
			loadData();
	}

}
