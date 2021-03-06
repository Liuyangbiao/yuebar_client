package com.fullteem.yueba.model;

import java.io.Serializable;

/**
 * 动态model基类
 * 
 * @author jun
 */
public class DynamicModel implements Serializable {
	private static final long serialVersionUID = 8421L;
	private int dynamicId;
	private String dynamicContent;
	private boolean isPraise;
	private int praiseNum;// 点赞数
	private int commentNum;// 评论数
	private String createDate;

	public int getDynamicId() {
		return dynamicId;
	}

	public String getDynamicContent() {
		return dynamicContent;
	}

	public boolean isPraise() {
		return isPraise;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setDynamicId(int dynamicId) {
		this.dynamicId = dynamicId;
	}

	public void setDynamicContent(String dynamicContent) {
		this.dynamicContent = dynamicContent;
	}

	public void setPraise(boolean isPraise) {
		this.isPraise = isPraise;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

}
