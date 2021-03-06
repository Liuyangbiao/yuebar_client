package com.fullteem.yueba.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.DynamicCommonAdapter;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.interfaces.ICommentOnClickListener;
import com.fullteem.yueba.model.BaseListModel;
import com.fullteem.yueba.model.MoodModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.AppUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;

public class DynamicFriendsFragment extends BaseFragment implements
		ICommentOnClickListener {
	// private static DynamicFriendsFragment dff;
	private View view;
	private List<MoodModel> dynamicList;
	private XListView expandListFriends;
	private int pageSize = 10;
	private BaseActivity activity;
	private boolean isRefresh;
	private AppContext appcontext;
	private DynamicCommonAdapter adapter;

	public DynamicFriendsFragment() {

	}

	// public static Fragment getInstance() {
	// if (dff == null) {
	// dff = new DynamicFriendsFragment();
	// }
	// return dff;
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		appcontext = AppContext.getApplication();
		activity = (BaseActivity) getActivity();
		view = inflater.inflate(R.layout.fragment_dynamic_friends, null);
		dynamicList = new ArrayList<MoodModel>();
		expandListFriends = (XListView) view
				.findViewById(R.id.expandListFriends);
		expandListFriends
				.setAdapter(adapter = adapter == null ? new DynamicCommonAdapter(
						getActivity(), dynamicList, 1, this) : adapter);
		expandListFriends
				.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

		expandListFriends.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				isRefresh = true;
				sendFriendsDynamicRequest();
			}

			@Override
			public void onLoadMore() {
				isRefresh = false;
				sendFriendsDynamicRequest();
			}
		});

		sendFriendsDynamicRequest();
		return view;
	}

	/**
	 * 发送好友请求
	 */
	private void sendFriendsDynamicRequest() {
		JSONObject jo = new JSONObject();
		jo.put("userId", appcontext.getUserInfo().getUserId());
		if (!isRefresh) {
			jo.put("pageNumber",
					AppUtil.getPageNum(dynamicList.size(), pageSize));
			jo.put("pageSize", pageSize);
		} else {
			jo.put("pageNumber", 1);
			jo.put("pageSize", pageSize);
		}
		HttpRequestFactory.getInstance().postRequest(Urls.USER_FRIENDS_DYNAMIC,
				jo, friendsDyHandler);
	}

	AsyncHttpResponseHandler friendsDyHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			if (activity != null) {
				activity.showLoadingDialog();
			}
		};

		@Override
		public void onSuccess(String content) {
			if (content == null) {
				return;
			}
			BaseListModel<MoodModel> model = JSON.parseObject(content,
					new TypeReference<BaseListModel<MoodModel>>() {
					});
			if (model == null)
				return;
			if (GlobleConstant.REQUEST_SUCCESS
					.equalsIgnoreCase(model.getCode())) {
				if (isRefresh) {
					dynamicList.clear();
				}
				dynamicList.addAll(model.getResult());

				if (!StringUtils
						.isLoadMoreVisible(dynamicList.size(), pageSize)) {
					expandListFriends.setPullLoadEnable(false);
				}

				adapter.notifyDataSetChanged();
			}
		};

		@Override
		public void onFailure(Throwable error) {
		};

		@Override
		public void onFinish() {
			expandListFriends.stopRefresh();
			expandListFriends.stopLoadMore();
			activity.dismissLoadingDialog();
		};
	};

	@Override
	public void reFreshPage() {
		isRefresh = true;
		sendFriendsDynamicRequest();
	}

}
