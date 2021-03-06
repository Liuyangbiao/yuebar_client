package com.fullteem.yueba.net;

import android.text.TextUtils;

import com.fullteem.yueba.util.LogUtil;

/* 业务数据URL地址（注意标注地址注释）
 * 
 * @author allen
 * @version 1.0.0
 * @created 2014-7-10
 */
public class Urls {
	public static boolean TEST_MODE = false;
	
	public static boolean doHttpsRequest = true;

	// default Server Address
	private static final String DEFAULT_HTTPS_SERVER_HOST_URL = "https://mumubar.com";
	private static final String DEFAULT_SERVER_HOST_URL = "http://mumubar.com";
	private static final String DEFAULT_BASE_IMAGE_URL = "http://mumubar.com";
//	private static final String DEFAULT_HTTPS_SERVER_HOST_URL = "https://192.168.0.127:8443/bro";//dadi server https
//	private static final String DEFAULT_SERVER_HOST_URL = "http://192.168.0.127:8080/bro";//dadi server http
	//private static final String DEFAULT_HTTPS_SERVER_HOST_URL = "https://123.57.91.105";
	//private static final String DEFAULT_SERVER_HOST_URL = "http://123.57.91.105";
	//private static final String DEFAULT_SERVER_HOST_URL = "http://192.168.0.126:8080/app-bar";//zijian server

	// internal test Server Address
	// private static final String TEST_SERVER_HOST =
	// "http://192.168.0.126:8088/app-bar";

	// api to get host: the address should not be changed
	public static final String GET_SERVER_HOST = "http://www.mumubar.com:83/DynamicHost/getDynamicHost.do";

	public static String BASE_HOST_URL = null;
	public static String BASE_HTTPS_HOST_URL = null;
	public static String BASE_IMAGE_URL = null;

