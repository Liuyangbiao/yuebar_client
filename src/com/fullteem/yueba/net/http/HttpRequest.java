package com.fullteem.yueba.net.http;

import android.content.Context;

import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.singer.model.SingerListModel;
import com.fullteem.yueba.app.singer.model.VideoCommentModel;
import com.fullteem.yueba.model.DateModel;
import com.fullteem.yueba.model.GiftModel;
import com.fullteem.yueba.model.HobbyModel;
import com.fullteem.yueba.model.IndustryModel;
import com.fullteem.yueba.model.MoodModel;
import com.fullteem.yueba.model.NearbyPubFriendsModel;
import com.fullteem.yueba.model.PubModel;
import com.fullteem.yueba.model.RequestModel;
import com.fullteem.yueba.model.VersionUpdateModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SharePreferenceUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月18日&emsp;下午1:03:46
 * @use 网络请求操作类
 */
public class HttpRequest {

	private static CustomAsyncHttpClient httpClient;
	private static HttpRequest mInstance;

	public static HttpRequest getInstance(Context context) {
		httpClient = new CustomAsyncHttpClient(context);
		// 双重锁定
		if (mInstance == null) {
			synchronized (HttpRequest.class) {
				if (mInstance == null) {
					mInstance = new HttpRequest();
				}
			}
		}
		return mInstance;
	}

	public static HttpRequest getInstance() {
		httpClient = new CustomAsyncHttpClient(null);
		// 双重锁定
		if (mInstance == null) {
			synchronized (HttpRequest.class) {
				if (mInstance == null) {
					mInstance = new HttpRequest();
				}
			}
		}
		return mInstance;
	}

