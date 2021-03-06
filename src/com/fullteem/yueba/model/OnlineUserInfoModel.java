package com.fullteem.yueba.model;

import java.io.Serializable;
import java.util.List;

public class OnlineUserInfoModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public List<OnlineUserModel> getUserList() {
		return userList;
	}

	public void setUserList(List<OnlineUserModel> userList) {
		this.userList = userList;
	}

	private String userNum;
	private List<OnlineUserModel> userList;

}
