package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.GroupMemberDetailListAdapter;
import com.fullteem.yueba.db.DBFriendListDao;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.NearbyGroupMemberDetailModel;
import com.fullteem.yueba.model.NearbyGroupMemberDetailModel.GroupMemberDetailModel;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.AppUtil;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.TopTitleView;
import com.fullteem.yueba.widget.XListView;
import com.fullteem.yueba.widget.XListView.IXListViewListener;

/**
 * 群成员列表展示页
 * 
 * @author wzhx
 * 
 */
public class GroupMemberDetailActivity extends BaseActivity {

	private XListView xListGroupMember;
	private View lvLoading;
	private TopTitleView topTitle;
	private AppContext appContext;
	private GroupMemberDetailListAdapter grouMemberlistAdapter;
	private List<GroupMemberDetailModel> listData;
	private int currentPosition;
	private String groupId;
	private boolean isRefresh = false;

	private int PAGE_NUM = 1;
	private int PAGE_SIZE = 10;

	private static final int GROUP_MEMBER_BACK = 0;

	public GroupMemberDetailActivity() {
		super(R.layout.activity_group_member_detail);
	}

	@Override
	public void initViews() {

		initTitle();

		groupId = getIntent().getStringExtra("groupId");

		xListGroupMember = (XListView) findViewById(R.id.list_group_members);
		EmptyView emptyView = new EmptyView(this);
		emptyView.setVisibility(View.GONE);
		((ViewGroup) xListGroupMember.getParent()).addView(emptyView);
		xListGroupMember.setEmptyView(emptyView);

		lvLoading = findViewById(R.id.lvLoading);
		lvLoading.setVisibility(View.GONE);

	}

	private void initTitle() {
		topTitle = (TopTitleView) findViewById(R.id.top_title);

		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// center
		topTitle.showCenterText(getResString(R.string.group_member_list), null);

	}

	@Override
	public void initData() {
		listData = new ArrayList<NearbyGroupMemberDetailModel.GroupMemberDetailModel>();
		appContext = (AppContext) this.getApplication();
		grouMemberlistAdapter = new GroupMemberDetailListAdapter(this, listData);
		xListGroupMember.setAdapter(grouMemberlistAdapter);
		loadDate();

	}

	/**
	 * 从服务器获取群成员信息详情
	 */
	private void loadDate() {
		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId == null || userId == -1 || TextUtils.isEmpty(groupId))
			return;
		JSONObject jo = new JSONObject();
		System.out.println("groupId:" + groupId);
		jo.put("pageNumber", AppUtil.getPageNum(listData.size(), PAGE_SIZE));
		jo.put("pageSize", PAGE_SIZE);
		jo.put("groupId", groupId);

		HttpRequestFactory.getInstance().getRequest(Urls.GROPU_MEMBER_DETAIL,
				jo, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						NearbyGroupMemberDetailModel nearGroupMemberModel = new NearbyGroupMemberDetailModel();
						nearGroupMemberModel = JsonUtil.convertJsonToObject(
								content, NearbyGroupMemberDetailModel.class);
						if (nearGroupMemberModel != null
								&& GlobleConstant.REQUEST_SUCCESS
										.equalsIgnoreCase(nearGroupMemberModel
												.getCode())) {
							listData.addAll(nearGroupMemberModel.getResult());
							grouMemberlistAdapter.notifyDataSetChanged();
							UIhandler.sendEmptyMessage(GROUP_MEMBER_BACK);
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						super.onFailure(error, content);
					}

				});
	}

	@Override
	public void bindViews() {
		// 刷新页面
		xListGroupMember.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				listData.clear();
				isRefresh = true;
				loadDate();
			}

			@Override
			public void onLoadMore() {
				isRefresh = false;
				loadDate();
			}
		});

		// 进入聊天页面
		xListGroupMember.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0 || position == listData.size() + 1)
					return;
				currentPosition = position - 1;
				Intent intent = new Intent(GroupMemberDetailActivity.this,
						ChatActivity.class);
				intent.putExtra("userId", listData.get(currentPosition)
						.getImServerId());
				intent.putExtra("nickname", listData.get(currentPosition)
						.getUserName());
				intent.putExtra("imgurl", listData.get(currentPosition)
						.getUserLogoUrl());

				// 直接在跳转之前就在数据库插入信息
				new Thread(new Runnable() {
					@Override
					public void run() {
						DBFriendListDao dao = new DBFriendListDao(
								GroupMemberDetailActivity.this);
						UserCommonModel user = new UserCommonModel();
						user.setUserMobile(listData.get(currentPosition)
								.getUserMobile());
						user.setImServerId(listData.get(currentPosition)
								.getImServerId());
						user.setUserId(listData.get(currentPosition)
								.getUserId());
						user.setUserName(listData.get(currentPosition)
								.getUserName());
						String imgUrl;
						if (listData.get(currentPosition).getUserLogoUrl() == null) {
							imgUrl = "null";
						} else {
							imgUrl = listData.get(currentPosition)
									.getUserLogoUrl();
						}
						user.setUserLogoUrl(imgUrl);
						dao.saveContacter(user);
					}
				}).start();
				startActivity(intent);
			}
		});
	}

	Handler UIhandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GROUP_MEMBER_BACK:
				grouMemberlistAdapter.notifyDataSetChanged();
				// 设置加载更多是否可见
				xListGroupMember.setPullLoadEnable(StringUtils
						.isLoadMoreVisible(listData.size(), PAGE_SIZE));
				break;

			default:
				break;
			}
		};
	};
}