	/**
	 * 获取酒吧列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param handler
	 * @param barTypeId
	 *            1清吧 2迪吧 3演绎 null不限
	 */
	public void getPubList(int pageNumber, int pageSize, Integer barTypeId,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		if (barTypeId != null)
			params.put("barTypeId", barTypeId);

		String city = SharePreferenceUtil.getInstance(
				AppContext.getApplication()).getSettingCityId();
		params.put("cityId", city);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(true);
		requestModel.setUrl(Urls.PUB_BAR_LIST);
		requestModel.setCls(PubModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取酒吧详情
	 */
	public void getPubDetait(int barId, int userId,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("barId", barId);
		params.put("userId", userId);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.PUB_BAR_DETAIL);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取歌手列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param singerSex
	 *            1:男;2女;null不限、默认
	 * @param handler
	 */
	public void getSingerList(int pageNumber, int pageSize, Integer singerSex,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		if (singerSex != null)
			params.put("singerSex", singerSex);

		String city = SharePreferenceUtil.getInstance(
				AppContext.getApplication()).getSettingCityId();
		params.put("cityId", city);

		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.SINGER_LIST);
		requestModel.setCls(SingerListModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取歌手详情
	 * 
	 * @param singerId
	 *            歌手id
	 */
	public void getSingerDetait(int singerId, int userId,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("singerId", singerId);
		params.put("userId", userId);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.SINGER_DETAIL);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取歌手心情记录
	 * 
	 * @param singerId
	 *            歌手id
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            页大小
	 */
	public void getSingerMood(int singerId, int pageNumber, int pageSize,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("singerId", singerId);
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.SINGER_MOOD);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取用户心情记录
	 * 
	 * @param userId
	 *            用户id
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            页大小
	 */
	public void getUserMood(int userId, int pageNumber, int pageSize,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		requestModel.setParams(params);
		requestModel.setCls(MoodModel.class);
		requestModel.setUrl(Urls.USER_MOOD);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 发表心情
	 * 
	 */
	public void getUpdateUserMood(int userId, String userMoodRecord,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("userMoodRecord", userMoodRecord);
		requestModel.setParams(params);
		requestModel.setShowDialog(true);
		requestModel.setUrl(Urls.UPDATE_USER_MOOD);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取已成功约会列表
	 * 
	 * @param handler
	 */
	public void getDateSuccessfulList(CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		String city = SharePreferenceUtil.getInstance(AppContext.getApplication()).getSettingCityId();
		params.put("cityId", city);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.DATE_SUCCESSFUL);
		// requestModel.setCls(DateModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取可加入约会列表
	 * 
	 * @param userSex
	 *            1:男；2女,不限就不传递任何参数
	 * @param handler
	 */
	public void getDateAddendusList(int userId, int pageNumber, int pageSize,
			Integer userAppointmentObj, CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		if (userAppointmentObj != null)
			params.put("userAppointmentObj", userAppointmentObj);

		String city = SharePreferenceUtil.getInstance(
				AppContext.getApplication()).getSettingCityId();
		params.put("cityId", city);
		LogUtil.printDateLog("getDateAddendusList city:" + city);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.DATE_ADDENDUS);
		requestModel.setCls(DateModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取歌手视频详情
	 * 
	 * @param singerId
	 *            歌手视频id
	 */
	public void getSingerVideoDetait(int videoId, Integer userId,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("singerVideoId", videoId);
		if (userId != null)
			params.put("userId", userId);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.SINGER_VIDEO);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取歌手视频的评论
	 * 
	 * @param videoId
	 * @param pageNumber
	 * @param pageSize
	 * @param handler
	 */
	public void getSingerVideoComment(int videoId, int pageNumber,
			int pageSize, CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("singerVideoId", videoId);
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.SINGER_VIDEO_COMMENT);
		requestModel.setCls(VideoCommentModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 歌手视频评论|回复
	 * 
	 * @param singerVideoId
	 *            歌手视频id
	 * @param commenterId
	 *            评论人id
	 * @param singerVideoCommentContent
	 *            评论内容
	 * @param typeId
	 *            1：评论 2 回复
	 * @param handler
	 */
	public void sendSingerVideoComment(int singerVideoId, int commenterId,
			String singerVideoCommentContent, int typeId,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("singerVideoId", singerVideoId);
		params.put("commenterId", commenterId);
		params.put("singerVideoCommentContent", singerVideoCommentContent);
		params.put("typeId", typeId);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.SINGER_VIDEO_COMMENT_INSERT);
		requestModel.setCls(VideoCommentModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 发送评论
	 * 
	 * @param userDynamicId
	 *            动态ID
	 * @param commenterId
	 *            评论人ID
	 * @param replyerId
	 *            回复人ID
	 * @param userDynamicCommentType
	 *            类型 1评论 2回复
	 * @param dynamicCommentContent
	 *            评论内容
	 * @param typeId
	 *            1：评论 2 回复，这个状态必须带
	 * @param handler
	 */
	public void sendComment(int userDynamicId, int commenterId,
			Integer replyerId, int userDynamicCommentType,
			String dynamicCommentContent, int typeId,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userDynamicId", userDynamicId);
		params.put("commenterId", commenterId);
		if (replyerId != null && replyerId > -1)
			params.put("replyerId", replyerId);
		params.put("userDynamicCommentType", userDynamicCommentType);
		params.put("dynamicCommentContent", dynamicCommentContent);
		params.put("typeId", typeId);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.COMMENT);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 约会详情
	 * 
	 * @param userAppointmentId
	 *            约会id
	 * @param handler
	 */
	public void getDateDetail(int userAppointmentId,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userAppointmentId", userAppointmentId);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.DATE_DETAIL);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 约会参与人接口
	 * 
	 * @param userAppointmentId
	 *            约会id
	 * @param pageNumber
	 * @param pageSize
	 * @param userApponitmentJoinType
	 *            0不同意，1同意，2申请状态中 null为默认
	 * @param handler
	 */
	public void getDatePerson(int userAppointmentId, int pageNumber,
			int pageSize, Integer userApponitmentJoinType,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userAppointmentId", userAppointmentId);
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		if (userApponitmentJoinType != null)
			params.put("userApponitmentJoinType", userApponitmentJoinType);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.DATE_PERSSON_LIST);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 管理约会参与人
	 * 
	 * @param userAppointmentJoinId
	 *            参加约会ID
	 * @param userApponitmentJoinType
	 *            0不同意，1同意，2申请状态中
	 * @param handler
	 */
	public void getDatePersonManage(int userAppointmentJoinId,
			int userApponitmentJoinType, CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userAppointmentJoinId", userAppointmentJoinId);
		params.put("userApponitmentJoinType", userApponitmentJoinType);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.DATE_PERSON_MANAGE);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 加入约会
	 * 
	 * @param userAppointmentId
	 * @param userId
	 * @param handler
	 */
	public void getDatePartake(int userAppointmentId, int userId,
			CustomAsyncResponehandler handler) {
		// http://124.237.121.72:8082/app-bar/api/userAppointmentOperate/insertUserAppointmentJoinByCondition.do?p={"userAppointmentId":9,”userId”:1}
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userAppointmentId", userAppointmentId);
		params.put("userId", userId);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.DATE_PARTAKE);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 发布约会
	 * 
	 * @param userAppointmentType
	 *            约会类型，1单独约会，2多人约会
	 * @param userAppointmentFee
	 *            约会费用类型(0:你请；1:我请；2:AA)
	 * @param userAppointmentObj
	 *            约会期望对象(0:男;1:女;2:不限)
	 * @param userAppointmentFavoriteId
	 *            心仪对象id ，从聊天等界面发起也会需要该id
	 * @param userAppointmentTitle
	 *            约会标题
	 * @param userAppointmentDescrip
	 *            约会描述
	 * @param userAppointmentDate
	 *            约会时间,时间戳
	 * @param barId
	 *            酒吧id
	 * @param userId
	 *            登录用户
	 * @param userOrderId
	 *            订单id 男士用户发布约会学要
	 * 
	 */
	public void datePublish(int userAppointmentType, int userAppointmentFee,
			Integer userAppointmentObj, Integer userAppointmentFavoriteId,
			String userAppointmentTitle, String userAppointmentDescrip,
			String userAppointmentDate, int barId, int userId,
			Integer userOrderId, CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userAppointmentType", userAppointmentType);
		params.put("userAppointmentFee", userAppointmentFee);
		if (userAppointmentObj != null)
			params.put("userAppointmentObj", userAppointmentObj);
		if (userAppointmentFavoriteId != null)
			params.put("userAppointmentFavoriteId", userAppointmentFavoriteId);
		params.put("userAppointmentTitle", userAppointmentTitle);
		params.put("userAppointmentDescrip", userAppointmentDescrip);
		params.put("userAppointmentDate", userAppointmentDate);
		params.put("barId", barId);
		params.put("userId", userId);
		if (userOrderId != null)
			params.put("userOrderId", userOrderId);
		requestModel.setParams(params);
		requestModel.setShowDialog(true);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.DATE_PUBLISH);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 发布约会选择的酒水订单
	 * 
	 * @param userId
	 * @param barId
	 * @param pageNumber
	 * @param pageSize
	 * @param handler
	 */
	public void getOrderList(int userId, int barId, int pageNumber,
			int pageSize, CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("barId", barId);
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.ORDER_LIST);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 更改约会状态
	 * 
	 * @param userAppointmentId
	 *            约会id
	 * @param status
	 *            约会状态 0终止约会 2 停止报名
	 * @param handler
	 */
	public void getDateUpdate(int userAppointmentId, int status,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userAppointmentId", userAppointmentId);
		params.put("status", status);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.DATE_UPDATE);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 礼物列表
	 * 
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            页大小
	 * @param handler
	 */
	public void getGiftList(int pageNumber, int pageSize,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.GIFT_LIST);
		requestModel.setCls(GiftModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 修改密码
	 * 
	 * @param userMobile
	 *            电话号码
	 * @param oldPassword
	 *            旧密码
	 * @param newPassword
	 *            新密码
	 * @param handler
	 */
	public void getChangePwd(String userMobile, String oldPassword,
			String newPassword, CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userMobile", userMobile);
		params.put("oldPassword", oldPassword);
		params.put("newPassword", newPassword);
		requestModel.setParams(params);
		requestModel.setShowDialog(false);
		requestModel.setShowErrorMessage(false);
		requestModel.setUrl(Urls.CHANGE_PASSWD);
		httpClient.doHttpsPost(requestModel, handler);
	}

	/**
	 * 关注、取消关注操作
	 * 
	 * @param barUserAttentionType
	 *            1:酒吧关注；2歌手关注；3好友关注
	 * @param barUserId
	 *            被关注ID（可能是酒吧可能是用户）
	 * @param userId
	 *            关注人id
	 * @param barUserMobile
	 *            被关注人手机号（酒吧关注无此项）
	 * @param userMobile
	 *            关注人手机号
	 * @param isFollow
	 *            当前是否已关注
	 * @param handler
	 */
	public void operateFollow(int barUserAttentionType, int barUserId,
			int userId, String barUserMobile, String userMobile,
			boolean isFollow, CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("barUserAttentionType", barUserAttentionType);
		params.put("barUserId", barUserId);
		params.put("userId", userId);
		if (barUserMobile != null)
			params.put("barUserMobile", barUserMobile);
		if (userMobile != null)
			params.put("userMobile", userMobile);
		requestModel.setParams(params);
		requestModel.setShowDialog(true);
		requestModel.setUrl(isFollow ? Urls.CANCEL_FOLLOW : Urls.ADD_FOLLOW);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 用户参与约会列表
	 * 
	 * @param handler
	 */
	public void getDateEnrolled(int userId, int pageNumber, int pageSize,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.DATE_ENROLLED);
		requestModel.setCls(DateModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 用户发布的约会列表
	 * 
	 * @param handler
	 */
	public void getDatePublished(int userId, int pageNumber, int pageSize,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.DATE_PUBLISHED);
		requestModel.setCls(DateModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取行业列表
	 * 
	 * @param handler
	 */
	public void getIndustryList(CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		requestModel.setUrl(Urls.INDUSTRY_LIST);
		requestModel.setCls(IndustryModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取爱好列表
	 * 
	 * @param handler
	 */
	public void getHobbyList(CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		requestModel.setUrl(Urls.HOBBY_LIST);
		requestModel.setCls(HobbyModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取幸运相遇吧友列表
	 * 
	 * @param userId
	 * @param hobbyId
	 *            兴趣ID
	 * @param industryId
	 *            行业id
	 * @param userSex
	 *            性别( 1:男 ；2:女)
	 * @param pageNumber
	 * @param pageSize
	 * @param handler
	 */
	public void getLuckMeetList(int userId, Integer hobbyId,
			Integer industryId, Integer userSex, int pageNumber, int pageSize,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		if (hobbyId != null)
			params.put("hobbyId", hobbyId);
		if (industryId != null)
			params.put("industryId", industryId);
		if (userSex != null)
			params.put("userSex", userSex);
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.LUCK_MEET);
		requestModel.setCls(NearbyPubFriendsModel.PubFriendsModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 群组详情
	 * 
	 * @param userId
	 * @param groupId
	 * @param handler
	 */
	public void getGroupDetail(int userId, String groupId,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("groupId", groupId);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.GROUP_DETAIL);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 加入群组
	 * 
	 * @param userMobile
	 * @param groupId
	 * @param handler
	 */
	public void getJoinGroup(String userMobile, String groupId,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userMobile", userMobile);
		params.put("groupId", groupId);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.JOIN_GROUP);
		requestModel.setShowDialog(true);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 退出群组
	 * 
	 * @param userMobile
	 * @param groupId
	 * @param handler
	 */
	public void getOutGroup(String userMobile, String groupId,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userMobile", userMobile);
		params.put("groupId", groupId);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.OUT_GROUP);
		requestModel.setShowDialog(true);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 更新群组简介
	 * 
	 * @param groupId
	 *            组id
	 * @param groupIntroduction
	 *            简介
	 * @param handler
	 */
	public void getUpdateGroupIntroduction(String groupId,
			String groupIntroduction, CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("groupId", groupId);
		params.put("groupDesc", groupIntroduction);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.UPDATE_GROUP_INTRODUCTION);
		requestModel.setShowDialog(true);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 视频点赞
	 * 
	 * @param singerVideoId
	 * @param userId
	 * @param handler
	 */
	public void getVideoPriase(int singerVideoId, int userId,
			boolean isCancelPriase, CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("singerVideoId", singerVideoId);
		params.put("userId", userId);
		requestModel.setParams(params);
		requestModel.setUrl(isCancelPriase ? Urls.VIDEO_PRIASE_CANCEL
				: Urls.VIDEO_PRIASE_ADD);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 获取我关注的酒吧列表
	 * 
	 * @param userId
	 * @param pageNumber
	 * @param pageSize
	 * @param handler
	 */
	public void getFollowPub(int userId, int pageNumber, int pageSize,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("pageNumber", pageNumber);
		params.put("pageSize", pageSize);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.FOLLOW_PUB);
		requestModel.setCls(PubModel.class);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 酒吧动态点赞、取消赞
	 * 
	 * @param barDynamicId
	 * @param userId
	 * @param isCancelPriase
	 * @param handler
	 */
	public void getPubPriase(int barDynamicId, int userId,
			boolean isCancelPriase, CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("barDynamicId", barDynamicId);
		params.put("userId", userId);
		requestModel.setParams(params);
		requestModel.setUrl(isCancelPriase ? Urls.PUB_PRAISE_CANCEL
				: Urls.PUB_PRAISE_ADD);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 更新群组图片
	 * 
	 * @param userGroupId
	 * @param userImgGroupUrl
	 * @param handler
	 */
	public void getGroupUpPhoto(String userGroupId, String userImgGroupUrl,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userGroupId", userGroupId);
		params.put("userImgGroupUrl", userImgGroupUrl);
		requestModel.setParams(params);
		requestModel.setShowDialog(true);
		requestModel.setUrl(Urls.GROUP_UPDATE_PHOTO);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 更新用户相册
	 * 
	 * @param userGroupId
	 * @param userImgGroupUrl
	 * @param handler
	 */
	public void getUserUpPhoto(int userId, String userPhotoImgurl,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("userPhotoImgurl", userPhotoImgurl);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.USER_UPDATE_PHOTO);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 酒吧动态评论
	 * 
	 * @param barDynamicId
	 *            酒吧动态ID
	 * @param commenterId
	 *            评论人ID
	 * @param replyerId
	 *            回复人ID
	 * @param barDynamicCommentType
	 *            类型 1评论 2回复。仅发表评价时，类型为1，后续的回复都为2
	 * @param dynamicCommentContent
	 *            评论内容
	 * @param barDynamicGroup
	 *            评价组（一条评论和其所有回复属于同组），第一条评论可以为空，后续的回复必须带上后台返回的值
	 * @param handler
	 */
	public void addBarDynamicComment(int barDynamicId, Integer commenterId,
			Integer replyerId, int barDynamicCommentType,
			String dynamicCommentContent, String barDynamicGroup,
			CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("barDynamicId", barDynamicId);
		if (commenterId != null)
			params.put("commenterId", commenterId);
		if (replyerId != null)
			params.put("replyerId", replyerId);
		params.put("barDynamicCommentType", barDynamicCommentType);
		params.put("dynamicCommentContent", dynamicCommentContent);
		if (barDynamicGroup != null)
			params.put("barDynamicGroup", barDynamicGroup);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.DYNAMIC_BAR_COMMENT_ADD);
		httpClient.post(requestModel, handler);
	}

	/**
	 * 检查本版更新
	 */
	public void checkVersion(CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("appType", 0); // appType 0 android,1 ios
		requestModel.setParams(params);
		requestModel.setCls(VersionUpdateModel.class);
		requestModel.setUrl(Urls.UPDATE_VERSION);
		httpClient.post(requestModel, handler);
	}
	
	/**
	 * 检查本版更新
	 */
	public void getServerHost(String version, String type, String id,CustomAsyncResponehandler handler) {
		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		params.put("version", version); 
		params.put("type", type); 
		params.put("id", id);
		requestModel.setParams(params);
		requestModel.setUrl(Urls.GET_SERVER_HOST);
		httpClient.post(requestModel, handler);
	}

}
