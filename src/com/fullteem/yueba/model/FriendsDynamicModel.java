package com.fullteem.yueba.model;

import java.io.Serializable;
import java.util.List;

/**
 * 好友动态model
 * 
 * @author ssy
 * 
 */
public class FriendsDynamicModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String moodRecordId;
	private String userLogourl;
	private String userName;
	private String createDate;
	private String moodRecordText;
	private String moodRecordImgUrl;
	private List<UserDynamicComment> userDynamicComment;

	public List<UserDynamicComment> getUserDynamicComment() {
		return userDynamicComment;
	}

	public void setUserDynamicComment(
			List<UserDynamicComment> userDynamicComment) {
		this.userDynamicComment = userDynamicComment;
	}

	public String getMoodRecordId() {
		return moodRecordId;
	}

	public void setMoodRecordId(String moodRecordId) {
		this.moodRecordId = moodRecordId;
	}

	public String getUserLogourl() {
		return userLogourl;
	}

	public void setUserLogourl(String userLogourl) {
		this.userLogourl = userLogourl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getMoodRecordText() {
		return moodRecordText;
	}

	public void setMoodRecordText(String moodRecordText) {
		this.moodRecordText = moodRecordText;
	}

	public String getMoodRecordImgUrl() {
		return moodRecordImgUrl;
	}

	public void setMoodRecordImgUrl(String moodRecordImgUrl) {
		this.moodRecordImgUrl = moodRecordImgUrl;
	}

	class UserDynamicComment {
		private String commenterId;
		private String userDynamicId;
		private String createDate;
		private String dynamicCommentContent;
		private String userDynamicCommentId;

		public String getCommenterId() {
			return commenterId;
		}

		public void setCommenterId(String commenterId) {
			this.commenterId = commenterId;
		}

		public String getUserDynamicId() {
			return userDynamicId;
		}

		public void setUserDynamicId(String userDynamicId) {
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

		public String getUserDynamicCommentId() {
			return userDynamicCommentId;
		}

		public void setUserDynamicCommentId(String userDynamicCommentId) {
			this.userDynamicCommentId = userDynamicCommentId;
		}

	}

}
