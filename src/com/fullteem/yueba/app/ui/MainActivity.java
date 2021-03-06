package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.NotificationCompat;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.EasyUtils;
import com.easemob.util.HanziToPinyin;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.fragment.DiscoverMainFragment;
import com.fullteem.yueba.app.ui.fragment.MessageMainFragment;
import com.fullteem.yueba.app.ui.fragment.NearbyMainFragment;
import com.fullteem.yueba.app.ui.fragment.PersonalMainFragment;
import com.fullteem.yueba.db.InviteMessgeDao;
import com.fullteem.yueba.db.UserDao;
import com.fullteem.yueba.download.UpdateManager;
import com.fullteem.yueba.engine.push.PushService;
import com.fullteem.yueba.entry.LoginActivity;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.InviteMessage;
import com.fullteem.yueba.model.InviteMessage.InviteMesageStatus;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.model.User;
import com.fullteem.yueba.model.VersionUpdateModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.util.CheckUtil;
import com.fullteem.yueba.util.CommonUtils;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.widget.HintConfirmationPopWindow;
import com.networkbench.agent.impl.NBSAppAgent;

public class MainActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		NBSAppAgent.setLicenseKey("c28e0710595e46db96e703c88e695eea")
				.withLocationServiceEnabled(true).start(this);
		super.onCreate(savedInstanceState);
	}

	private List<Fragment> mFragments;
	private List<TextView> mTvLists;
	private List<Drawable> mImgUnSelected;
	private List<Drawable> mImgSelected;
	private final int PAGE_TOATAL = 4;
	private MyListener mListener;
	private int lastIndex;

	/******************************************************* 环信处理部分 ********************************************************/
	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;
	// 账号在别处登录
	private boolean isConflict = false;
	/**
	 * 消息广播接受
	 */
	private NewYBMessageBroadcastReceiver msgReceiver;
	private static final int notifiId = 11;

	// 未读消息textview
	private TextView unreadLabel;
	private TextView unreadLabelSys;

	private boolean isVersionChecked;// 检测过版本更新

	public MainActivity() {
		super(R.layout.activity_main);
		LogUtil.LogDebug("运行时",
				"MainActivity onCreate:" + System.currentTimeMillis(), null);
	}

	@Override
	public void initViews() {
		mTvLists = new ArrayList<TextView>(PAGE_TOATAL);
		mTvLists.add((TextView) findViewById(R.id.tv_nearby));
		mTvLists.add((TextView) findViewById(R.id.tv_message));
		mTvLists.add((TextView) findViewById(R.id.tv_discover));
		mTvLists.add((TextView) findViewById(R.id.tv_personal));

		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		unreadLabelSys = (TextView) findViewById(R.id.unread_msg_number_presonal);
	}

	@Override
	public void initData() {
		LogUtil.LogDebug("运行时",
				"请求显示第一个Fragment:" + System.currentTimeMillis(), null);
		mListener = new MyListener();
		// for fragent
		mFragments = new ArrayList<Fragment>(PAGE_TOATAL);
		mFragments.add(NearbyMainFragment.getInstance());
		mFragments.add(MessageMainFragment.getInstance());
		mFragments.add(DiscoverMainFragment.getInstance());
		mFragments.add(PersonalMainFragment.getInstance());

		for (int i = 0; i < mFragments.size(); i++) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.ll_fragmentcontainer, mFragments.get(i)).commit();
		}
		((NearbyMainFragment) mFragments.get(0)).onLuckyMeet();

		// for show image
		int[] imgId = { R.drawable.img_main_nearby,
				R.drawable.img_main_message, R.drawable.img_main_discover,
				R.drawable.img_main_personal,
				R.drawable.img_main_nearby_selected,
				R.drawable.img_main_message_selected,
				R.drawable.img_main_discover_selected,
				R.drawable.img_main_personal_selected };
		mImgUnSelected = new ArrayList<Drawable>(PAGE_TOATAL);
		for (int i = 0; i < PAGE_TOATAL; i++) {
			Drawable drawable = getResources().getDrawable(imgId[i]);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			mImgUnSelected.add(drawable);
		}
		mImgSelected = new ArrayList<Drawable>(PAGE_TOATAL);
		for (int i = 4; i < imgId.length; i++) {
			Drawable drawable = getResources().getDrawable(imgId[i]);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			mImgSelected.add(drawable);
		}

		// 环信数据库初始化
		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);
		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewYBMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);
		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		// 注册一个透传消息的BroadcastReceiver
		IntentFilter cmdMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getCmdMessageBroadcastAction());
		cmdMessageIntentFilter.setPriority(3);
		registerReceiver(cmdMessageReceiver, cmdMessageIntentFilter);

		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(
				new MyContactListener());
		// 注册一个监听连接状态的listener
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());
		// 注册群聊相关的listener
		EMGroupManager.getInstance().addGroupChangeListener(
				new MyGroupChangeListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
		LogUtil.LogDebug("运行时",
				"MainActivity 环信初始化完成:" + System.currentTimeMillis(), null);
	}
	/**
	 * onWindowFocusChanged 此方法执行说明activity真正visible
	 * 此时弹出更新版本的popwindow，否则会发生异常
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && !isVersionChecked) {
				checkVersionUpdate();
		}
	}

	/** 检测有无版本更新 */
	private void checkVersionUpdate() {
		HttpRequest.getInstance().checkVersion(new CustomAsyncResponehandler() {
			@Override
			public void onSuccess(ResponeModel baseModel) {
				if (baseModel != null && baseModel.isStatus()) {
					final VersionUpdateModel vpm = (VersionUpdateModel) baseModel
							.getResultObject();
					// 有版本升级
					if (vpm != null
							&& CheckUtil.isUpdate(MainActivity.this,
									vpm.getVersionNum())) {
						final HintConfirmationPopWindow hintConfirmationPopWindow = new HintConfirmationPopWindow(
								MainActivity.this);
						hintConfirmationPopWindow.setTitle(vpm.getAppTitle());
						hintConfirmationPopWindow.setCenterText(vpm
								.getAppContent());
						hintConfirmationPopWindow
								.setSureButton("取消",R.drawable.gray_shape_frame,null)
								.setCancelButton("更新",R.drawable.blue_shape_frame, new OnClickListener() {
									@Override
									public void onClick(View v) {
										hintConfirmationPopWindow
												.getPopupWindow().dismiss();
										UpdateManager manager = new UpdateManager(
												MainActivity.this,vpm.getAppUrl());
										manager.checkUpdate();
									}
								}).showWindow();
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
			}

			@Override
			public void onFinish() {
				isVersionChecked = true;
			}
		});
	}

	@Override
	public void bindViews() {
		for (int i = 0; i < mTvLists.size(); i++) {
			mTvLists.get(i).setOnClickListener(mListener);
		}
	}

	private class MyListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_nearby:
				setTabSelection(0);
				break;
			case R.id.tv_message:
				setTabSelection(1);
				break;
			case R.id.tv_discover:
				setTabSelection(2);
				break;
			case R.id.tv_personal:
				setTabSelection(3);
				break;

			}
		}
	}

	/**
	 * 根据传入的index参数来设置选中的tab页。
	 * 
	 * @param index
	 *            每个tab页对应的下标。0:附近，1：消息，2：发现，3：我。
	 */
	private void setTabSelection(int index) {

		// 开启新的Fragment事务去处理
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		if (lastIndex == index || index > PAGE_TOATAL - 1) {
			return;
		}
		if (lastIndex < index) {
			transaction.setCustomAnimations(R.anim.scale_in_left,
					R.anim.opt_zoom_out);
		} else if (lastIndex > index) {
			transaction.setCustomAnimations(R.anim.scale_in_right,
					R.anim.opt_zoom_out);
		}
		if (lastIndex == 0)
			((NearbyMainFragment) mFragments.get(lastIndex))
					.onCancelLuckyMeet();
		hideFragments(transaction);
		lastIndex = index;
		mTvLists.get(index).setTextColor(
				getResColor(R.color.tv_bottom_selected));
		mTvLists.get(index).setCompoundDrawables(null, mImgSelected.get(index),
				null, null);
		transaction.show(mFragments.get(index));
		transaction.commit();
		if (index == 0)
			((NearbyMainFragment) mFragments.get(index)).onLuckyMeet();
	}

	/**
	 * 将所有的Fragment都还原且置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		for (int i = 0; i < mFragments.size(); i++) {
			mTvLists.get(i).setTextColor(
					getResColor(R.color.tv_bottom_unselected));
			mTvLists.get(i).setCompoundDrawables(null, mImgUnSelected.get(i),
					null, null);
			transaction.hide(mFragments.get(i));
			mFragments.get(i).onDestroyView();
		}
	}

	/**************************************** 消息接受者 *****************************/
	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewYBMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 注销广播接收者，否则在ChatActivity中会收到这个广播
			abortBroadcast();
			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看
			// EMChatManager.getInstance().activityResumed();

			String from = intent.getStringExtra("from");
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);

			// 刷新bottom bar消息未读数
			updateUnreadLabel();
			if (mFragments.get(lastIndex) == MessageMainFragment.getInstance()) {
				// 当前页面如果为聊天历史页面，刷新此页面
				if (MessageMainFragment.getInstance() != null) {
					MessageMainFragment.getInstance().refresh();
				}
			} else {
				notifyNewMessage(message);
			}

			// 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
			if (ChatActivity.activityInstance != null) {
				if (message.getChatType() == ChatType.GroupChat) {
					if (message.getTo().equals(
							ChatActivity.activityInstance.getToChatUsername()))
						return;
				} else {
					if (from.equals(ChatActivity.activityInstance
							.getToChatUsername()))
						return;
				}
			}

		}
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();

			System.out.println("已读回执6");
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");

			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);

				if (msg != null) {

					// 2014-11-5 修复在某些机器上，在聊天页面对方发送已读回执时不立即显示已读的bug
					if (ChatActivity.activityInstance != null) {
						if (msg.getChatType() == ChatType.Chat) {
							if (from.equals(ChatActivity.activityInstance
									.getToChatUsername()))
								return;
						}
					}
					msg.isAcked = true;
				}
			}

		}
	};

	/*
	 * 好友变化listener
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = appContext.getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = setUserHead(username);
				// 暂时有个bug，添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 刷新ui

			// TODO
			// if (currentTabIndex == 1)
			// contactListFragment.refresh();

			if (mFragments.get(lastIndex) == MessageMainFragment.getInstance()) {
				// 当前页面如果为聊天历史页面，刷新此页面
				if (MessageMainFragment.getInstance() != null) {
					MessageMainFragment.getInstance().refresh();
				}
			}

		}

		@Override
		public void onContactDeleted(final List<String> usernameList) {
			// 被删除
			Map<String, User> localUsers = appContext.getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// 如果正在与此用户的聊天页面
					if (ChatActivity.activityInstance != null
							&& usernameList
									.contains(ChatActivity.activityInstance
											.getToChatUsername())) {
						Toast.makeText(MainActivity.this, "已从好友列表移除此联系人", 1)
								.show();
						// ChatActivity.activityInstance.finish();
					}

					// TODO
					updateUnreadLabel();
				}
			});

			// TODO
			// 刷新ui
			// if (currentTabIndex == 1)
			// contactListFragment.refresh();

			if (mFragments.get(lastIndex) == MessageMainFragment.getInstance()) {
				// 当前页面如果为聊天历史页面，刷新此页面
				if (MessageMainFragment.getInstance() != null) {
					MessageMainFragment.getInstance().refresh();
				}
			}
		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null
						&& inviteMessage.getFrom().equals(username)) {
					inviteMessgeDao.deleteMessage(username);
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			LogUtil.LogDebug("MainActvity", username + "请求加你为好友,reason: "
					+ reason, true);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			LogUtil.LogDebug("MainActivity", username + "同意了你的好友请求", true);
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现
			LogUtil.LogDebug(username, username + "拒绝了你的好友请求", true);
		}

	}

	/**
	 * set head
	 * 
	 * @param username
	 * @return
	 */
	User setUserHead(String username) {
		User user = new User();
		user.setUsername(username);
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(GlobleConstant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
		return user;
	}

	/**
	 * 保存提示新消息
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		// EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

		// 刷新bottom bar消息未读数
		updateUnReadLableInPersonal();
		// 刷新好友页面ui
		// TODO
		// if (currentTabIndex == 1)
		// contactListFragment.refresh();

	}

	/**
	 * 保存邀请等msg
	 * 
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {

		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = appContext.getContactList().get(
				GlobleConstant.NEW_FRIENDS_USERNAME);
		if (user.getUnreadMsgCount() == 0)
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 个人中心未读消息
	 */
	public void updateUnReadLableInPersonal() {
		User user = appContext.getContactList().get(
				GlobleConstant.NEW_FRIENDS_USERNAME);
		int count = user.getUnreadMsgCount();
		if (count > 0) {
			unreadLabelSys.setText(String.valueOf(count));
			unreadLabelSys.setVisibility(View.VISIBLE);
		} else {
			unreadLabelSys.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 获取未读申请与通知消息
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (appContext.getContactList()
				.get(GlobleConstant.NEW_FRIENDS_USERNAME) != null)
			unreadAddressCountTotal = appContext.getContactList()
					.get(GlobleConstant.NEW_FRIENDS_USERNAME)
					.getUnreadMsgCount();
		return unreadAddressCountTotal;
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	/**
	 * 连接监听listener
	 * 
	 */
	private class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO
					// chatHistoryFragment.errorItem.setVisibility(View.GONE);
				}

			});
		}

		@Override
		public void onDisconnected(final int error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登陆dialog
						showConflictDialog();
					} else {
						// TODO
						// chatHistoryFragment.errorItem
						// .setVisibility(View.VISIBLE);
						// if (NetUtils.hasNetwork(MainActivity.this))
						// chatHistoryFragment.errorText.setText("连接不到聊天服务器");
						// else
						// chatHistoryFragment.errorText
						// .setText("当前网络不可用，请检查网络设置");

					}
				}

			});
		}
	}

	private android.app.AlertDialog.Builder conflictBuilder;
	private boolean isConflictDialogShow;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		appContext.logout();

		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(
							MainActivity.this);
				conflictBuilder.setTitle("下线通知");
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								conflictBuilder = null;
								finish();
								startActivity(new Intent(MainActivity.this,
										LoginActivity.class));
							}
						});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				Log.e("###",
						"---------color conflictBuilder error" + e.getMessage());
			}

		}

	}

	/**
	 * 群组邀请
	 */
	private class MyGroupChangeListener implements GroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName,
				String inviter, String reason) {
			boolean hasGroup = false;
			for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
				if (group.getGroupId().equals(groupId)) {
					hasGroup = true;
					break;
				}
			}
			if (!hasGroup)
				return;

			// 被邀请
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(inviter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(appContext.getUserInfo().getUserName() + "邀请你加入了群聊"));
			// 保存邀请消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO
					updateUnreadLabel();
					// // 刷新ui
					// if (currentTabIndex == 0)
					// chatHistoryFragment.refresh();
					// if (CommonUtils.getTopActivity(MainActivity.this).equals(
					// GroupsActivity.class.getName())) {
					// GroupsActivity.instance.onResume();
					// }

					if (mFragments.get(lastIndex) == MessageMainFragment
							.getInstance()) {
						// 当前页面如果为聊天历史页面，刷新此页面
						if (MessageMainFragment.getInstance() != null) {
							MessageMainFragment.getInstance().refresh();
						}
					}
				}
			});

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter,
				String reason) {

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee,
				String reason) {

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {
			// 提示用户被T了，demo省略此步骤
			// 刷新ui
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						// TODO
						updateUnreadLabel();
						// if (currentTabIndex == 0)
						// chatHistoryFragment.refresh();
						// if (CommonUtils.getTopActivity(MainActivity.this)
						// .equals(GroupsActivity.class.getName())) {
						// GroupsActivity.instance.onResume();
						// }
					} catch (Exception e) {
						Log.e("###", "refresh exception " + e.getMessage());
					}

				}
			});
		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {
			// 群被解散
			// 提示用户群被解散,demo省略
			// 刷新ui
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO
					updateUnreadLabel();
					// if (currentTabIndex == 0)
					// chatHistoryFragment.refresh();
					// if (CommonUtils.getTopActivity(MainActivity.this).equals(
					// GroupsActivity.class.getName())) {
					// GroupsActivity.instance.onResume();
					// }
				}
			});

		}

		@Override
		public void onApplicationReceived(String groupId, String groupName,
				String applyer, String reason) {
			// 用户申请加入群聊
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			LogUtil.LogDebug("MainActivity", applyer + " 申请加入群聊：" + groupName,
					true);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);
		}

		@Override
		public void onApplicationAccept(String groupId, String groupName,
				String accepter) {
			// 加群申请被同意
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(accepter + "同意了你的群聊申请"));
			// 保存同意消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO
					updateUnreadLabel();
					// // 刷新ui
					// if (currentTabIndex == 0)
					// chatHistoryFragment.refresh();
					// if (CommonUtils.getTopActivity(MainActivity.this).equals(
					// GroupsActivity.class.getName())) {
					// GroupsActivity.instance.onResume();
					// }
				}
			});
		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName,
				String decliner, String reason) {
			// 加群申请被拒绝，demo未实现
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播接收者
		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(ackMessageReceiver);
		} catch (Exception e) {
		}
		// try {
		// unregisterReceiver(offlineMessageReceiver);
		// } catch (Exception e) {
		// }

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}

		lastIndex = 0;

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (outState != null)
			outState.putInt("CURRTEN_INDEX", lastIndex);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			lastIndex = savedInstanceState.getInt("CURRTEN_INDEX", 0);
			setTabSelection(lastIndex);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		PushService.onResume("main", this);

		if (!isConflict) {
			updateUnreadLabel();
			// EMChatManager.getInstance().activityResumed();
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		PushService.onPause("main", this);
	}

	/**
	 * 透传消息BroadcastReceiver
	 */
	private BroadcastReceiver cmdMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();
			EMLog.d("MainActivity", "收到透传消息");
			// 获取cmd message对象
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = intent.getParcelableExtra("message");
			// 获取消息body
			CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
			String action = cmdMsgBody.action;// 获取自定义action

			// 获取扩展属性 此处省略
			// message.getStringAttribute("");
			// EMLog.d("MainActivity",
			// String.format("透传消息：action:%s,message:%s", action,
			// message.toString()));
			// Toast.makeText(MainActivity.this, "收到透传：action：" + action,
			// Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下 如果不需要，注释掉即可
	 * 
	 * @param message
	 */
	protected void notifyNewMessage(EMMessage message) {
		// 如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
		// 以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
		if (!EasyUtils.isAppRunningForeground(this)) {
			return;
		}

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(getApplicationInfo().icon)
				.setWhen(System.currentTimeMillis()).setAutoCancel(true);

		String ticker = CommonUtils.getMessageDigest(message, this);
		if (message.getType() == Type.TXT)
			ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
		// 设置状态栏提示
		String userName = "未知姓名";
		try {
			userName = message.getStringAttribute("nickname");
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mBuilder.setTicker(userName + ": " + ticker);

		Notification notification = mBuilder.build();
		notificationManager.notify(notifiId, notification);
		notificationManager.cancel(notifiId);
	}

}
