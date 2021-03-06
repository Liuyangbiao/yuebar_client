package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.GroupAdapter;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.GroupsModel;
import com.fullteem.yueba.model.GroupsModel.Groups;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.widget.InScrollListView;
import com.fullteem.yueba.widget.TopTitleView;

public class GroupMyCreatedActivity extends BaseActivity {

	private InScrollListView lvMyGroups;
	private List<Groups> listMyCreate;
	private GroupAdapter groupAdapter;

	private TopTitleView topTitle;
	
	private RelativeLayout mRlGroupMyCreated;

	public GroupMyCreatedActivity() {
		super(R.layout.activity_group_my_created);
	}

	@Override
	public void initViews() {
		initToptile();
		lvMyGroups = (InScrollListView) findViewById(R.id.lvMyGroups);
		mRlGroupMyCreated = (RelativeLayout) findViewById(R.id.rl_group_my_created);
		listMyCreate = new ArrayList<Groups>();

	}

	private void initToptile() {
		// TODO Auto-generated method stub
		topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		topTitle.showCenterText(getString(R.string.group_mycreate), null);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		groupAdapter = new GroupAdapter(this, listMyCreate);
		lvMyGroups.setAdapter(groupAdapter);
		getMyCreateGroups();

	}

	@Override
	public void bindViews() {
		// TODO Auto-generated method stub

	}

	/**
	 * 我创建的群组
	 */
	private void getMyCreateGroups() {
		// TODO Auto-generated method stub
		JSONObject ob = new JSONObject();
		ob.put("userId", appContext.getUserInfo().getUserId());
		HttpRequestFactory.getInstance().postRequest(Urls.MY_CREATE_GROUPS, ob,
				myCreateHander);

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

				if (null != listMyCreate && listMyCreate.size() > 0) {
					mRlGroupMyCreated
							.setBackgroundResource(R.drawable.content_top_bg);
				} else {
					mRlGroupMyCreated
							.setBackgroundResource(R.drawable.date_item_content);
				}
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

	@Override
	protected void onResume() {
		super.onResume();
		if (listMyCreate != null && listMyCreate.size() > 0) {
			getMyCreateGroups();
		}
	}
}
