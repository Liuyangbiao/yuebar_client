package com.fullteem.yueba.app.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.alibaba.fastjson.JSONObject;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.ui.GroupDetailActivity;
import com.fullteem.yueba.app.ui.PerssonalInfoAcitivity;
import com.fullteem.yueba.db.DBFriendListDao;
import com.fullteem.yueba.db.DBGroupsDao;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.Group;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.SmileUtils;
import com.fullteem.yueba.widget.CircleImageView;
import com.umeng.analytics.MobclickAgent;

public class SwipeAdapter extends ArrayAdapter<EMConversation> {
	/**
	 * 上下文对象
	 */
	private Context mContext = null;

	/**
     * 
     */
	private int mRightWidth = 0;

	/**
	 * 单击事件监听器
	 */
	private IOnItemRightClickListener mListener = null;

	private List<EMConversation> conversationList;
	private List<EMConversation> copyConversationList;
	private ConversationFilter conversationFilter;
	private final static int GET_DB = 101;
	private final static int SET_DB = 102;
	private List<UserCommonModel> userList;

	public interface IOnItemRightClickListener {
		void onRightClick(View v, int position);
	}

	public SwipeAdapter(Context context, int textViewResourceId,
			List<EMConversation> objects, int rightWidth,
			IOnItemRightClickListener l) {
		super(context, textViewResourceId, objects);
		mContext = context;
		mRightWidth = rightWidth;
		mListener = l;
		//userList = new ArrayList<UserCommonModel>();
		this.conversationList = objects;
		copyConversationList = new ArrayList<EMConversation>();
		copyConversationList.addAll(objects);
	}

	@Override
	public int getCount() {
		return conversationList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder item;
		final int thisPosition = position;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_chathistory_swipelistview, parent, false);
			item = new ViewHolder();
			item.item_left = convertView.findViewById(R.id.item_left);
			item.item_right = convertView.findViewById(R.id.item_right);
			// item.item_left_txt =
			// (TextView)convertView.findViewById(R.id.item_left_txt);
			// item.item_right_txt =
			// (TextView)convertView.findViewById(R.id.item_right_txt);
			item.name = (TextView) convertView.findViewById(R.id.name);

