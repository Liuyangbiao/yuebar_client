package com.fullteem.yueba.entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKGeocoderAddressComponent;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.HanziToPinyin;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.app.ui.CreateGroupActivity;
import com.fullteem.yueba.app.ui.MainActivity;
import com.fullteem.yueba.db.AllCityDao;
import com.fullteem.yueba.db.UserDao;
import com.fullteem.yueba.engine.push.PushService;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.City;
import com.fullteem.yueba.model.LoginModel;
import com.fullteem.yueba.model.User;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.net.md5.MD5Util;
import com.fullteem.yueba.service.LocationManager;
import com.fullteem.yueba.service.NetworkManager;
import com.fullteem.yueba.service.ServerHostManager;
import com.fullteem.yueba.service.ServerHostManager.IHostFetchListener;
import com.fullteem.yueba.util.Base64Utils;
import com.fullteem.yueba.util.CheckUtil;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SharePreferenceUtil;
import com.fullteem.yueba.util.UmengUtil;

/**
 * @author ssy 登陆页面，本页面主要完成登陆逻辑
 */
public class LoginActivity extends BaseActivity implements OnClickListener,
		IHostFetchListener {
	private CheckBox checkBoxRemeberPsd;
	private Button btnLogin;
	private TextView btnRegister;
	private TextView btnForgetPsd;
	private Button btnQQ, btnSina, btnWeixin;
	
	//百度地图
	private BMapManager mapManager;
	private MKSearch mkSearch;
	private double latitude;
	private double longtitude;
	private String cityName;
	private Integer cityId;

	/**
	 * 登陆账号密码
	 */
	private EditText edtUserName, edtPsd;
	private boolean progressShow;

	// whether to login after finished getting host
	private boolean delayLogin;

	public LoginActivity() {
		super(R.layout.activity_login_main);

		LogUtil.printUserEntryLog("enter LoginActivity");
		// get host and location info
		//
		LocationManager.getInstance().getLocation();
		NetworkManager.checkNetwork();
	}

	@Override
	public void initViews() {
		
		ServerHostManager.getInstance(this).sendServerHostRequest(this);

		boolean isOpenLoginActivity = SharePreferenceUtil.getInstance(this)
				.getBooleanFromShare("open", false);
		if (!isOpenLoginActivity) {
			startActivity(new Intent(this, GuideActivity.class));
			finish();
		}

		btnLogin = (Button) findViewById(R.id.loginBtn);
		checkBoxRemeberPsd = (CheckBox) findViewById(R.id.loginCheckBox);
		btnRegister = (TextView) findViewById(R.id.register);
		btnForgetPsd = (TextView) findViewById(R.id.forgetpsd);
		btnQQ = (Button) findViewById(R.id.btnQQ);
		btnSina = (Button) findViewById(R.id.btnSina);
		btnWeixin = (Button) findViewById(R.id.btnWeixin);
		edtUserName = (EditText) findViewById(R.id.edtUsraccount);
		edtPsd = (EditText) findViewById(R.id.edtPsd);
		
		//初始化百度地图API
		mapManager = new BMapManager(getApplication());
		mapManager.init("3AB1810EBAAE0175EB41A744CF3B2D6497407B87", new MyGeneralListener());
		mkSearch = new MKSearch();
		mkSearch.init(mapManager, new MySearchListener());
		latitude = appContext.getLocation().getLatitude() * 1e6;
		longtitude = appContext.getLocation().getLongitude() * 1e6;
		
	}
	
	/**
	 * 界面监听
	 * 
	 * @author Administrator
	 * 
	 */
	class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(LoginActivity.this, "您的网络出错啦！",
						Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(LoginActivity.this, "输入正确的检索条件！",
						Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Log.e("map", "permissio denied. check your app key");
			}
		}

	}
	
	/**
	 * 实现MKSearchListener接口,用于实现异步搜索服务，得到搜索结果
	 */
	public class MySearchListener implements MKSearchListener {
		/**
		 * 根据经纬度搜索地址信息结果
		 * 
		 * @param result
		 *            搜索结果
		 * @param iError
		 *            错误号（0表示正确返回）
		 */
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			MKGeocoderAddressComponent kk=result.addressComponents; 
			cityName=kk.city;
			if(null == cityName){
				return;
			}
			City city = AllCityDao.getCityByName(cityName);
	        if(null != city){
	        	cityId = city.getCID();
//	        	String cityid = SharePreferenceUtil.getInstance(AppContext.getApplication()).getSettingCityId();
//	        	if(cityId+"" == cityid || "11" == cityid){
	        		SharePreferenceUtil.getInstance(AppContext.getApplication()).setSettingCityId(cityId+"");
		        	SharePreferenceUtil.getInstance(AppContext.getApplication()).setSettingCityName(cityName);
//	        	}
	        }
		}

		/**
		 * 驾车路线搜索结果
		 * 
		 * @param result
		 *            搜索结果
		 * @param iError
		 *            错误号（0表示正确返回）
		 */
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
		}

		/**
		 * POI搜索结果（范围检索、城市POI检索、周边检索）
		 * 
		 * @param result
		 *            搜索结果
		 * @param type
		 *            返回结果类型（11,12,21:poi列表 7:城市列表）
		 * @param iError
		 *            错误号（0表示正确返回）
		 */
		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
		}

		/**
		 * 公交换乘路线搜索结果
		 * 
		 * @param result
		 *            搜索结果
		 * @param iError
		 *            错误号（0表示正确返回）
		 */
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result,
				int iError) {
		}

		/**
		 * 步行路线搜索结果
		 * 
		 * @param result
		 *            搜索结果
		 * @param iError
		 *            错误号（0表示正确返回）
		 */
		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int iError) {
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}
	}

	@Override
	public void initData() {
	}

	@Override
	public void bindViews() {
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		btnForgetPsd.setOnClickListener(this);
		btnQQ.setOnClickListener(this);
		btnSina.setOnClickListener(this);
		btnWeixin.setOnClickListener(this);

		// 若上一次登陆成功且记住过密码
		if (!TextUtils.isEmpty(appContext.getLocalUserName())
				&& !TextUtils.isEmpty(appContext.getLocalPsd())) {
			edtUserName.setText(appContext.getLocalUserName());
			// edtPsd.setText(EncryptUtil.decrypt(appContext.getLocalPsd()));
			edtPsd.setText(Base64Utils.decode(appContext.getLocalPsd()));
		}
	}

	@Override
	public void onHostFetched(boolean result) {
		if (delayLogin) {
			LogUtil.printUserEntryLog("start login after fetched host");
			startLogin();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			if (ServerHostManager.getInstance(this).isHostReady()) {
				delayLogin = false;
				startLogin();
			} else {
				LogUtil.printUserEntryLog("delay login");
				delayLogin = true;
			}

			break;
		case R.id.register:
			Intent registerIntent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(registerIntent);
			break;
		case R.id.forgetpsd:
			Intent forgetIntent = new Intent(LoginActivity.this,
					ForgetActivity.class);
			startActivity(forgetIntent);
			break;
		case R.id.btnQQ:
			break;
		case R.id.btnSina:
			break;
		case R.id.btnWeixin:
			break;

		default:
			break;
		}

	}

	/**
	 * 开始登录流程
	 */
	private void startLogin() {
		LogUtil.printUserEntryLog("start login process");
		final String username = edtUserName.getText().toString();
		final String password = edtPsd.getText().toString();

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			if (!CheckUtil.checkUserName(username)) {
				showToast(getString(R.string.string_please_input_right_user));
				return;
			}
			if (!Urls.TEST_MODE)
				if (!CheckUtil.checkUserPassword(password)) {
					showToast(getString(R.string.string_please_input_right_psd));
					return;
				}

			final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
			pd.setCanceledOnTouchOutside(false);
			pd.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					progressShow = false;
				}
			});

			doSelfLogin(username, password, pd);
		} else {
			if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
				showToast(getString(R.string.string_please_inputuserpsd));
				return;
			}
			if (TextUtils.isEmpty(username)) {
				showToast(getString(R.string.string_please_inputuser));
				return;
			}
			if (TextUtils.isEmpty(password)) {
				showToast(getString(R.string.string_please_inputpsd));
				return;
			}
		}
	}

	/**
	 * 做本地服务器登陆
	 */
	private void doSelfLogin(final String username, final String password,
			final ProgressDialog pd) {
        
        mkSearch.reverseGeocode(new GeoPoint((int)latitude, (int)longtitude));//调用后可获取位置相关信息
        
		LogUtil.printUserEntryLog("start self login");
		pd.setMessage("正在登陆...");
		pd.show();
		progressShow = true;

		JSONObject ob = new JSONObject();
		if (!Urls.TEST_MODE)
			ob.put("userMobileOrAccount", username);
		else
			ob.put("userMobile", username);
		ob.put("userPassword", MD5Util.string2MD5(password));

		// if location has not been fetched, will use default location instead.
		ob.put("lng", appContext.getLocation().getLongitude());
		ob.put("lat", appContext.getLocation().getLatitude());

		HttpRequestFactory.getInstance().doHttpsPostRequest(Urls.login_action, ob,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showLoadingDialog();
							}
						});
					}

					@Override
					public void onSuccess(String content) {
						LogUtil.printUserEntryLog("self login succeed");

						super.onSuccess(content);

						LoginModel lm = new LoginModel();
						lm = JsonUtil.convertJsonToObject(content,
								LoginModel.class);
						if (lm == null) {
							try {
								org.json.JSONObject json = new org.json.JSONObject(
										content);
								String code = json.getString("code");
								// 登陆密码不正确，提示
								if (code != null
										&& "1002".equalsIgnoreCase(code)) {
									dismissPd(pd);
									showToastInThread(getString(R.string.string_please_input_right_psd));
								} else if (code != null
										&& "1001".equalsIgnoreCase(code)) {
									// 用户名错误提示
									dismissPd(pd);
									showToastInThread(getString(R.string.string_please_input_right_user));
								} else {
									showToastInThread(getString(R.string.login_fail));
									dismissLoadingDialog();
									pd.dismiss();
								}
								return;
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						if (lm != null
								&& lm.getCode() != null
								&& GlobleConstant.REQUEST_SUCCESS
										.equalsIgnoreCase(lm.getCode())) {
							LogUtil.printUserEntryLog("code is right");
							appContext.setUserInfo(lm.getResult());
							// 切换用户清空之前保存的用户名密码
							if (!username.equalsIgnoreCase(appContext
									.getLocalUserName())) {
								appContext.clearData();
							}
							// 登陆成功，保存用户名密码
							if (checkBoxRemeberPsd.isChecked()) {
								appContext.setLocalPsd(Base64Utils
										.encode(password));
								appContext.setLocalUserName(username);
							} else {
								appContext.clearData();
							}

							// 服务器端登录
							if (!Urls.TEST_MODE)
								doIMServerLogin(appContext.getUserInfo()
										.getImServerId(), appContext
										.getUserInfo().getImServerPwd(), pd);
							else
								doIMServerLogin(username,
										MD5Util.string2MD5(password), pd);
							// 登陆成功后登陆按钮不能点击
							btnLogin.setClickable(false);
						} else {
							LogUtil.printUserEntryLog("self login succeed but code is not right");

							dismissPd(pd);
							showToastInThread(getString(R.string.login_fail));
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.printUserEntryLog("self login failed");
						super.onFailure(error, content);

						dismissPd(pd);

						ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
						boolean wifi = con.getNetworkInfo(
								ConnectivityManager.TYPE_WIFI)
								.isConnectedOrConnecting();
						boolean internet = con.getNetworkInfo(
								ConnectivityManager.TYPE_MOBILE)
								.isConnectedOrConnecting();

						if (wifi | internet) {
							// 执行相关操作 ，说明不是网络状态错误，提示登录错误
							showToastInThread(getString(R.string.login_fail));
						} else {
							showToastInThread(getString(R.string.net_not_found));
						}
					}

					@Override
					public void onFinish() {
						dismissLoadingDialog();
						LogUtil.printUserEntryLog("doSelfLogin finished");

					}
				});
	}

	/**
	 * 登陆环信流程
	 */
	private void doIMServerLogin(final String username, final String password,
			final ProgressDialog pd) {
		LogUtil.printUserEntryLog("start IM server login");

		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(username, password, new EMCallBack() {

			@Override
			public void onSuccess() {
				LogUtil.printUserEntryLog("IM server login succeed");
				if (!progressShow) {
					return;
				}

				Hashtable<String, EMConversation> conversations = EMChatManager
						.getInstance().getAllConversations();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						pd.setMessage("正在获取好友和群聊列表...");
					}
				});
				try {
					handleIMServerLoginLogin();
				} catch (Exception e) {
					LogUtil.printUserEntryLog("获取过程中出现问题:" + e);
				}

				dismissPd(pd);

				finishLogin();
			}

			@Override
			public void onError(int arg0, String arg1) {
				LogUtil.printUserEntryLog(arg1);
				dismissPd(pd);

				Message msg = new Message();
				msg.obj = arg1;
				showMsgHandler.sendMessage(msg);
				dismissLoadingDialog();
			}

			@Override
			public void onProgress(int arg0, String arg1) {
				LogUtil.printUserEntryLog(arg1);
				dismissPd(pd);

				Message msg = new Message();
				msg.obj = arg1;
				showMsgHandler.sendMessage(msg);
				dismissLoadingDialog();
			}

		});

	}

	private void dismissPd(final ProgressDialog pd) {
		if (pd != null && !LoginActivity.this.isFinishing()) {
			pd.dismiss();
			progressShow = false;
		}
	}

	private void handleIMServerLoginLogin() throws EaseMobException {
		LogUtil.printUserEntryLog("handle IM server login");

		// ** 第一次登录或者之前logout后，加载所有本地群和回话
		// ** manually load all local groups and
		// conversations in case we are auto login
		EMGroupManager.getInstance().loadAllGroups();
		EMChatManager.getInstance().loadAllConversations();

		// 每次登陆都去获取好友username
		List<String> usernames = EMContactManager.getInstance()
				.getContactUserNames();
		LogUtil.printUserEntryLog("contacts size: " + usernames.size());
		Map<String, User> userlist = new HashMap<String, User>();
		for (String username : usernames) {
			User user = new User();
			user.setUsername(username);
			setUserHearder(username, user);
			userlist.put(username, user);
		}
		// 添加user"申请与通知"
		User newFriends = new User();
		newFriends.setUsername(GlobleConstant.NEW_FRIENDS_USERNAME);
		newFriends.setNick("申请与通知");
		newFriends.setHeader("");
		userlist.put(GlobleConstant.NEW_FRIENDS_USERNAME, newFriends);
		// 添加"群聊"
		User groupUser = new User();
		groupUser.setUsername(GlobleConstant.GROUP_USERNAME);
		groupUser.setNick("群聊");
		groupUser.setHeader("");
		userlist.put(GlobleConstant.GROUP_USERNAME, groupUser);

		// 存入内存
		appContext.setContactList(userlist);
		// 存入db
		UserDao dao = new UserDao(LoginActivity.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);

		// 获取群聊列表(群聊里只有groupid和groupname的简单信息),sdk会把群组存入到内存和db中
		EMGroupManager.getInstance().getGroupsFromServer();

		boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
				AppContext.currentUserNick);
		if (!updatenick) {
			LogUtil.printUserEntryLog("update current user nick fail");
		}
	}

	private void finishLogin() {
		LogUtil.printUserEntryLog("finish login");
		showToastInThread(getString(R.string.login_success));

		// 进入主页面
		startActivity(new Intent(LoginActivity.this, MainActivity.class));

		finish();
		dismissLoadingDialog();
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, User user) {
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
	}

	Handler showMsgHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			showToast(msg.obj.toString());
		};
	};

	@Override
	public void onResume() {
		super.onResume();
		PushService.onResume("login", this);
		UmengUtil.onLoginPageStart(this,"SplashScreen");
	}

	@Override
	public void onPause() {
		super.onPause();
		PushService.onPause("login", this);
		UmengUtil.onLoginPageEnd(this,"SplashScreen");
	}

}
