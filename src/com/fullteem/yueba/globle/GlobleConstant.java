package com.fullteem.yueba.globle;

import java.io.File;

import android.os.Environment;

public class GlobleConstant {
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";

	/** 广播事件,改变配置 */
	public static final String UPDATE_EMCHAT_NOTIFY = "com.fullteem.yueba.UPDATE_EMCHAT_NOTIFY";

	/**
	 * 自定义http工具类
	 */
	public static final int TIMEOUT = 30000;

	public static final int POSTJSON = 810;

	public static final int POSTSTRING = 820;

	public static final int UPLOAD = 830;

	public static final int POSTNORMALJSON = 840;

	public static final int GET = 850;

	/**
	 * 对话框右侧栏宽度
	 */
	public static final int RIGHT_LENGTH = 200;

	/**
	 * 短信专用APPKEY和APPSECRET
	 */
	/**
	 * 填写从短信SDK应用后台注册得到的APPKEY
	 */
	public static final String APPKEY = "47d4d6372062";

	/**
	 * 填写从短信SDK应用后台注册得到的APPSECRET
	 */
	public static final String APPSECRET = "59b938bfa67c8985e7ab68ef1cad072b";

	/**
	 * 用户基础信息
	 */
	public static final String SHARED_KEY_USERINFO = "share_key_userinfo";
	
	/**
	 * 开关
	 */
	public static final String SHARED_KEY_SERVER_HOST = "share_key_server_host";

	/********************************************************* 返回码 ************************************************************/

	/**
	 * 服务器返回成功
	 */
	public static String REQUEST_SUCCESS = "200";

	/**
	 * 1007
	 */
	public static String GROUP_IS_EXIST = "1007";

	/**
	 * 服务器返回失败
	 */
	public static int REQUEST_FAILED = 500;

	/******************************************************** 路径存放类 **********************************************************/

	/** 美天在内存卡文件夹 */
	public static final String SDCARD = Environment
			.getExternalStorageDirectory() + File.separator + "yuebar";

	public static String USERPHOTO_DIR = SDCARD + File.separator + "photo"
			+ File.separator;

	/**
	 * 图片存放位置
	 */
	public static String IMAGE_RES = SDCARD + File.separator + "imgs"
			+ File.separator;

	/**
	 * 报错文件存放目录
	 */
	public static String CRASH_LOG = SDCARD + File.separator + "crashlog"
			+ File.separator;

	/**
	 * 用户头像
	 */
	public static String USERPHOTO_NAME = "userPhoto.jpg";

	/******************************************************** Intent Name **********************************************************/

	/** 酒吧id */
	public static final String PUB_ID = "pub_id";

	/** 酒吧名称 */
	public static final String PUB_NAME = "pub_name";
	
	/** 酒吧能否支付 */
	public static final String ENABLE_PAY = "enable_pay";

	/** 歌手id */
	public static final String SINGER_ID = "singer_id";

	/** 歌手姓名 */
	public static final String SINGER_NAME = "singer_name";

	/** 视频id */
	public static final String VIDEO_ID = "video_id";

	/** 约会bean */
	public static final String DATE_MODEL = "date_model";

	/** 是否从送礼跳到礼物仓 */
	public static final String GIFT_GIVE = "gift_give";

	/** 发起单人约会，心仪对象昵称 */
	public static final String DATE_FAVORITE_NAME = "favorite_name";
	/** 发起单人约会，心仪对象ID */
	public static final String DATE_FAVORITE_ID = "favorite_id";
	/** 发起单人约会，心仪对象头像 */
	public static final String DATE_FAVORITE_URL = "favorite_url";
	/** 发起单人约会，心仪对象号码 */
	public static final String DATE_FAVORITE_MOBILE = "favorite_mobile";

	/** 发起单人约会，心仪对象号码 */
	public static final String MESSAGE_TYPE = "system_message";

	/******************************************************** Intent Name **********************************************************/

	/************************************************* SharePreference key **********************************************************/

	/* '*_FILTER_DEFAULT' means 全部或不限 */
	/** 歌手弹窗记录选择项 只看女生 只看男生 全部 */
	public static String SINGER_FILTER = "singerFilter";
	public static int SINGER_FILTER_DEFAULT = 2;

	/** 吧友弹窗记录选择项 只看女生 只看男生 全部 */
	public static String BARFRIENDS_FILTER = "barFriendsFilter";
	public static int BARFRIENDS_FILTER_DEFAULT = 2;

	/** 酒吧弹窗记录选择项 酒吧 迪吧吧 演艺吧 不限 */
	public static String PUB_FILTER = "pubFilter";
	public static int PUB_FILTER_DEFAULT = 3;

	/** 约会弹窗记录选择项 城市 只看女生 只看男生 不限 */
	public static String DATE_FILTER = "dateFilter";
	public static int DATE_FILTER_DEFAULT = 3;

	/************************************************* SharePreference key **********************************************************/

	/** 热门城市 */
	public static final String HOTCITYS[] = new String[] { "北京", "上海", "广州",
			"天津", "深圳", "武汉", "长沙", "东莞", "西安", "重庆", "佛山", "南京", "郑州", "惠州",
			"杭州", "济南", "汕头", "福州", "合肥", "江门", "南宁", "南昌", "厦门", "成都", "昆明",
			"青岛", "沈阳", "太原", "大连", "哈尔滨", "长春", "苏州", "丽江" };

	/** 酒吧详情页面是否需要显示设为约会地点 */
	public static final String ACTION_ADDRESS = "action_address";
	public static final int ACTION_ADDRESS_CODE = 100;
	/** 发布约会返回的订单id */
	public static final int ACTION_ORDER_CODE = 110;

	/** 吧友页面点击地点id */
	public static final int ACTION_HOT_CITY = 120;

	/**
	 * 升级文件名
	 */
	public static String APKName = "Yuebar";

	// /**来消息震动提示*/
	// public static final String NOTICED_VIBRATE = "NoticedByVibrate";
	// /**来消息声音提示*/
	// public static final String NOTICE_SOUND = "NoticeBySound";

}
