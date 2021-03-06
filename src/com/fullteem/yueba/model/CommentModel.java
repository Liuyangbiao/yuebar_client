package com.fullteem.yueba.model;

import java.io.Serializable;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月22日&emsp;下午6:31:21
 * @use 回复bean
 */
public class CommentModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int dynamicCommentType; // 1表评论 2 表回复
	private int dynamicCommentId; // 动态id
	private int commentId; // 回复id
	private String commentommentContent; // 回复的内容

	public int getDynamicCommentType() {
		return dynamicCommentType;
	}

	public int getDynamicCommentId() {
		return dynamicCommentId;
	}

	public int getCommentId() {
		return commentId;
	}

	public String getCommentommentContent() {
		return commentommentContent;
	}

	public void setDynamicCommentType(int dynamicCommentType) {
		this.dynamicCommentType = dynamicCommentType;
	}

	public void setDynamicCommentId(int dynamicCommentId) {
		this.dynamicCommentId = dynamicCommentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public void setCommentommentContent(String commentommentContent) {
		this.commentommentContent = commentommentContent;
	}

}
