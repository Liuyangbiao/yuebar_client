package com.fullteem.yueba.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.exceptions.EaseMobException;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.ChatActivity;
import com.fullteem.yueba.app.ui.MainActivity;
import com.fullteem.yueba.db.AllCityDao;
import com.fullteem.yueba.db.DbOpenHelper;
import com.fullteem.yueba.db.HotCityDao;
import com.fullteem.yueba.db.UserDao;
import com.fullteem.yueba.engine.push.PushService;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.City;
import com.fullteem.yueba.model.PayContextModel;
import com.fullteem.yueba.model.User;
import com.fullteem.yueba.model.UserCommonModel;
import com.fullteem.yueba.receiver.VoiceCallReceiver;
import com.fullteem.yueba.service.LocationManager;
import com.fullteem.yueba.service.ServerHostModel;
import com.fullteem.yueba.service.ServerHostModel.ServerHostDataModel;
import com.fullteem.yueba.util.AppUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SharePreferenceUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.UIHelper;
import com.fullteem.yueba.util.VideoPlayerHelper;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * 
 * @author bin
 * @version 1.0.0
 * @created 2014-2-24
 */
public class AppContext extends Application {
	private boolean isLog = true;
	private String TAG = "AppContext";
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	public static final int PAGE_SIZE = 20;// 默认分页大小
	public static final int YES = 1;
	public static final int NOT = 2;

	/** 视频播放，唯一实例 */
	public final static VideoPlayerHelper mp = new VideoPlayerHelper();

	public static Context applicationContext;
	// login user name
	public final String PREF_LOCALNAME = "localname";
	private String localName = null;
	// login password
	private static final String PREF_PSD = "localpsd";
	private String localPsd = null;
	private Map<String, User> contactList;
	public static ArrayList<EventHandler> mListeners = new ArrayList<EventHandler>();
	private static final int CITY_LIST_SCUESS_HAND = 0;// 城市加载成功
	private static final int HOT_CITY_LIST_SCUESS_HAND = 1;// 热门城市加载成功
	private static final int NET_CHANGE_STATE_HAND = 2;// 网络状态改变
	public static boolean ISAllCITYGETED = false;// 是否获取了所有的城市
	public static boolean ISAllHOTCITYGETED = false;// 是否获取了所有的热门城市
	public static boolean ISNETCONNECTED = false;// 网络是否连接
	public static int CURRENTNETTYPE = 0;// 当前网络类型

	public static List<City> citiesList;// 所有的城市
	public static List<City> allhotCitiesList;// 所有的热门城市
	private static AppContext application;
	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";

	/**
	 * 用户基础信息
	 */
	private UserCommonModel userInfo;

	private BDLocation location;
	
	private boolean enrollDateClickable;
	private boolean successfulDateClickable;
	private boolean availableDateClickable;
	private boolean barClickable;

	/*
	 * 支付context信息
	 */
	private PayContextModel payContext;
	
