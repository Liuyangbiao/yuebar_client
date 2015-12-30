package com.fullteem.yueba.model;

import java.io.Serializable;

/**
 * 用户评论model
 * 
 * @author ssy
 * 
 */
public class UserDynamicCommentModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int commenterId = -1;// 评论人id
	private int userDynamicId;// 动态id
	private String createDate;
	private String dynamicCommentContent;// 评论
	private int userDynamicCommentId; // 评论id
	private int userId;// 用户名
	private String userName;// 用户名
	private String userDynamicGroup;// 评价组
	private String userLogoUrl;
	private String commenterUserLogoUrl;
	private String replyerUserLogoUrl;
	private String replyerUserName;
	private String commenterUserName;
	private int userDynamicCommentType;
	private int replyerId;
	private String commentHint = "";// 评论编辑框提示内容
	private String commentContent = "";// 评论编辑框评论内容

	public int getReplyerId() {
		return replyerId;
	}

	public void setReplyerId(int replyerId) {
		this.replyerId = replyerId;
	}

	public int getUserDynamicCommentType() {
		return userDynamicCommentType;
	}

	public void setUserDynamicCommentType(int userDynamicCommentType) {
		this.userDynamicCommentType = userDynamicCommentType;
	}

	public String getReplyerUserName() {
		return replyerUserName;
	}

	public void setReplyerUserName(String replyerUserName) {
		this.replyerUserName = replyerUserName;
	}

	public String getCommenterUserName() {
		return commenterUserName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setCommenterUserName(String commenterUserName) {
		this.commenterUserName = commenterUserName;
	}

	public String getCommenterUserLogoUrl() {
		return commenterUserLogoUrl;
	}

	public void setCommenterUserLogoUrl(String commenterUserLogoUrl) {
		this.commenterUserLogoUrl = commenterUserLogoUrl;
	}

	public String getReplyerUserLogoUrl() {
		return replyerUserLogoUrl;
	}

	public void setReplyerUserLogoUrl(String replyerUserLogoUrl) {
		this.replyerUserLogoUrl = replyerUserLogoUrl;
	}

	public String getUserLogoUrl() {
		return userLogoUrl;
	}

	public void setUserLogoUrl(String userLogoUrl) {
		this.userLogoUrl = userLogoUrl;
	}

	public String getUserDynamicGroup() {
		return userDynamicGroup;
	}

	public void setUserDynamicGroup(String userDynamicGroup) {
		this.userDynamicGroup = userDynamicGroup;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(int commenterId) {
		this.commenterId = commenterId;
	}

	public int getUserDynamicId() {
		return userDynamicId;
	}

	public void setUserDynamicId(int userDynamicId) {
		this.userDynamicId = userDynamicId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getDynamicCommentContent() {
		return dynamicCommentContent;
	}

	public void setDynamicCommentContent(String dynamicCommentContent) {
		this.dynamicCommentContent = dynamicCommentContent;
	}

	public int getUserDynamicCommentId() {
		return userDynamicCommentId;
	}

	public void setUserDynamicCommentId(int userDynamicCommentId) {
		this.userDynamicCommentId = userDynamicCommentId;
	}

	public String getCommentHint() {
		return commentHint;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentHint(String commentHint) {
		this.commentHint = commentHint;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

}
