package com.fullteem.yueba.model;

import java.io.Serializable;

import android.widget.TextView;

import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.widget.CircleImageView;

/**
 * 用户基础信息保存类
 * 
 * @author ssy
 * 
 */
public class UserCommonModel extends CommonModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userSex;
	private String userMobile;
	private String userName;// nick name
	private boolean isSinger;
	private String createDate;
	private String userLogoUrl;
	private TextView nameTextView;
	private CircleImageView ivHeader;

	private String userAccount;// Mumu Id
	private String imServerId;// IM id
	private String imServerPwd;

	private String userId;// login
	private String userAge;

	/*
	 * when user login: he may use mumu id(userAccount) or phone number when
	 * user do some operation: userId(index of the table, served as foreign key)
	 * is needed to passed to server, to do table query.
	 * 
	 * localName in AppContext is used to save the user info when he login, so
	 * it may be mumu id, or phone number. while userName in AppContext is to
	 * save IM server account, so it is equal to mumu id. This info is just
	 * saved on memory, not file system. And it is set when login Im server
	 * succeed.
	 * 
	 * in LoginActivity, doSelfLogin succeed-->appContext.setUserInfo,
	 * appContext.setLocalUserName doIMServerLogin
	 * succeed-->appContext.setUserName
	 * 
	 * usages: appContext.getUserName()--> im id --- has modified code to used
	 * imServerId. appContext.getUserInfo().getImServerId()-->im id
	 * appContext.getUserInfo().getUserName()-->nick name
	 * appContext.getUserInfo().getUserId()-->db index
	 */

	public String getImServerId() {
		return imServerId;
	}

	public void setImServerId(String imServerId) {
		this.imServerId = imServerId;
	}

	public String getImServerPwd() {
		return imServerPwd;
	}

	public void setImServerPwd(String imServerPwd) {
		this.imServerPwd = imServerPwd;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserLogoUrl() {
		return userLogoUrl;
	}

	public void setUserLogoUrl(String userLogoUrl) {
		if (!userLogoUrl.contains(Urls.IMAGEHOST)) {
			this.userLogoUrl = Urls.IMAGEHOST + userLogoUrl;
		} else {
			this.userLogoUrl = userLogoUrl;
		}
	}

	public CircleImageView getIvHeader() {
		return ivHeader;
	}

	public void setIvHeader(CircleImageView ivHeader) {
		this.ivHeader = ivHeader;
	}

	public TextView getNameTextView() {
		return nameTextView;
	}

	public void setNameTextView(TextView nameTextView) {
		this.nameTextView = nameTextView;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isSinger() {
		return isSinger;
	}

	public void setSinger(boolean isSinger) {
		this.isSinger = isSinger;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}

}
