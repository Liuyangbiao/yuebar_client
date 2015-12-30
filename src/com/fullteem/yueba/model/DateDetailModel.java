package com.fullteem.yueba.model;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月24日&emsp;上午11:06:36
 * @use 约会详情Bean
 */
public class DateDetailModel extends DateModel {
	// private int barId;
	// private String barLogoUrl;
	// private String barName; ////约会地点
	// private int userApointmentId; //约会id
	// private String userAppointmentDate; //约会时间
	// private String userAppointmentDescrip; //约会描述
	private int userAppointmentFavoriteId; // 心仪对象id
	// private int userAppointmentFee; //约会费用类型(0:你请；1:我请；2:AA)
	// private int userAppointmentObj; //约会期望对象(0:男;1:女;2:不限)
	// private String userAppointmentTitle; //约会主题
	private int userAppointmentType; // 1是单独约会 2是多人约会

	// private int userId;
	// private int userOrderId;
	// private int status; //0终止约会、1发布约会、2停止约会

	public int getUserAppointmentFavoriteId() {
		return userAppointmentFavoriteId;
	}

	public int getUserAppointmentType() {
		return userAppointmentType;
	}

	public void setUserAppointmentFavoriteId(int userAppointmentFavoriteId) {
		this.userAppointmentFavoriteId = userAppointmentFavoriteId;
	}

	public void setAddress(String address) {
		setUserAppointmentLocation(address);
	}

	public void setUserAppointmentType(int userAppointmentType) {
		this.userAppointmentType = userAppointmentType;
	}

	public void setDateModel(DateModel dateModeTmp) {
		setBarId(dateModeTmp.getBarId());
		setBarLogoUrl(dateModeTmp.getBarLogoUrl());
		setCount(dateModeTmp.getCount());
		setUserAppointmentDate(dateModeTmp.getUserAppointmentDate());
		setUserAppointmentDescrip(dateModeTmp.getUserAppointmentDescrip());
		setUserAppointmentId(dateModeTmp.getUserAppointmentId());
		setBarName(dateModeTmp.getBarName());
		setUserName(dateModeTmp.getUserName());
		setUserMobile(dateModeTmp.getUserMobile());
	}

}
