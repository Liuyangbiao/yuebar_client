package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.BaseListAdapter;
import com.fullteem.yueba.db.DBFriendListDao;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.NearbyPubFriendsModel;
import com.fullteem.yueba.model.NearbyPubFriendsModel.PubFriendsModel;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.AppUtil;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.StringUtils.Gender;
import com.fullteem.yueba.widget.CircleImageView;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * 好友列表fragment
 * 
 * @author ssy
 */
public class MyBarFriendListActivity extends BaseActivity implements
		OnItemClickListener {
	private ListView lvfriendsList;
	private AppContext appContext;

	/**
	 * 用户List
	 */
	private List<PubFriendsModel> listContact;

	/**
	 * 黑名单
	 */
	private List<String> blackList;

	private final int pageSize = 10;

	private FriendsListAdapter friendsListAdapter;
	private TopTitleView topTitle;

	public MyBarFriendListActivity() {
		super(R.layout.activity_my_bar_friendslist);
	}

	@Override
	public void initViews() {
		initTitle();
		appContext = AppContext.getApplication();
		// TODO Auto-generated method stub
		listContact = new ArrayList<PubFriendsModel>();
		getContactList();
		lvfriendsList = (ListView) findViewById(R.id.lvfriendsList);
	}

	private void initTitle() {
		topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		topTitle.showCenterText(getString(R.string.discover_myFollow), null);
	}

	@Override
	public void bindViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// 移出黑民单
		// blackList = EMContactManager.getInstance().getBlackListUsernames();
		// for (String str : blackList) {
		// try {
		// EMContactManager.getInstance().deleteUserFromBlackList(str);
		// } catch (EaseMobException e) {
		// System.out.println(e.toString());
		// }
		// }
		friendsListAdapter = new FriendsListAdapter(this, listContact);
		lvfriendsList.setAdapter(friendsListAdapter);
		lvfriendsList.setOnItemClickListener(this);
	}

	/**
	 * @author ssy 好友列表
	 */
	class FriendsListAdapter extends BaseListAdapter<PubFriendsModel> {
		private Context context;

		public FriendsListAdapter(Activity context, List<PubFriendsModel> list) {
			super(context, listContact);
			this.context = context;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.adapter_barfriends_xlistview, null);
				vh = new ViewHolder();
				vh.headerImg = (CircleImageView) convertView
						.findViewById(R.id.ImgViewHeader);
				vh.tvName = (TextView) convertView.findViewById(R.id.tvName);
				vh.tvSpace = (TextView) convertView.findViewById(R.id.tvSpace);
				vh.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
				vh.tvSex = (TextView) convertView.findViewById(R.id.tvSex);
				vh.tvMood = (TextView) convertView.findViewById(R.id.tvMood);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.tvName.setText(listContact.get(position).getUserName());
			ImageLoaderUtil.getImageLoader().displayImage(
					DisplayUtils.getAbsolutelyUrl(listContact.get(position)
							.getUserLogoUrl()), vh.headerImg);
			vh.tvSpace.setText(listContact.get(position).getDistance());
			vh.tvTime.setText(listContact.get(position).getCreateDate1());

			String mood = listContact.get(position).getUserAsign();
			if (StringUtils.isEmpty(mood)) {
				vh.tvMood.setText("签名什么的我不在意");
			} else
				vh.tvMood.setText(listContact.get(position).getUserAsign());
			if ("男".equalsIgnoreCase(listContact.get(position).getUserSex())) {
				StringUtils.changeStyle(context, vh.tvSex, Gender.GENDER_BOY);
				// tvSex.setBackgroundResource(R.drawable.tv_gender_selector);
			} else {
				StringUtils.changeStyle(context, vh.tvSex, Gender.GENDER_GIRL);
				// tvSex.setBackgroundResource(R.drawable.barfriend_boy_bg);
			}
			vh.tvSex.setText(listContact.get(position).getUserAge());
			vh.headerImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MyBarFriendListActivity.this,
							PerssonalInfoAcitivity.class);
					intent.putExtra("userId", listContact.get(position)
							.getUserId());
					startActivity(intent);
				}
			});
			return convertView;
		}
	}

	/**
	 * 获取联系人列表
	 */
	private void getContactList() {
		// try {
		// // usernames = EMContactManager.getInstance().getContactUserNames();
		// usernames = EMChatManager.getInstance().getContactUserNames();
		// } catch (EaseMobException e) {
		// showToast("获取好友失败");
		// }
		// if (usernames == null)
		// return;
		// EMLog.d("roster", "contacts size: " + usernames.size());
		// Map<String, User> userlist = new HashMap<String, User>();
		// for (String username : usernames) {
		// User user = new User();
		// user.setUsername(username);
		// userlist.put(username, user);
		// }
		// Iterator<Entry<String, User>> iterator =
		// userlist.entrySet().iterator();
		// while (iterator.hasNext()) {
		// Entry<String, User> entry = iterator.next();
		// if (!entry.getKey().equals(GlobleConstant.NEW_FRIENDS_USERNAME)
		// && !entry.getKey().equals(GlobleConstant.GROUP_USERNAME))
		// listContact.add(entry.getValue());
		// }

		JSONObject jo = new JSONObject();
		jo.put("userId", appContext.getUserInfo().getUserId());
		jo.put("pageNumber", AppUtil.getPageNum(listContact.size(), pageSize));
		jo.put("pageSize", pageSize);
		HttpRequestFactory.getInstance().getRequest(Urls.GET_FRIEND_LIST, jo,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						((BaseActivity) context).showLoadingDialog();
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						listContact.clear();
						NearbyPubFriendsModel nearModel = JsonUtil
								.convertJsonToObject(content,
										NearbyPubFriendsModel.class);
						if (nearModel != null
								&& GlobleConstant.REQUEST_SUCCESS
										.equalsIgnoreCase(nearModel.getCode())) {
							if (nearModel.getResult() != null) {
								listContact.addAll(nearModel.getResult());
								friendsListAdapter.notifyDataSetChanged();
							}
						}
					}

					@Override
					public void onFinish() {
						super.onFinish();
						Activity activity = ((BaseActivity) context);
						if (activity != null)
							((BaseActivity) activity).dismissLoadingDialog();
					}
				});

	}

	class ViewHolder {
		CircleImageView headerImg;
		TextView tvName;
		TextView tvSex;
		TextView tvMood;
		TextView tvSpace;
		TextView tvTime;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		// 栏目点击跳转
		Intent intent = new Intent(MyBarFriendListActivity.this,
				ChatActivity.class);
		intent.putExtra("userId", listContact.get(position).getImServerId());
		intent.putExtra("nickname", listContact.get(position).getUserName());
		intent.putExtra("imgurl", listContact.get(position).getUserLogoUrl());

		// 直接在跳转之前就在数据库插入信息
		new Thread(new Runnable() {
			@Override
			public void run() {
				DBFriendListDao dao = new DBFriendListDao(
						MyBarFriendListActivity.this);
				UserCommonModel user = new UserCommonModel();
				user.setUserMobile(listContact.get(position).getUserMobile());
				user.setUserId(listContact.get(position).getUserId());
				user.setUserName(listContact.get(position).getUserName());
				String imgUrl;
				if (listContact.get(position).getUserLogoUrl() == null) {
					imgUrl = "null";
				} else {
					imgUrl = listContact.get(position).getUserLogoUrl();
				}
				user.setUserLogoUrl(imgUrl);
				dao.saveContacter(user);
			}
		}).start();
		startActivity(intent);
	}
}
