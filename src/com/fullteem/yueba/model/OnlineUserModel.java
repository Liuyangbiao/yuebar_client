package com.fullteem.yueba.model;

import java.io.Serializable;

public class OnlineUserModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return "OnlineUserModel [barId=" + barId + ", userId=" + userId
				+ ", userName=" + userName + ", userLogoUrl=" + userLogoUrl
				+ ", userSex=" + userSex + ", userAge=" + userAge + "]";
	}

	public String getBarId() {
		return barId;
	}

	public void setBarId(String barId) {
		this.barId = barId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserLogoUrl() {
		return userLogoUrl;
	}

	public void setUserLogoUrl(String userLogoUrl) {
		this.userLogoUrl = userLogoUrl;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}

	private String barId;
	private String userId;
	private String userName;
	private String userLogoUrl;
	private String userSex;
	private String userAge;

}
