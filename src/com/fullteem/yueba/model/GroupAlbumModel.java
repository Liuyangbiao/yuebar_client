package com.fullteem.yueba.model;
/**
 * 群组相册model
 * @author jecinwang
 *
 */
public class GroupAlbumModel {
	
	private String createDate;
	private String status;
	private String userGroupId;
	private String userGroupImgId;
	private String userGroupImgUrl;

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getUserGroupImgId() {
		return userGroupImgId;
	}

	public void setUserGroupImgId(String userGroupImgId) {
		this.userGroupImgId = userGroupImgId;
	}

	public String getUserGroupImgUrl() {
		return userGroupImgUrl;
	}

	public void setUserGroupImgUrl(String userGroupImgUrl) {
		this.userGroupImgUrl = userGroupImgUrl;
	}

}
