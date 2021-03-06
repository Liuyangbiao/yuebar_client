package com.fullteem.yueba.app.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.SwipeAdapter;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.app.ui.ChatActivity;
import com.fullteem.yueba.app.ui.MainActivity;
import com.fullteem.yueba.db.InviteMessgeDao;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.SwipeListView;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月26日&emsp;下午3:21:25
 * @use 主界面->消息
 */
public class MessageMainFragment extends BaseFragment {
	private View view;
	private TopTitleView topTitle;
	private SwipeListView chatListView;
	private List<EMConversation> conversationList;
	private List<EMGroup> groups;

	/**
	 * 输入法管理
	 */
	private InputMethodManager inputMethodManager;
	private SwipeAdapter msgAdapter;
	private AppContext appContext;
	private boolean hidden;

	public MessageMainFragment() {
	}

	private static MessageMainFragment instance;

	public static MessageMainFragment getInstance() {
		if (instance == null) {
			instance = new MessageMainFragment();
		}
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_message_main, container,
				false);
		appContext = (AppContext) getActivity().getApplication();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		topTitle = (TopTitleView) getView().findViewById(R.id.toptile);
		topTitle.showCenterButton("消息", null, null, new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "你好", Toast.LENGTH_SHORT).show();
			}
		});

		/*
		 * topTitle.showRightImag(BitmapManager.getBitmapFromDrawable(getActivity
		 * (), R.drawable.message_friends_icon), new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(getActivity(), FriendsListActivity.class);
		 * startActivity(intent); } });
		 */

		initData();
	}

	private void initData() {
		inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		chatListView = (SwipeListView) getView().findViewById(R.id.chatList);
		conversationList = loadConversationsWithRecentChat();
		msgAdapter = new SwipeAdapter(getActivity(), 1, conversationList,
				GlobleConstant.RIGHT_LENGTH,
				new SwipeAdapter.IOnItemRightClickListener() {

					@Override
					public void onRightClick(View v, int position) {
						deleMsg(position);
					}
				});
		// 设置adapter
		chatListView.setAdapter(msgAdapter);
		// 群组
		groups = EMGroupManager.getInstance().getAllGroups();

		chatListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				UserCommonModel uc = ((SwipeAdapter) parent.getAdapter())
						.getUserInfo(position);
				EMConversation conversation = ((SwipeAdapter) parent
						.getAdapter()).getItem(position);
				String userName = conversation.getUserName();// 环信的用户名，格式mumu_test_100076
				String nickName = "我的好友";
				String imageUrl = "";
				EMMessage msg = conversation.getMessage(conversation
						.getMsgCount() - 1);
				try {
					nickName = msg.getStringAttribute("nickname");
					imageUrl = msg.getStringAttribute("imgurl");
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
				if (userName.equals(appContext.getUserInfo().getImServerId()))
					((BaseActivity) getActivity()).showToast("不能和自己聊天");// compare
																		// to IM
																		// id
				else {
					// 进入聊天页面
					Intent intent = new Intent();
					EMContact emContact = null;
					groups = EMGroupManager.getInstance().getAllGroups();
					for (EMGroup group : groups) {
						if (group.getGroupId().equals(userName)) {
							emContact = group;
							break;
						}
					}
					if (emContact != null && emContact instanceof EMGroup) {
						// it is group chat
						intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
						intent.putExtra("groupId",
								((EMGroup) emContact).getGroupId());
						//intent.putExtra("imgurl", ((EMGroup) emContact).get);
					} else {
						// it is single chat
						if (uc != null) {
							intent.putExtra("userId", userName);
							intent.putExtra("nickname", uc.getUserName());
							System.out.println("nickname: " + uc.getUserName());
							intent.putExtra("imgurl", uc.getUserLogoUrl());
						}
					}
					intent.setClass(getActivity(), ChatActivity.class);
					startActivity(intent);
				}
			}
		});

		// 注册上下文菜单
		registerForContextMenu(chatListView);
		chatListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				hideSoftKeyboard();
				return false;
			}
		});

	}

	/**
	 * 隐藏软键盘
	 */
	void hideSoftKeyboard() {
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(getActivity()
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		List<EMConversation> list = new ArrayList<EMConversation>();
		// 过滤掉messages size为0的conversation
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getAllMessages().size() != 0)
				list.add(conversation);
		}
		// 排序
		sortConversationByLastChatTime(list);
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1,
					final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage
						.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage
						.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		msgAdapter.notifyDataSetChanged();
		((MainActivity) getActivity()).updateUnreadLabel();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		UmengUtil.onPageStart(getActivity(),"MainScreen");
		if (!hidden) {
			refresh();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		UmengUtil.onPageStart(getActivity(),"MainScreen");
	}

	/**
	 * 删除消息方法
	 */
	private void deleMsg(int position) {
		EMConversation tobeDeleteUser = msgAdapter.getItem(position);
		boolean isGroup = false;
		if (tobeDeleteUser.isGroup())
			isGroup = true;
		// 删除此会话
		EMChatManager.getInstance().deleteConversation(
				tobeDeleteUser.getUserName(), isGroup);
		InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
		inviteMessgeDao.deleteMessage(tobeDeleteUser.getUserName());
		msgAdapter.remove(tobeDeleteUser);
		msgAdapter.notifyDataSetChanged();
		// 更新消息未读数
		((MainActivity) getActivity()).updateUnreadLabel();
	}
}