			item.unreadLabel = (TextView) convertView
					.findViewById(R.id.unread_msg_number);
			item.message = (TextView) convertView.findViewById(R.id.message);
			item.time = (TextView) convertView.findViewById(R.id.time);
			item.avatar = (CircleImageView) convertView
					.findViewById(R.id.avatar);
			item.msgState = convertView.findViewById(R.id.msg_state);
			item.list_item_layout = (RelativeLayout) convertView
					.findViewById(R.id.list_item_layout);
			convertView.setTag(item);
		} else {
			// 有直接获得Viewitem
			item = (ViewHolder) convertView.getTag();
		}
		userList = new ArrayList<UserCommonModel>();
		LinearLayout.LayoutParams lp1 = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		item.item_left.setLayoutParams(lp1);
		LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		item.item_right.setLayoutParams(lp2);
		item.item_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightClick(v, thisPosition);
				}
			}
		});

		// 环信部分
		// 获取与此用户/群组的会话
		EMConversation conversation = getItem(position);

		if (conversation.getUnreadMsgCount() > 0) {
			// 显示与此用户的消息未读数
			item.unreadLabel.setText(String.valueOf(conversation
					.getUnreadMsgCount()));
			item.unreadLabel.setVisibility(View.VISIBLE);
		} else {
			item.unreadLabel.setVisibility(View.INVISIBLE);
		}

		if (conversation.getMsgCount() != 0) {
			// 把最后一条消息的内容作为item的message内容
			EMMessage lastMessage = conversation.getLastMessage();
			item.message
					.setText(
							SmileUtils.getSmiledText(
									getContext(),
									getMessageDigest(lastMessage,
											(this.getContext()))),
							BufferType.SPANNABLE);

			item.time.setText(DateUtils.getTimestampString(new Date(lastMessage
					.getMsgTime())));
			if (lastMessage.direct == EMMessage.Direct.SEND
					&& lastMessage.status == EMMessage.Status.FAIL) {
				item.msgState.setVisibility(View.VISIBLE);
			} else {
				item.msgState.setVisibility(View.GONE);
			}
		}

		// 因为每次对话都会携带参数，所以只要取第一次对话的信息即可
		EMMessage msg = conversation.getLastMessage();

		String from = msg.getFrom();

		String str1 = "", str2 = "", str3 = "", str4 = "";
		boolean isF;
		final String mobile = conversation.getUserName();
		// 若消息来源与对象为同一个号码，则说明该条消息为接收到的消息，否则就是自己发出去的消息
		if (from.equalsIgnoreCase(mobile)) {
			isF = false;
		} else {
			isF = true;
		}

		final boolean isFromMySelf = isF;
		try {
			if (msg != null) {
				str1 = msg.getStringAttribute("nickname");
				str2 = msg.getStringAttribute("imgurl");
				str3 = msg.getStringAttribute("userid");
			}
		} catch (EaseMobException e) {
			e.printStackTrace();
		}

		if (GlobleConstant.MESSAGE_TYPE.equalsIgnoreCase(str4)) {
			item.message.setTextColor(mContext.getResources().getColor(
					R.color.girl_red));
		} else {
			item.message.setTextColor(mContext.getResources().getColor(
					R.color.black));
		}

		if (conversation.getUnreadMsgCount() > 0) {
			// 显示与此用户的消息未读数
			item.unreadLabel.setText(String.valueOf(conversation
					.getUnreadMsgCount()));
			item.unreadLabel.setVisibility(View.VISIBLE);
		} else {
			item.unreadLabel.setVisibility(View.INVISIBLE);
		}

		if (conversation.getMsgCount() != 0) {
			// 把最后一条消息的内容作为item的message内容
			EMMessage lastMessage = conversation.getLastMessage();
			item.message
					.setText(
							SmileUtils.getSmiledText(
									getContext(),
									getMessageDigest(lastMessage,
											(this.getContext()))),
							BufferType.SPANNABLE);

			item.time.setText(DateUtils.getTimestampString(new Date(lastMessage
					.getMsgTime())));
			if (lastMessage.direct == EMMessage.Direct.SEND
					&& lastMessage.status == EMMessage.Status.FAIL) {
				item.msgState.setVisibility(View.VISIBLE);
			} else {
				item.msgState.setVisibility(View.GONE);
			}
		}

		final String nickName = str1;
		final String imgUrl = str2;
		final String userId = str3;
		// 获取用户username或者群组groupid
		final String username = conversation.getUserName();
		List<EMGroup> groups = EMGroupManager.getInstance().getAllGroups();
		EMContact contact = null;
		boolean isGroup = false;
		for (EMGroup group : groups) {
			if (group.getGroupId().equals(username)) {
				isGroup = true;
				contact = group;
				break;
			}
		}

		if (isGroup) {
			// 若消息来源为群，根据群id去获取群信息

			// 群聊消息，显示群聊头像
			// item.avatar.setImageResource(R.drawable.group_icon);
			item.name.setText(contact.getNick() != null ? contact.getNick()
					: username);
			// final ImageView imageView = item.avatar;

			DBGroupsDao dao = new DBGroupsDao(mContext);
			final Group group = dao.getGroup(contact.getUsername());
			if (group.getGroupId() == null || group.getGroupIcon() == null) {
				getGroupInfoRequest(contact.getUsername(), item.avatar);
			} else {
				ImageLoaderUtil.getImageLoader().displayImage(
						DisplayUtils.getAbsolutelyUrl(group.getGroupIcon()),
						item.avatar);
			}
		} else {
			// 本地或者服务器获取用户详情，以用来显示头像和nick
			// item.avatar.setImageResource(R.drawable.default_avatar);
			if (username.equals(GlobleConstant.GROUP_USERNAME)) {
				item.name.setText("群聊");

			} else if (username.equals(GlobleConstant.NEW_FRIENDS_USERNAME)) {
				item.name.setText("申请与通知");
			}

			final TextView ftvName = item.name;
			final CircleImageView headerImage = item.avatar;
			// 群聊不走此处
			if (!username.equals(GlobleConstant.GROUP_USERNAME)) {

				// 此处根据username在数据库里面查询对面的信息
				showInfoHandler.post(new Runnable() {
					@Override
					public void run() {
						// 只有非自己发送的消息时走下面路线
						DBFriendListDao dbdao = new DBFriendListDao(mContext);
						// 此处的username为imServerId
						UserCommonModel uc = dbdao.getContacter(username);
						uc.setNameTextView(ftvName);
						uc.setIvHeader(headerImage);
						// 若数据库有此信息则对比用户名和imageurl没有变化之后直接取其信息，若没有或者有差异则做储存动作
						if (uc != null
								&& uc.getUserMobile() != null
								&& imgUrl.equalsIgnoreCase(uc.getUserLogoUrl())
								&& username.equalsIgnoreCase(uc.getImServerId())) {
							showInfoHandler.obtainMessage(GET_DB, uc)
									.sendToTarget();
						} else {
							if (!isFromMySelf) {
								UserCommonModel userc = new UserCommonModel();
								userc.setUserLogoUrl(imgUrl);
								userc.setUserName(nickName);
								userc.setUserId(userId);
								// userc.setUserMobile(mobile);
								userc.setImServerId(username);
								// 存储这个用户
								dbdao.saveContacter(userc);

								// 最后储存textview对象，不能将其保存在数据库里
								userc.setNameTextView(ftvName);
								userc.setIvHeader(headerImage);
								showInfoHandler.obtainMessage(GET_DB, userc)
										.sendToTarget();
							} else {
								showInfoHandler.obtainMessage(GET_DB, uc)
										.sendToTarget();
							}
						}
					}
				});
			}
			// item.name.setText(username);
		}
		final EMContact groupContact = contact;
		final boolean lastIsGroup = isGroup;
		findUserIdByAccount(username, item.avatar, lastIsGroup);
		// 点击头像进入个人信息/群组详情界面
		item.avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent;
				if (lastIsGroup) {
					intent = new Intent(mContext, GroupDetailActivity.class);
					intent.putExtra("GROUP_ID", groupContact.getUsername());
					mContext.startActivity(intent);
					// } else {
					// intent = new Intent(mContext,
					// PerssonalInfoAcitivity.class);
					// intent.putExtra("userId", toUserId);
					// System.out.println("聊天对象userId:"+toUserId);
					// mContext.startActivity(intent);
				}
			}
		});
		return convertView;
	}

	/**
	 * 通过mumu号获取用户Id
	 * 
	 * @param username
	 * @param avatar
	 * @param lastIsGroup
	 * @param groupContact
	 */
	private void findUserIdByAccount(String username,
			final CircleImageView avatar, final boolean lastIsGroup) {
		JSONObject jo = new JSONObject();
		jo.put("userAccountStr", username);
		HttpRequestFactory.getInstance().getRequest(Urls.FRIENDS_INFO, jo,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						if (null == content || "".equals(content)) {
							return;
						}
						org.json.JSONObject json;
						try {
							json = new org.json.JSONObject(content);
							org.json.JSONObject result = json
									.getJSONObject("result");
							JSONArray userInfo = result
									.getJSONArray("userInfoList");
							org.json.JSONObject info = userInfo
									.getJSONObject(0);
							final String userId = info.getString("userId");
							avatarEvent(avatar, lastIsGroup, userId);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						System.out.println("get date from server error");
					}
				});
	}

	/**
	 * 点击头像进入个人信息/群组详情界面
	 * 
	 * @param avatar
	 * @param lastIsGroup
	 * @param userId
	 */
	private void avatarEvent(final CircleImageView avatar,
			final boolean lastIsGroup, final String userId) {
		avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent;
				if (!lastIsGroup) {
					intent = new Intent(mContext, PerssonalInfoAcitivity.class);
					intent.putExtra("userId", userId);
					System.out.println("聊天对象userId:" + userId);
					mContext.startActivity(intent);
				}
			}
		});
	}

	private class ViewHolder {
		View item_right;

		View item_left;

		/** 和谁的聊天记录 */
		TextView name;
		/** 消息未读数 */
		TextView unreadLabel;
		/** 最后一条消息的内容 */
		TextView message;
		/** 最后一条消息的时间 */
		TextView time;
		/** 用户头像 */
		CircleImageView avatar;
		/** 最后一条消息的发送状态 */
		View msgState;
		/** 整个list中每一行总布局 */
		RelativeLayout list_item_layout;
	}

	private class ConversationFilter extends Filter {
		List<EMConversation> mOriginalValues = null;

		public ConversationFilter(List<EMConversation> mList) {
			mOriginalValues = mList;
		}

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				mOriginalValues = new ArrayList<EMConversation>();
			}
			if (prefix == null || prefix.length() == 0) {
				results.values = copyConversationList;
				results.count = copyConversationList.size();
			} else {
				String prefixString = prefix.toString();
				final int count = mOriginalValues.size();
				final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

				for (int i = 0; i < count; i++) {
					final EMConversation value = mOriginalValues.get(i);
					String username = value.getUserName();

					EMGroup group = EMGroupManager.getInstance().getGroup(
							username);
					if (group != null) {
						username = group.getGroupName();
					}

					// First match against the whole ,non-splitted value
					if (username.startsWith(prefixString)) {
						newValues.add(value);
					} else {
						final String[] words = username.split(" ");
						final int wordCount = words.length;

						// Start at index 0, in case valueText starts with
						// space(s)
						for (int k = 0; k < wordCount; k++) {
							if (words[k].startsWith(prefixString)) {
								newValues.add(value);
								break;
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			conversationList.clear();
			conversationList.addAll((List<EMConversation>) results.values);
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}

		}

	}

	@Override
	public Filter getFilter() {
		if (conversationFilter == null) {
			conversationFilter = new ConversationFilter(conversationList);
		}
		return conversationFilter;
	}

	String getStrng(Context context, int resId) {
		return context.getResources().getString(resId);
	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				// 从sdk中提到了ui中，使用更简单不犯错的获取string的方法
				// digest = EasyUtils.getAppResourceString(context,
				// "location_recv");
				digest = getStrng(context, R.string.location_recv);
				digest = String.format(digest, message.getFrom());
				return digest;
			} else {
				// digest = EasyUtils.getAppResourceString(context,
				// "location_prefix");
				digest = getStrng(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 图片消息
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			digest = getStrng(context, R.string.picture)
					+ imageBody.getFileName();
			break;
		case VOICE:// 语音消息
			digest = getStrng(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getStrng(context, R.string.video);
			break;
		case TXT: // 文本消息
			if (!message.getBooleanAttribute(
					GlobleConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = txtBody.getMessage();
			} else {
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				digest = getStrng(context, R.string.voice_call)
						+ txtBody.getMessage();
			}
			break;
		case FILE: // 普通文件消息
			digest = getStrng(context, R.string.file);
			break;
		default:
			System.err.println("error, unknow type");
			return "";
		}

		return digest;
	}

	/**
	 * UI处理
	 */
	Handler showInfoHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case GET_DB:
				UserCommonModel uc = (UserCommonModel) msg.obj;
				if (uc != null) {
					uc.getNameTextView().setText(uc.getUserName());
				}
				CircleImageView civ = uc.getIvHeader();
				if (civ != null) {
					ImageLoaderUtil.getImageLoader().displayImage(
							DisplayUtils.getAbsolutelyUrl(uc.getUserLogoUrl()),
							civ);
				}
				userList.add(uc);
				System.out.println("userList的长度： "+userList.size());
				break;
			case SET_DB:
				break;
			default:
				break;
			}
		};
	};

	public UserCommonModel getUserInfo(int position) {
		if (userList.size() > 0) {
			for (UserCommonModel user : userList) {
				String mumuId = user.getImServerId();
				if (null == mumuId) {
					mumuId = "";
				}
				if (mumuId.equalsIgnoreCase(conversationList.get(position)
						.getUserName())) {
					return user;
				}
			}

			return userList.get(position);
		} else {
			return null;
		}
	}

	/**
	 * 获取群信息接口
	 */
	private void getGroupInfoRequest(String groupId,
			final CircleImageView headImg) {
		JSONObject jo = new JSONObject();
		jo.put("groupId", groupId);
		jo.put("lng", AppContext.getApplication().getLocation().getLongitude());
		jo.put("lat", AppContext.getApplication().getLocation().getLatitude());

		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		HttpRequest.getInstance().getGroupDetail(userId, groupId,
				new CustomAsyncResponehandler() {

					@Override
					public void onSuccess(ResponeModel baseModel) {
						Group groupModel = (Group) baseModel.getObject(
								"barGroupList", Group.class);
						if (groupModel != null) {
							DBGroupsDao dao = new DBGroupsDao(mContext);
							Group group = new Group();
							group.setGroupIcon(groupModel.getGroupIcon());
							group.setGroupId(groupModel.getGroupId());
							group.setGroupName(groupModel.getGroupName());
							dao.saveGroup(group);

							// 设置图片
							ImageLoaderUtil.getImageLoader().displayImage(
									DisplayUtils.getAbsolutelyUrl(group
											.getGroupIcon()), headImg);
						}

					}

					@Override
					public void onFailure(Throwable error, String content) {

					}
				});
	}
}