	public static void setBaseServerUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			BASE_HOST_URL = DEFAULT_SERVER_HOST_URL;
			BASE_HTTPS_HOST_URL = DEFAULT_HTTPS_SERVER_HOST_URL;
		} else {
			BASE_HOST_URL = url;
			if(url.contains("http") && doHttpsRequest){
				String httpsUrl = url.replace("http", "https");
				BASE_HTTPS_HOST_URL = httpsUrl;
			}else{
				BASE_HTTPS_HOST_URL = BASE_HOST_URL;
			}
		}
		BASE_HOST_URL="http://123.57.91.105";
		BASE_HTTPS_HOST_URL="https://123.57.91.105";

		initPaths();
		LogUtil.printUserEntryLog("server host url is:" + BASE_HOST_URL);
		LogUtil.printUserEntryLog("server https host url is:" + BASE_HTTPS_HOST_URL);
	}
	
	public static void setBaseImageUrl(String url){
		if (TextUtils.isEmpty(url)) {
			BASE_IMAGE_URL = DEFAULT_BASE_IMAGE_URL;
		} else {
			BASE_IMAGE_URL = url;
		}

		initPaths();
		LogUtil.printUserEntryLog("image host url is:" + BASE_IMAGE_URL);
	}

	public static void setDefaultServerUrl() {
		BASE_HOST_URL = DEFAULT_SERVER_HOST_URL;
		BASE_HTTPS_HOST_URL = DEFAULT_HTTPS_SERVER_HOST_URL;
		initPaths();
	}

	public static boolean isMumuBarHost() {
		return (BASE_HOST_URL.equals(DEFAULT_SERVER_HOST_URL));
	}

	private static void initPaths() {

		SERVERHOST = BASE_HOST_URL + "/";
		IMAGEHOST = BASE_IMAGE_URL + "/";
		TEST_HEAD_URL = BASE_HOST_URL + "/api/";
		ONLINE_HEAD_URL = BASE_HOST_URL + "/api/";
		TEST_IMAGE_URL = BASE_HOST_URL + "/api/";
		ONLINE_IMAGE_URL = BASE_HOST_URL + "/api/";
		//TODO
		HEAD_URL = BASE_HOST_URL + "/api/";
		HEAD_HTTPS_URL = BASE_HTTPS_HOST_URL + "/api/";
		
		IMAGE_URL = BASE_HOST_URL + "/api/";

		/* pay */
		ALI_PAY_BASE_URL = HEAD_URL + "bbro/alipay/";
		ALI_PAY_HTTPS_BASE_URL = HEAD_HTTPS_URL + "bbro/alipay/";
		ALI_PAY_CONTEXT = ALI_PAY_HTTPS_BASE_URL + "checkAlipayConfig.do";
		ALI_PAY_APPLY = ALI_PAY_HTTPS_BASE_URL + "pay.do";
		ALI_PAY_UPDATE_ORDER = ALI_PAY_HTTPS_BASE_URL + "updatePay.do";
		MY_ORDER_QUERY = ALI_PAY_HTTPS_BASE_URL + "find.do";
		ALI_PAY_REFUND = ALI_PAY_HTTPS_BASE_URL + "refund.do";
		ALI_PAY_DELETE = ALI_PAY_HTTPS_BASE_URL + "del.do";
		ALI_PAY_CONSUMER_BAR = ALI_PAY_HTTPS_BASE_URL + "consumerBar.do";
		ALI_PAY_NOTIFY = ALI_PAY_BASE_URL + "payOver.do";

		/* bar chat */
		BAR_CHAT_PUSH_MESSAGE = HEAD_URL + "jpush/pushBarChatInfo.do";
		BAR_CHAT_BASE_URL = HEAD_URL + "barChatOperate/";
		BAR_CHAT_GET_HISTORY_MESSAGE = BAR_CHAT_BASE_URL + "getBarChatMsg.do";// not
																				// use
																				// by
																				// now
		BAR_CHAT_ADD_USER = BAR_CHAT_BASE_URL + "addUserToBarChat.do";
		BAR_CHAT_REMOVE_USER = BAR_CHAT_BASE_URL + "delUserFromBarChat.do";
		BAR_CHAT_GET_USER = BAR_CHAT_BASE_URL + "getBarChatUserList.do";

		/* whistle blow */
		BLOW_WHISTLE = HEAD_URL + "report/addReport.do";

		/** 示例 */
		example_action = HEAD_URL + "example.action";
		/** 登陆 */
		//login_action = HEAD_URL + "userOperate/userLoginInfce.do";
		login_action = HEAD_HTTPS_URL + "userOperate/userLoginInfce.do";
		/** 获取验证码 */
		getAutoCode_action = HEAD_URL + "member_getAutoCode.action";
		/** 校验验证码 */
		checkoutAutoCode_action = HEAD_URL + "member_checkoutAutoCode.action";
		/** 注册 */
		//register_action = HEAD_URL + "userOperate/userRegisterInfce.do";
		register_action = HEAD_HTTPS_URL + "userOperate/userRegisterInfce.do";
		/** 设置新密码 */
		setNewPassword_action = HEAD_URL + "member_setNewPassword.action";
		/** 更新个人信息 */
		updateMemberInfo_action = HEAD_URL + "member_updateMemberInfo.action";

		/** 上传个人图像 */
		uploadMemberImage_action = HEAD_URL + "userOperate/uploadFile.do";

		/** 忘记密码 */
		forgetpsd_action = HEAD_URL + "userOperate/forgetUserPassword.do";

		/** 记录坐标 */
		location_acation = HEAD_URL + "userAddressOperate/inserUserAddress.do";

		/** 酒吧列表 */
		PUB_BAR_LIST = HEAD_URL + "barOperate/selectBarByCondition.do";

		/** 获取酒吧详情 */
		PUB_BAR_DETAIL = HEAD_URL + "barOperate/selectBarByPrimaryKey.do";

		/** 歌手列表 */
		SINGER_LIST = HEAD_URL + "singerOperate/selectSinger.do";

		/** 歌手详情 */
		SINGER_DETAIL = HEAD_URL + "singerOperate/selectSingerBySingerId.do";

		/** 歌手心情 */
		SINGER_MOOD = HEAD_URL + "singerOperate/selectSingerMoodRecord.do";

		/** 用户心情 */
		USER_MOOD = HEAD_URL + "userOperate/selectUserMoodRecordByUserId.do";

		/** 歌手视频 */
		SINGER_VIDEO = HEAD_URL
				+ "singerVideoOperate/selectSingerVideoDetail.do";

		/** 歌手视频评论 */
		SINGER_VIDEO_COMMENT = HEAD_URL
				+ "singerVideoOperate/selectSingerVideoComment.do";

		/** 歌手视频增加评论 */
		SINGER_VIDEO_COMMENT_INSERT = HEAD_URL
				+ "singerVideoOperate/insertSingerVideoComment.do";

		/** 约会参与人列表获取接口 */
		DATE_PERSSON_LIST = HEAD_URL
				+ "userAppointmentOperate/selectUserAppointmentDetailUserList.do";

		/** 发布约会 */
		DATE_PUBLISH = HEAD_URL
				+ "userAppointmentOperate/insertUserAppointment.do";

		/** 发布约会 */
		DATE_PARTAKE = HEAD_URL
				+ "userAppointmentOperate/insertUserAppointmentJoinByCondition.do";

		/** 发布约会选择的酒水订单 */
		ORDER_LIST = HEAD_URL
				+ "userOrderOperate/selectUserOrderByCondition.do";

		/** 更改约会状态 */
		DATE_UPDATE = HEAD_URL
				+ "userAppointmentOperate/updateUserAppointmentByUserAppointmentId.do";

		/** 评论接口 */
		COMMENT = HEAD_URL + "userDynamicOperate/insertUserDynamicComment.do";

		/** 已成功约会 */
		DATE_SUCCESSFUL = HEAD_URL
				+ "userAppointmentOperate/selectUserAppointmentByCondition.do";

		/** 可加入约会 */
		DATE_ADDENDUS = HEAD_URL
				+ "userAppointmentOperate/selectUserAppointmentByStatus.do";

		/** 约会详情 */
		DATE_DETAIL = HEAD_URL
				+ "userAppointmentOperate/selectUserAppointmentByPrimaryKey.do";

		/** 用户参与约会列表 */
		DATE_ENROLLED = HEAD_URL
				+ "userAppointmentOperate/selectUserAppointmentJoin.do";

		/** 用户发布的约会列表 */
		DATE_PUBLISHED = HEAD_URL
				+ "userAppointmentOperate/selectUserAppointmentListJoinByUserId.do";

		/** 管理想参与约会用户列表(同意、拒绝) */
		DATE_PERSON_MANAGE = HEAD_URL
				+ "userAppointmentJoinOperate/updateUserAppointmentJoinByUserAppointmentId.do";

		/** 选择礼物 */
		SINGER_GIFT = HEAD_URL + "singerOperate/selectSingerGift.do";

		SINGER_PHOTO = HEAD_URL + "singerOperate/selectSingerPhoto.do";

		/** 吧友列表 **/
		BAR_FRIENDS = HEAD_URL + "userOperate/selectUserByCondition.do";

		/** 创建群组 */
		CREATE_GROUPS = HEAD_URL
				+ "barGroupOperate/insertBarGroupByCondition.do";

		/** 获取用户信息 */
		GET_USER_INFO = HEAD_URL + "userOperate/selectUserByUserId.do";

		/** 加入的群组 **/
		IM_IN_GROUPS = HEAD_URL + "userGroupOperate/selectJoinGroupList.do";

		/** 创建的群组 **/
		MY_CREATE_GROUPS = HEAD_URL
				+ "userGroupOperate/selectOwnerGroupList.do";

		/** 附近群组 */
		NEAR_BY_GROUPS = HEAD_URL
				+ "barGroupOperate/selectBarGroupByCondition.do";

		/** 我的动态 */
		MY_DYNAMIC_MINE = HEAD_URL
				+ "userDynamicOperate/selectUserDynamicByUserId.do";

		/** 礼物列表 */
		GIFT_LIST = HEAD_URL + "generalGiftOperate/selectGeneralGift.do";

		/** 修改密码 */
		CHANGE_PASSWD = HEAD_HTTPS_URL + "userOperate/updateUserPassword.do";

		/** 添加关注 */
		ADD_FOLLOW = HEAD_URL
				+ "barUserAttentionOperate/inserBarUserAttention.do";

		/** 取消关注 */
		CANCEL_FOLLOW = HEAD_URL
				+ "barUserAttentionOperate/cancleBarUserAttention.do";

		/** 获取好友列表 **/
		FRIENDS_INFO = HEAD_URL + "userOperate/selectUserInfoByUserAccount.do";

		/** 行业列表 */
		INDUSTRY_LIST = HEAD_URL
				+ "generalIndustryOperate/selectGeneralInDustery.do";

		/** 爱好列表 */
		HOBBY_LIST = HEAD_URL + "generalHobbyOperate/selectGeneralHobby.do";

		/** 幸运相遇 **/
		LUCK_MEET = HEAD_URL + "userOperate/selectUserByDetialInfo.do";

		/** 群组详情 **/
		GROUP_DETAIL = HEAD_URL + "barGroupOperate/selectBarGroupDetail.do";

		/** 加入群组 **/
		JOIN_GROUP = HEAD_URL + "barGroupOperate/joinUserGroup.do";

		/** 退出群组 **/
		OUT_GROUP = HEAD_URL + "barGroupOperate/outUserGroup.do";

		/** 更新群组简介 **/
		UPDATE_GROUP_INTRODUCTION = HEAD_URL
				+ "barGroupOperate/updateByGroupId.do";

		/** 更新用户信息 */
		UPDATE_USER_INFO = HEAD_URL + "userOperate/updateUserByCondition.do";

		/** 获取已关注好友列表 */
		GET_FRIEND_LIST = HEAD_URL + "userDynamicOperate/selectUserDynamic.do";

		/** 获取星座等列表 */
		GET_GENERAL_SUMMARY = HEAD_URL
				+ "generalSummaryOperate/selectGeneralSummary.do";

		/** 更新用户相册 */
		UPDATE_USER_PHOTO = HEAD_URL + "userOperate/updateUserPhotoByUserId.do";

		/** 用户好友动态 */
		USER_FRIENDS_DYNAMIC = HEAD_URL
				+ "userDynamicOperate/selectBarUserAttentionByUserId.do";

		/** 更新心情 */
		UPDATE_USER_MOOD = HEAD_URL
				+ "userOperate/updateUserMoodRecordByUserId.do";

		/** 获取用户心情列表 */
		GET_USER_MOOD = HEAD_URL
				+ "userOperate/selectUserMoodRecordByUserId.do";

		/** 视频点赞 */
		VIDEO_PRIASE_ADD = HEAD_URL
				+ "singerVideoOperate/insertSingerPriase.do";

		/** 视频取消点赞 */
		VIDEO_PRIASE_CANCEL = HEAD_URL
				+ "singerVideoOperate/deleteSingerPriase.do";

		/** 动态评论接口 */
		USER_DYNAMIC_COMMENT = HEAD_URL
				+ "userDynamicOperate/insertUserDynamicComment.do";

		/** 获取我关注的酒吧列表 */
		FOLLOW_PUB = HEAD_URL + "barOperate/selectBarAttentionByUserId.do";

		/** 酒吧点赞 */
		PUB_PRAISE_ADD = HEAD_URL
				+ "barDynamicPriaseOperate/insertBarDynamicPraise.do";

		/** 酒吧取消点赞 */
		PUB_PRAISE_CANCEL = HEAD_URL
				+ "barDynamicPriaseOperate/deleteBarDynamicPraiseByCondition.do";

		/** 更新群组相册 */
		GROUP_UPDATE_PHOTO = HEAD_URL + "barGroupOperate/insertUserGroupImg.do";

		/** 更新用户相册 */
		USER_UPDATE_PHOTO = HEAD_URL + "userOperate/updateUserPhotoByUserId.do";

		/** 酒吧动态新增评论 */
		DYNAMIC_BAR_COMMENT_ADD = HEAD_URL
				+ "barOperate/insertBarDynamicComment.do";

		/** 动态点赞 */
		DYNAMIC_PRIASE = HEAD_URL
				+ "UserDynamicPriaseOperate/insertUserDynamicPriase.do";

		/** 更新 */
		UPDATE_VERSION = HEAD_URL + "appVersion/selectAppVersion.do";

		/** 获取群组成员列表 */
		GROPU_MEMBER_DETAIL = HEAD_URL + "userGroupOperate/getGroupUserList.do";
		
		/** 机器人聊天 */
		CHAT_ROBOT = HEAD_URL+"ibot/chatRobot.do";
	}

	public static String SERVERHOST;
	public static String IMAGEHOST;
	public static String TEST_HEAD_URL;
	public static String ONLINE_HEAD_URL;
	public static String TEST_IMAGE_URL;
	public static String ONLINE_IMAGE_URL;

	public static String HEAD_URL;
	public static String HEAD_HTTPS_URL;
	public static String IMAGE_URL;

	/* pay */
	public static String ALI_PAY_BASE_URL;
	public static String ALI_PAY_HTTPS_BASE_URL;
	public static String ALI_PAY_CONTEXT;
	public static String ALI_PAY_APPLY;
	public static String ALI_PAY_UPDATE_ORDER;
	public static String ALI_PAY_NOTIFY;
	public static String MY_ORDER_QUERY;
	public static String ALI_PAY_REFUND;
	public static String ALI_PAY_DELETE;
	public static String ALI_PAY_CONSUMER_BAR;

	/* bar chat */
	public static String BAR_CHAT_PUSH_MESSAGE;
	public static String BAR_CHAT_BASE_URL;
	public static String BAR_CHAT_GET_HISTORY_MESSAGE;// not use by now
	public static String BAR_CHAT_ADD_USER;
	public static String BAR_CHAT_REMOVE_USER;
	public static String BAR_CHAT_GET_USER;

	/* whistle blow */
	public static String BLOW_WHISTLE;

	/** 示例 */
	public static String example_action;
	/** 登陆 */
	public static String login_action;
	/** 获取验证码 */
	public static String getAutoCode_action;
	/** 校验验证码 */
	public static String checkoutAutoCode_action;
	/** 注册 */
	public static String register_action;
	/** 设置新密码 */
	public static String setNewPassword_action;
	/** 更新个人信息 */
	public static String updateMemberInfo_action;

	/** 上传个人图像 */
	public static String uploadMemberImage_action;

	/** 忘记密码 */
	public static String forgetpsd_action;

	/** 记录坐标 */
	public static String location_acation;

	/** 酒吧列表 */
	public static String PUB_BAR_LIST;

	/** 获取酒吧详情 */
	public static String PUB_BAR_DETAIL;

	/** 歌手列表 */
	public static String SINGER_LIST;

	/** 歌手详情 */
	public static String SINGER_DETAIL;

	/** 歌手心情 */
	public static String SINGER_MOOD;

	/** 用户心情 */
	public static String USER_MOOD;

	/** 歌手视频 */
	public static String SINGER_VIDEO;

	/** 歌手视频评论 */
	public static String SINGER_VIDEO_COMMENT;

	/** 歌手视频增加评论 */
	public static String SINGER_VIDEO_COMMENT_INSERT;

	/** 约会参与人列表获取接口 */
	public static String DATE_PERSSON_LIST;

	/** 发布约会 */
	public static String DATE_PUBLISH;

	/** 发布约会 */
	public static String DATE_PARTAKE;

	/** 发布约会选择的酒水订单 */
	public static String ORDER_LIST;

	/** 更改约会状态 */
	public static String DATE_UPDATE;

	/** 评论接口 */
	public static String COMMENT;

	/** 已成功约会 */
	public static String DATE_SUCCESSFUL;

	/** 可加入约会 */
	public static String DATE_ADDENDUS;

	/** 约会详情 */
	public static String DATE_DETAIL;

	/** 用户参与约会列表 */
	public static String DATE_ENROLLED;

	/** 用户发布的约会列表 */
	public static String DATE_PUBLISHED;

	/** 管理想参与约会用户列表(同意、拒绝) */
	public static String DATE_PERSON_MANAGE;

	/** 选择礼物 */
	public static String SINGER_GIFT;

	public static String SINGER_PHOTO;

	/** 吧友列表 **/
	public static String BAR_FRIENDS;

	/** 创建群组 */
	public static String CREATE_GROUPS;

	/** 获取用户信息 */
	public static String GET_USER_INFO;

	/** 加入的群组 **/
	public static String IM_IN_GROUPS;

	/** 创建的群组 **/
	public static String MY_CREATE_GROUPS;

	/** 附近群组 */
	public static String NEAR_BY_GROUPS;

	/** 我的动态 */
	public static String MY_DYNAMIC_MINE;

	/** 礼物列表 */
	public static String GIFT_LIST;

	/** 修改密码 */
	public static String CHANGE_PASSWD;

	/** 添加关注 */
	public static String ADD_FOLLOW;

	/** 取消关注 */
	public static String CANCEL_FOLLOW;

	/** 获取好友列表 **/
	public static String FRIENDS_INFO;

	/** 行业列表 */
	public static String INDUSTRY_LIST;

	/** 爱好列表 */
	public static String HOBBY_LIST;

	/** 幸运相遇 **/
	public static String LUCK_MEET;

	/** 群组详情 **/
	public static String GROUP_DETAIL;

	/** 加入群组 **/
	public static String JOIN_GROUP;

	/** 退出群组 **/
	public static String OUT_GROUP;

	/** 更新群组简介 **/
	public static String UPDATE_GROUP_INTRODUCTION;

	/** 更新用户信息 */
	public static String UPDATE_USER_INFO;

	/** 获取已关注好友列表 */
	public static String GET_FRIEND_LIST;

	/** 获取星座等列表 */
	public static String GET_GENERAL_SUMMARY;

	/** 更新用户相册 */
	public static String UPDATE_USER_PHOTO;

	/** 用户好友动态 */
	public static String USER_FRIENDS_DYNAMIC;

	/** 更新心情 */
	public static String UPDATE_USER_MOOD;

	/** 获取用户心情列表 */
	public static String GET_USER_MOOD;

	/** 视频点赞 */
	public static String VIDEO_PRIASE_ADD;

	/** 视频取消点赞 */
	public static String VIDEO_PRIASE_CANCEL;

	/** 动态评论接口 */
	public static String USER_DYNAMIC_COMMENT;

	/** 获取我关注的酒吧列表 */
	public static String FOLLOW_PUB;

	/** 酒吧点赞 */
	public static String PUB_PRAISE_ADD;

	/** 酒吧取消点赞 */
	public static String PUB_PRAISE_CANCEL;

	/** 更新群组相册 */
	public static String GROUP_UPDATE_PHOTO;

	/** 更新用户相册 */
	public static String USER_UPDATE_PHOTO;

	/** 酒吧动态新增评论 */
	public static String DYNAMIC_BAR_COMMENT_ADD;

	/** 动态点赞 */
	public static String DYNAMIC_PRIASE;

	/** 更新 */
	public static String UPDATE_VERSION;

	/** 群组用户详情 */
	public static String GROPU_MEMBER_DETAIL;
	
	/** 机器人聊天 */
	public static String CHAT_ROBOT;

}
