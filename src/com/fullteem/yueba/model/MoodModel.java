package com.fullteem.yueba.model;

import java.io.Serializable;
import java.util.List;

import com.fullteem.yueba.app.singer.model.SingerModel;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月22日&emsp;下午2:36:40
 * @use 心情bean
 */
public class MoodModel extends SingerModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String moodRecordImgUrl; // 心情图片
	private String moodRecordText;// 心情描述
	private String postDate; // 心情发布时间
	private String praise;// 点赞量
	private String createDate;
	private String moodRecordId;
	private String userLogourl;
	private String userName;
	private List<UserDynamicCommentModel> userDynamicComment;
	private String userId;
	private String commentNum;
	private String total;

	public String getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}

	public String getTotal() {
		return total;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<UserDynamicCommentModel> getUserDynamicComment() {
		return userDynamicComment;
	}

	public void setUserDynamicComment(
			List<UserDynamicCommentModel> userDynamicComment) {
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

	public void setPraise(String praise) {
		this.praise = praise;
	}

	public String getMoodRecordImgUrl() {
		// if (TextUtils.isEmpty(moodRecordImgUrl))
		// return "drawable://" + R.drawable.img_loading_empty;
		//
		// // 不包含http://或者ftp://
		// if (!(moodRecordImgUrl.contains("http://") || moodRecordImgUrl
		// .contains("ftp://")))
		// return Urls.SERVERHOST + moodRecordImgUrl;
		//
		// // 包含了看下前缀是不是http开头或者ftp
		// String urlHead = moodRecordImgUrl.substring(0, 7);
		// if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
		// return Urls.SERVERHOST + moodRecordImgUrl;
		//
		// if (moodRecordImgUrl.length() <= 7)// 错误地址
		// return "drawable://" + R.drawable.img_loading_fail;
		return moodRecordImgUrl;
	}

	public String getMoodRecordText() {
		return moodRecordText;
	}

	public void setMoodRecordImgUrl(String moodRecordImgUrl) {
		this.moodRecordImgUrl = moodRecordImgUrl;
	}

	public void setMoodRecordText(String moodRecordText) {
		this.moodRecordText = moodRecordText;
	}

	public String getPostDate() {
		return postDate;
	}

	public String getPraise() {
		return praise;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public void setTotal(String praise) {
		this.praise = praise;
	}

}
