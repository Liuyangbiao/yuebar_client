package com.fullteem.yueba.model;

import java.io.Serializable;

import com.fullteem.yueba.engine.push.MessageUtil;

public class BarChatModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String barId;

	@Override
	public String toString() {
		return "BarChatModel [barId=" + barId + ", userId=" + userId
				+ ", userName=" + userName + ", msgContent=" + msgContent
				+ ", userLogoUrl=" + userLogoUrl + ", msgType=" + msgType
				+ ", userSex=" + userSex + ", userAge=" + userAge
				+ ", createDate=" + createDate + ", userNum=" + userNum + "]";
	}

	private String userId;
	private String userName;
	private String msgContent;
	private String userLogoUrl;
	private int msgType;// 1: string; 2: image name; 3: both string and image
						// name

	private String userSex; // 1: male 2: female
	private String userAge;
	private String createDate;
	private String userNum;

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

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getUserLogoUrl() {
		return userLogoUrl;
	}

	public void setUserLogoUrl(String userLogoUrl) {
		this.userLogoUrl = userLogoUrl;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	/* other public functions */
	public boolean isMale() {
		return (this.userSex.equals("1"));
	}

	public boolean isReceiver() {
		return MessageUtil.isMessageReceiver(this.userId);
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}
}
