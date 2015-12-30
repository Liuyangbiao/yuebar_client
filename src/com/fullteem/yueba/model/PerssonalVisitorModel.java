package com.fullteem.yueba.model;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月11日&emsp;下午9:54:21
 * @use 详细资料页面访客 bean
 */
public class PerssonalVisitorModel {
	private String visitorHeader; // 访客头像
	private String visitorNickname;// 访客昵称

	public PerssonalVisitorModel() {
	}

	public PerssonalVisitorModel(String visitorHeader, String visitorNickname) {
		this.visitorHeader = visitorHeader;
		this.visitorNickname = visitorNickname;
	}

	public String getVisitorHeader() {
		// if (TextUtils.isEmpty(visitorHeader))
		// return "drawable://" + R.drawable.img_loading_empty;
		//
		// //不包含http://或者ftp://
		// if (!(visitorHeader.contains("http://") ||
		// visitorHeader.contains("ftp://")))
		// return Urls.SERVERHOST + visitorHeader;
		//
		// //包含了看下前缀是不是http开头或者ftp
		// String urlHead = visitorHeader.substring(0, 7);
		// if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
		// return Urls.SERVERHOST + visitorHeader;
		//
		// if (visitorHeader.length() <= 7)//错误地址
		// return "drawable://" + R.drawable.img_loading_fail;
		return visitorHeader;
	}

	public void setVisitorHeader(String visitorHeader) {
		this.visitorHeader = visitorHeader;
	}

	public String getVisitorNickname() {
		return visitorNickname;
	}

	public void setVisitorNickname(String visitorNickname) {
		this.visitorNickname = visitorNickname;
	}

}
