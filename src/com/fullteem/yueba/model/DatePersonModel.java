package com.fullteem.yueba.model;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月24日&emsp;下午3:22:39
 * @use 约会的人bean
 */
public class DatePersonModel extends UserInfoModel {
	private int userAppointmentId; // 参与约会的id
	private int userAppointmentJoinId; // 参加约会ID,发布约会的人用来管理参与者
	private String userMobile;
	private String imServerId;

	public String getImServerId() {
		return imServerId;
	}

	public void setImServerId(String imServerId) {
		this.imServerId = imServerId;
	}

	public int getUserAppointmentId() {
		return userAppointmentId;
	}

	public void setSex(String sex) {
		setUserGender(sex);
	}

	public void setUserAppointmentId(int userAppointmentId) {
		this.userAppointmentId = userAppointmentId;
	}

	public int getUserAppointmentJoinId() {
		return userAppointmentJoinId;
	}

	public void setUserAppointmentJoinId(int userAppointmentJoinId) {
		this.userAppointmentJoinId = userAppointmentJoinId;
	}

	public void setUserLogoUrl(String userLogoUrl) {
		super.setUserHeader(userLogoUrl);
	}

	public void setUserName(String userName) {
		super.setUserNickname(userName);
	}

	public void setUserAsign(String userAsign) {
		super.setUserPersonalitySignature(userAsign);
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

}
