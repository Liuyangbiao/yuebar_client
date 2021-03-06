package com.fullteem.yueba.model;

import java.io.Serializable;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月19日&emsp;下午6:48:57
 * @use 约会bean
 */
public class DateModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userAppointmentTitle; // 约会主题
	private String userAppointmentDescrip; // 约会描述
	private String userAppointmentDate; // 约会时间
	private String userAppointmentLocation; // 约会地点
	private String barName; // 酒吧名称
	private int userAppointmentObj;// 约会期望对象(0:男;1:女;2:不限)
	private int userAppointmentFee; // 约会费用类型(0:你请；1:我请；2:AA)
	private int userAppointmentJoinType;// 0：不同意；1：同意；2：申请状态中
	private String userName;// 用户名称
	private String barLogoUrl;
	private String userLogoUrl;
	private String userMobile;
	private int count;// 可参与人数
	private int hasCount;// 已参与人数
	private int userAppointmentId;// 约会id
	private int userId = -1;// 用户id
	private int barId;// 酒吧id
	private int userOrderId;// 订单id
	private int status; // 0终止约会、1发布约会、2停止报名
	private boolean isSuccessful;// 是不是成功的约会，掉成功约会接口时手动将属性设为true，反之设为false
	private String imServerId;

	public String getUserAppointmentDescrip() {
		return userAppointmentDescrip;
	}

	public void setUserAppointmentDescrip(String userAppointmentDescrip) {
		this.userAppointmentDescrip = userAppointmentDescrip;
	}

	public String getUserAppointmentDate() {
		return userAppointmentDate;
	}

	public void setUserAppointmentDate(String userAppointmentDate) {
		this.userAppointmentDate = userAppointmentDate;
	}

	public String getBarName() {
		return barName;
	}

	public void setBarName(String barName) {
		this.barName = barName;
	}

	public String getUserAppointmentObj() {
		return userAppointmentObj == 0 ? "男" : (userAppointmentObj == 1 ? "女"
				: "不限");
	}

	public void setUserAppointmentObj(int userAppointmentObj) {
		this.userAppointmentObj = userAppointmentObj;
	}

	public String getUserAppointmentLocation() {
		return userAppointmentLocation;
	}

	public int getUserAppointmentJoinType() {
		return userAppointmentJoinType;
	}

	public void setUserAppointmentJoinType(int userAppointmentJoinType) {
		this.userAppointmentJoinType = userAppointmentJoinType;
	}

	public void setUserAppointmentLocation(String userAppointmentLocation) {
		this.userAppointmentLocation = userAppointmentLocation;
	}

	public String getBarLogoUrl() {
		// if (TextUtils.isEmpty(barLogoUrl))
		// return "drawable://" + R.drawable.img_loading_empty;
		//
		// // 不包含http://或者ftp://
		// if (!(barLogoUrl.contains("http://") ||
		// barLogoUrl.contains("ftp://")))
		// return Urls.SERVERHOST + barLogoUrl;
		//
		// // 包含了看下前缀是不是http开头或者ftp
		// String urlHead = barLogoUrl.substring(0, 7);
		// if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
		// return Urls.SERVERHOST + barLogoUrl;
		//
		// if (barLogoUrl.length() <= 7)// 错误地址
		// return "drawable://" + R.drawable.img_loading_fail;

		return barLogoUrl;
	}

	public void setBarLogoUrl(String barLogoUrl) {
		this.barLogoUrl = barLogoUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserLogoUrl() {
		// if (TextUtils.isEmpty(userLogoUrl))
		// return "drawable://" + R.drawable.img_loading_empty;
		//
		// // 不包含http://或者ftp://
		// if (!(userLogoUrl.contains("http://") ||
		// userLogoUrl.contains("ftp://")))
		// return Urls.SERVERHOST + userLogoUrl;
		//
		// // 包含了看下前缀是不是http开头或者ftp
		// String urlHead = userLogoUrl.substring(0, 7);
		// if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
		// return Urls.SERVERHOST + userLogoUrl;
		//
		// if (userLogoUrl.length() <= 7)// 错误地址
		// return "drawable://" + R.drawable.img_loading_fail;

		return userLogoUrl;
	}

	public void setUserLogoUrl(String userLogoUrl) {
		this.userLogoUrl = userLogoUrl;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getUserAppointmentId() {
		return userAppointmentId;
	}

	public void setUserAppointmentId(int userAppointmentId) {
		this.userAppointmentId = userAppointmentId;
	}

	/**
	 * 同setUserAppointmentId(),后台返回数据解析
	 * 
	 * @param userAppointmentId
	 */
	public void setUserApointmentId(int userAppointmentId) {
		this.userAppointmentId = userAppointmentId;
	}

	public int getHasCount() {
		return hasCount;
	}

	public int getUserId() {
		return userId;
	}

	public void setHasCount(int hasCount) {
		this.hasCount = hasCount;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserAppointmentTitle() {
		return userAppointmentTitle;
	}

	public void setUserAppointmentTitle(String userAppointmentTitle) {
		this.userAppointmentTitle = userAppointmentTitle;
	}

	public String getUserAppointmentFee() {
		return userAppointmentFee == 0 ? "你请" : userAppointmentFee == 1 ? "我请"
				: "AA";
		// 0:你请；1:我请；2:AA
	}

	public int getBarId() {
		return barId;
	}

	public int getUserOrderId() {
		return userOrderId;
	}

	public int getStatus() {
		return status;
	}

	public void setUserAppointmentFee(int userAppointmentFee) {
		this.userAppointmentFee = userAppointmentFee;
	}

	public void setBarId(int barId) {
		this.barId = barId;
	}

	public void setUserOrderId(int userOrderId) {
		this.userOrderId = userOrderId;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getImServerId() {
		return imServerId;
	}

	public void setImServerId(String imServerId) {
		this.imServerId = imServerId;
	}

}