	private ServerHostDataModel serverInfo;
	@Override
	public void onCreate() {
		super.onCreate();
		setEnrollDateClickable(true);
		setSuccessfulDateClickable(true);
		setAvailableDateClickable(true);
		setBarClickable(true);
		application = this;
		// 进程ID
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果使用到百度地图或者类似启动remote service的第三方库，这个if判断不能少
		if (processAppName == null || processAppName.equals("")) {
			// workaround for baidu location sdk
			// 百度定位sdk，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
			// 创建新的进程。
			// 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfo
			// processName，
			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}

		applicationContext = this;
		// 初始化环信SDK,一定要先调用init()
		EMChat.getInstance().init(applicationContext);
		EMChat.getInstance().setDebugMode(true);
		Log.d("EMChat Demo", "initialize EMChat SDK");
		// debugmode设为true后，就能看到sdk打印的log了

		// 获取到EMChatOptions对象
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// 设置自定义的文字提示
		options.setNotifyText(new OnMessageNotifyListener() {

			@Override
			public String onNewMessageNotify(EMMessage message) {
				// 可以根据message的类型提示不同文字，这里为一个简单的示例

				String userName = null;
				try {
					userName = message.getStringAttribute("nickname");
				} catch (EaseMobException e1) {
					e1.printStackTrace();
				}

				if (userName == null) {
					return "您有一个好友发来了一条消息~";
				} else {
					return "好友" + userName + "发来了一条消息~";
				}
			}

			@Override
			public String onLatestMessageNotify(EMMessage message,
					int fromUsersNum, int messageNum) {
				return fromUsersNum + "好友发来" + messageNum + "条消息";
			}

			@Override
			public String onSetNotificationTitle(EMMessage arg0) {
				return null;
			}

			@Override
			public int onSetSmallIcon(EMMessage arg0) {
				return 0;
			}
		});
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		// 默认环信是不维护好友关系列表的，如果app依赖环信的好友关系，把这个属性设置为true
		options.setUseRoster(true);
		// 设置收到消息是否有新消息通知(声音和震动提示)，默认为true
		// options.setNotifyBySoundAndVibrate(SharePreferenceUtil.getInstance(
		// applicationContext).getSettingMsgNotification());
		options.setNotifyBySoundAndVibrate(true);

		// 设置收到消息是否有声音提示，默认为true
		options.setNoticeBySound(SharePreferenceUtil.getInstance(
				applicationContext).getSettingMsgSound());
		// 设置收到消息是否震动 默认为true
		options.setNoticedByVibrate(SharePreferenceUtil.getInstance(
				applicationContext).getSettingMsgVibrate());
		// 设置语音消息播放是否设置为扬声器播放 默认为true
		options.setUseSpeaker(SharePreferenceUtil.getInstance(
				applicationContext).getSettingMsgSpeaker());

		// 提示音
		options.setNotifyRingUri(Uri.parse("android.resource://"
				+ getPackageName() + "/" + R.raw.dengdengdeng));

		// 设置notification消息点击时，跳转的intent为自定义的intent
		options.setOnNotificationClickListener(new OnNotificationClickListener() {

			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(applicationContext,
						MainActivity.class);
				ChatType chatType = message.getChatType();
				if (chatType == ChatType.Chat) { // 单聊信息
					intent.putExtra("userId", message.getFrom());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
				} else { // 群聊信息
							// message.getTo()为群聊id
					intent.putExtra("groupId", message.getTo());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				}
				return intent;
			}
		});

		EMChatManager.getInstance().setChatOptions(options);

		initCity();

