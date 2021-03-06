package com.fullteem.yueba.app.ui.fragment;

import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.DynamicBarsAdapter;
import com.fullteem.yueba.app.adapter.DynamicBarsAdapter.IPraiseClickListener;
import com.fullteem.yueba.model.PubModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;

public class DynamicBarsFragment extends BaseFragment implements
		IPraiseClickListener {
	// private static DynamicBarsFragment dff;
	private View view;
	private List<PubModel> listPub;
	private XListView lvPubs;
	private DynamicBarsAdapter adapter;

	private int pageNo = 1;
	private int pageSize = 10;
	private boolean isRefresh;

	public DynamicBarsFragment() {

	}

	// public static Fragment getInstance() {
	// if (dff == null) {
	// dff = new DynamicBarsFragment();
	// }
	// return dff;
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_dynamic_friends, null);
		initView();
		intiDate();
		bindVIew();
		return view;
	}

	private void initView() {
		lvPubs = (XListView) view.findViewById(R.id.expandListFriends);
		EmptyView emptyView = new EmptyView(getActivity());
		((ViewGroup) lvPubs.getParent()).addView(emptyView);
		emptyView.setVisibility(View.GONE);
		lvPubs.setEmptyView(emptyView);
	}

	private void intiDate() {
		lvPubs.setAdapter(adapter = adapter == null ? new DynamicBarsAdapter(
				this, listPub = listPub == null ? new LinkedList<PubModel>()
						: listPub) : adapter);
		if (listPub == null || listPub.size() <= 0)
			loadDate(pageNo, pageSize);
	}

	private void bindVIew() {
		lvPubs.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				isRefresh = true;
				pageNo = 1;
				loadDate(pageNo, pageSize);
			}

			@Override
			public void onLoadMore() {
				pageNo++;
				isRefresh = false;
				loadDate(pageNo, pageSize);
			}
		});
	}

	private void loadDate(int pageNo, final int pageSize) {
		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId != null)
			HttpRequest.getInstance().getFollowPub(userId, pageNo, pageSize,
					new CustomAsyncResponehandler() {
						@Override
						public void onSuccess(ResponeModel baseModel) {
							if (baseModel != null && baseModel.isStatus()) {
								List<PubModel> listTmp = (List<PubModel>) baseModel
										.getResultObject();
								lvPubs.setPullLoadEnable(listTmp == null ? false
										: listTmp.size() >= pageSize);
								if (listTmp == null || listTmp.size() <= 0
										|| listTmp.get(0) == null)
									return;
								if (isRefresh)
									listPub.clear();
								listPub.addAll(listTmp);
								adapter.notifyDataSetChanged();
							}
						}

						@Override
						public void onFinish() {
							if (isRefresh) {
								lvPubs.stopRefresh();
								isRefresh = false;
								lvPubs.stopLoadMore();
							}
						}

						@Override
						public void onFailure(Throwable error, String content) {

						}
					});
	}

	/*
	 * adapter里面点赞事件回传 (non-Javadoc)
	 * 
	 * @see
	 * com.fullteem.yueba.app.adapter.DynamicBarsAdapter.IPraiseClickListener
	 * #onPraiseClickListener()
	 */
	@Override
	public void onPraiseClickListener() {
		isRefresh = true;
		loadDate(pageNo = 1, pageSize = listPub.size());
	}

	// @Override
	// public void onDestroy() {
	// dff = null;
	// listPub = null;
	// super.onDestroy();
	// }

}
