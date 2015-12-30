package com.fullteem.yueba.app.ui.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.GroupAdapter;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.GroupsModel;
import com.fullteem.yueba.model.GroupsModel.Groups;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.AppUtil;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.widget.InScrollListView;

/**
 * 群组fragment
 * 
 * @author ssy
 */
public class GroupFragment extends BaseFragment {
	private static Fragment groupFragment;
	private View view;
	private InScrollListView lvMyGroups, lvImInGroups;
	private List<Groups> listMyCreate, listImIN;
	// private List<EMGroup> listImIN;
	// private ImageView imgCreateGroup;
	public AppContext appContext;
	private int pageSize = 10;
	private GroupAdapter groupAdapter, adapterImIn;

	public GroupFragment() {
	}

	public static Fragment getInstance() {
		if (groupFragment == null) {
			groupFragment = new GroupFragment();
		}
		return groupFragment;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (listMyCreate != null && listMyCreate.size() > 0) {
			getMyCreateGroups();
		}
		if (listImIN != null && listImIN.size() > 0) {
			getImInGroups();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_group, null);
		appContext = (AppContext) getActivity().getApplication();
		initData();
		return view;
	}

	private void initData() {
		/*
		 * imgCreateGroup = (ImageView) view.findViewById(R.id.imgCreateGroup);
		 * imgCreateGroup.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { startActivity(new
		 * Intent(getActivity(), CreateGroupActivity.class));
		 * 
		 * } });
		 */
		lvMyGroups = (InScrollListView) view.findViewById(R.id.lvMyGroups);
		lvImInGroups = (InScrollListView) view.findViewById(R.id.lvImInGroups);
		listMyCreate = new ArrayList<Groups>();
		// listImIN = new ArrayList<EMGroup>();
		// 假数据
		// List<EMGroup> emGroupList =
		// EMGroupManager.getInstance().getAllGroups();

		// listImIN.add(emGroupList);
		// listImIN.add(gm3);
		// listImIN.add(gm4);
		groupAdapter = new GroupAdapter(getActivity(), listMyCreate);
		lvMyGroups.setAdapter(groupAdapter);
		lvImInGroups
				.setAdapter(adapterImIn = adapterImIn == null ? new GroupAdapter(
						getActivity(),
						listImIN = listImIN == null ? new LinkedList<Groups>()
								: listImIN) : adapterImIn);
		// lvImInGroups.setAdapter(new GroupAdapter(getActivity(), listIm));

		// 获取我创建的
		getMyCreateGroups();

		// 获取我加入的群
		getImInGroups();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 我创建的群组
	 */
	private void getMyCreateGroups() {
		JSONObject ob = new JSONObject();
		ob.put("userId", appContext.getUserInfo().getUserId());
		HttpRequestFactory.getInstance().postRequest(Urls.MY_CREATE_GROUPS, ob,
				myCreateHander);

	}

	/**
	 * 我加入的群组
	 */
	private void getImInGroups() {
		JSONObject ob = new JSONObject();
		ob.put("userId", appContext.getUserInfo().getUserId());
		ob.put("pageNumber", AppUtil.getPageNum(listMyCreate.size(), pageSize));
		ob.put("pageSize", pageSize);
		HttpRequestFactory.getInstance().getRequest(Urls.IM_IN_GROUPS, ob,
				mImInHandler);
	}

	/**
	 * 查询我创建的群组
	 */
	AsyncHttpResponseHandler myCreateHander = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			if (!isLoadingDialogShowing()) {
				showLoadingDialog();
			}
		};

		@Override
		public void onSuccess(String content) {
			GroupsModel gm = new GroupsModel();
			gm = JsonUtil.convertJsonToObject(content, GroupsModel.class);
			if (gm != null
					&& gm.getCode().equalsIgnoreCase(
							GlobleConstant.REQUEST_SUCCESS)) {
				if (listMyCreate.size() > 0)
					listMyCreate.clear();
				listMyCreate.addAll(gm.getResult());
				groupAdapter.notifyDataSetChanged();
			}

		};

		@Override
		public void onFailure(Throwable error) {
		};

		@Override
		public void onFinish() {
			System.out.println("创建finish");
			dismissLoadingDialog();
		};

	};

	/**
	 * 查询我加入的
	 */
	AsyncHttpResponseHandler mImInHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			if (!isLoadingDialogShowing()) {
				showLoadingDialog();
			}
		};

		@Override
		public void onSuccess(String content) {
			GroupsModel gm = new GroupsModel();
			gm = JsonUtil.convertJsonToObject(content, GroupsModel.class);
			if (gm != null
					&& gm.getCode().equalsIgnoreCase(
							GlobleConstant.REQUEST_SUCCESS)) {
				if (listImIN.size() > 0)
					listImIN.clear();
				listImIN.addAll(gm.getResult());
				adapterImIn.notifyDataSetChanged();
			}
		};

		@Override
		public void onFailure(Throwable error) {
		};

		@Override
		public void onFinish() {
			System.out.println("加入finish");
			dismissLoadingDialog();
		};

	};

}
