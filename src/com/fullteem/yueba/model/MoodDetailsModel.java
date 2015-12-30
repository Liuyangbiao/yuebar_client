package com.fullteem.yueba.model;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月14日&emsp;上午8:26:25
 * @use 心情bean
 */
public class MoodDetailsModel extends UserInfoModel {
	private String postDate; // 心情发布时间
	private String praise;// 点赞量

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getPraise() {
		return praise;
	}

	public void setPraise(String praise) {
		this.praise = praise;
	}

}