		// 设置一个connectionlistener监听账户重复登陆
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());
		// 注册一个语言电话的广播接收者
		IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance()
				.getIncomingVoiceCallBroadcastAction());
		registerReceiver(new VoiceCallReceiver(), callFilter);

		// jpush initialize
		PushService.initPush(this);
	}

	public static AppContext getApplication() {
		return application;
	}

	/**
	 * 初始化城市
	 */
	private void initCity() {
		// TODO Auto-generated method stub
		citiesList = new ArrayList<City>();
		allhotCitiesList = new ArrayList<City>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ISAllCITYGETED = false;
				ISAllHOTCITYGETED = false;
				AllCityDao.initPre(application);
				citiesList = AllCityDao.getAllCity();
				LogUtil.LogDebug("cccccccccccccc", citiesList.size()
						+ "--------------", isLog);
				mHandler.sendEmptyMessage(CITY_LIST_SCUESS_HAND);
				if (HotCityDao.getInstance(application).checkIsNeedUpdate(
						application)) {
					allhotCitiesList = matchCity(GlobleConstant.HOTCITYS);
					mHandler.sendEmptyMessage(HOT_CITY_LIST_SCUESS_HAND);
					HotCityDao.getInstance(application).addHotCity(
							allhotCitiesList, application);
				} else {
					allhotCitiesList = HotCityDao.getInstance(application)
							.getAllHotCity();
					mHandler.sendEmptyMessage(HOT_CITY_LIST_SCUESS_HAND);
				}
			}
		}).start();
	}

	/**
	 * 根据热门城市名字从所有城市中获取热门城市详细信息
	 * 
	 * @return
	 */
	private List<City> matchCity(String[] cityName) {
		List<City> cities = new ArrayList<City>();
		for (int i = 0; i < cityName.length; i++) {
			for (City city : citiesList) {
				if (AppUtil.parceCityName(cityName[i]).equals(
						AppUtil.parceCityName(city.getCName()))) {
					cities.add(city);
					break;
				}
			}
		}
		return cities;
	}

	/**
	 * 检测当前系统声音是否为正常模式
	 * 
	 * @return
	 */
	public boolean isAudioNormal() {
		AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
	}

	/**
	 * 应用程序是否发出提示音
	 * 
	 * @return
	 */
	public boolean isAppSound() {
		return isAudioNormal();
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	@SuppressLint("DefaultLocale")
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!StringUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * 
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	/**
	 * 保存磁盘缓存
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void setDiskCache(String key, String value) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = openFileOutput("cache_" + key + ".data", Context.MODE_PRIVATE);
			fos.write(value.getBytes());
			fos.flush();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 获取磁盘缓存数据
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public String getDiskCache(String key) throws IOException {
		FileInputStream fis = null;
		try {
			fis = openFileInput("cache_" + key + ".data");
			byte[] datas = new byte[fis.available()];
			fis.read(datas);
			return new String(datas);
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		if (getUserInfo().getImServerId() != null && contactList == null) {
			UserDao dao = new UserDao(applicationContext);
			// 获取本地好友user list到内存,方便以后获取好友list
			contactList = dao.getContactList();
		}
		return contactList;
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		this.contactList = contactList;
	}

	public void setStrangerList(Map<String, User> List) {

	}

	/**
	 * 设置本地用户名
	 * 
	 * @param user
	 */
	public void setLocalUserName(String username) {
		if (username != null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString(PREF_LOCALNAME, username).commit()) {
				localName = username;
			}
		}
	}

	/**
	 * 获取当前登陆用户名
	 * 
	 * @return
	 */
	public String getLocalUserName() {
		if (localName == null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(applicationContext);
			localName = preferences.getString(PREF_LOCALNAME, null);
		}
		return localName;
	}

	/**
	 * 储存本地加密的密码
	 * 
	 * @param psd
	 */
	public void setLocalPsd(String psd) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putString(PREF_PSD, psd).commit()) {
			localPsd = psd;
		}
	}

	/**
	 *  获取本地加密的密码
	 * 
	 * @return
	 */
	public String getLocalPsd() {
		if (localPsd == null) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(applicationContext);
			localPsd = preferences.getString(PREF_PSD, null);
		}
		return localPsd;
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout() {
		// 先调用sdk logout，在清理app中自己的数据
		EMChatManager.getInstance().logout();
		DbOpenHelper.getInstance(applicationContext).closeDB();

		setContactList(null);

		PushService.stopPush();
	}

	/**
	 * 清空数据
	 */
	public void clearData() {
		setLocalUserName(null);
		setLocalPsd(null);
		setContactList(null);
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List<?> l = am.getRunningAppProcesses();
		Iterator<?> i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					Log.d("Process", "Id: " + info.pid + " ProcessName: "
							+ info.processName + "  Label: " + c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				Log.d("Process", "Error>> :" + e.toString());
			}
		}
		return processName;
	}

	class MyConnectionListener implements EMConnectionListener {
		@Override
		public void onDisconnected(int error) {
			if (error == EMError.CONNECTION_CONFLICT) {
				Intent intent = new Intent(applicationContext,
						MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("conflict", true);
				startActivity(intent);
			}

		}

		@Override
		public void onConnected() {
		}
	}

	/**
	 * 保存用户信息
	 * 
	 * @param user
	 */
	public void setUserInfo(UserCommonModel user) {
		SharePreferenceUtil.saveObjectToShare(getApplication(),
				GlobleConstant.SHARED_KEY_USERINFO, "", user);
		userInfo = user;
	}

	/**
	 * 获取用户基础信息
	 * 
	 * @return userInfo
	 */
	public UserCommonModel getUserInfo() {
		if (userInfo == null) {
			userInfo = SharePreferenceUtil.getObjectFromShare(getApplication(),
					GlobleConstant.SHARED_KEY_USERINFO, "");
		}
		return userInfo;
	}
	
	/**
	 * 保存server信息
	 * 
	 * @param user
	 */
	public void setServerHostData(ServerHostDataModel server) {
		SharePreferenceUtil.saveObjectToShare(getApplication(),
				GlobleConstant.SHARED_KEY_SERVER_HOST, "", server);
		serverInfo = server;
	}

	/**
	 * 获取server信息
	 * 
	 * @return userInfo
	 */
	public ServerHostDataModel getServerHostData() {
		if (serverInfo == null) {
			serverInfo = SharePreferenceUtil.getObjectFromShare(getApplication(),
					GlobleConstant.SHARED_KEY_SERVER_HOST, "");
		}
		return serverInfo;
	}

	/**
	 * 设置当前位置
	 * 
	 * @param location
	 */
	public void setLocation(BDLocation location) {
		this.location = location;
	}

	/**
	 * 获取当前位置
	 * 
	 * @return
	 */
	public BDLocation getLocation() {
		if (location == null) {
			LogUtil.printUserEntryLog("getLocation use default location");
			return LocationManager.getInstance().getDefaultLocation();
		}

		return location;
	}

	/**
	 * 上传在application中处理，保证可全局使用
	 */
	public void upLoadingFile(String path) {

	}

	public static abstract interface EventHandler {
		public abstract void onCityComplite();

		public abstract void onHotCityComplite();

		public abstract void onNetChange();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CITY_LIST_SCUESS_HAND:
				ISAllCITYGETED = true;
				if (mListeners.size() > 0)// 通知接口完成所有城市加载
					for (EventHandler handler : mListeners) {
						handler.onCityComplite();
						UIHelper.ShowMessage(application, "城市加载完成________");
					}
				break;
			case HOT_CITY_LIST_SCUESS_HAND:
				ISAllHOTCITYGETED = true;
				if (mListeners.size() > 0)// 通知接口完成热门城市加载
					for (EventHandler handler : mListeners) {
						handler.onHotCityComplite();
					}
				break;
			case NET_CHANGE_STATE_HAND:
				if (mListeners.size() > 0)// 通知网络状态发生改变
					for (EventHandler handler : mListeners) {
						if (handler != null)
							handler.onNetChange();
					}
			default:
				break;
			}
		}
	};

	/*
	 * 保存ali pay context
	 */
	public void setAliPayContext(PayContextModel payContext) {
		this.payContext = payContext;
	}

	public PayContextModel getAliPayContext() {
		return this.payContext;
	}
	
	
	public boolean isEnrollDateClickable() {
		return enrollDateClickable;
	}

	public void setEnrollDateClickable(boolean enrollDateClickable) {
		this.enrollDateClickable = enrollDateClickable;
	}

	public boolean isSuccessfulDateClickable() {
		return successfulDateClickable;
	}

	public void setSuccessfulDateClickable(boolean successfulDateClickable) {
		this.successfulDateClickable = successfulDateClickable;
	}

	public boolean isAvailableDateClickable() {
		return availableDateClickable;
	}

	public void setAvailableDateClickable(boolean availableDateClickable) {
		this.availableDateClickable = availableDateClickable;
	}

	public boolean isBarClickable() {
		return barClickable;
	}

	public void setBarClickable(boolean barClickable) {
		this.barClickable = barClickable;
	}

}
