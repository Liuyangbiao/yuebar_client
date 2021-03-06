package com.fullteem.yueba.app.ui;

import java.util.LinkedList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
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
import com.fullteem.yueba.util.AppUtil;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.widget.InScrollListView;
import com.fullteem.yueba.widget.TopTitleView;

public class GroupImInActivity extends BaseActivity {

	private InScrollListView lvImInGroups;
	private List<Groups> listImIN;
	private GroupAdapter adapterImIn;

	private TopTitleView topTitle;

	private int pageSize = 10;

	public GroupImInActivity() {
		super(R.layout.activity_group_im_in);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		initTopTitle();

		lvImInGroups = (InScrollListView) findViewById(R.id.lvImInGroups);
		mTvGroupImIn = (TextView) findViewById(R.id.tv_groups_im_in);

	}

	private void initTopTitle() {
		// TODO Auto-generated method stub
		topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		topTitle.showCenterText(getString(R.string.group_imin), null);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		lvImInGroups
				.setAdapter(adapterImIn = adapterImIn == null ? new GroupAdapter(
						this,
						listImIN = listImIN == null ? new LinkedList<Groups>()
								: listImIN) : adapterImIn);
		// 获取我加入的群
		getImInGroups();

	}

	@Override
	public void bindViews() {
		// TODO Auto-generated method stub

	}

	/**
	 * 我加入的群组
	 */
	private void getImInGroups() {
		JSONObject ob = new JSONObject();
		ob.put("userId", appContext.getUserInfo().getUserId());
		ob.put("pageNumber", AppUtil.getPageNum(listImIN.size(), pageSize));
		ob.put("pageSize", pageSize);
		HttpRequestFactory.getInstance().getRequest(Urls.IM_IN_GROUPS, ob,
				mImInHandler);
	}

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

				if (null != listImIN && listImIN.size() > 0) {
					mTvGroupImIn
							.setBackgroundResource(R.drawable.content_top_bg);
				} else {
					mTvGroupImIn
							.setBackgroundResource(R.drawable.date_item_content);
				}
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
	private TextView mTvGroupImIn;

	@Override
	protected void onResume() {
		super.onResume();
		if (listImIN != null && listImIN.size() > 0) {
			getImInGroups();
		}
	}

}
