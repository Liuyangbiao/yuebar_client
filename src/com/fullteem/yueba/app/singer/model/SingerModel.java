package com.fullteem.yueba.app.singer.model;

import java.io.Serializable;

import android.text.TextUtils;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月13日&emsp;上午9:54:21
 * @use 歌手bean
 */
public class SingerModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int singerId = -1; // 歌手id
	private String singerName; // 歌手名
	private String singerIntroduction;// 歌手简介
	private String singerGender; // 歌手性别
	private String logoUrl;// 歌手头像
	private String userLogoUrl;// 用户头像
	private int charm;// 魅力值
	private String singerAge; // 年龄
	private int pageviews;// 歌手浏览量
	private String praiseTotal; // 点赞量
	private int singerLevel;// 歌手等级,存放图片资源
	private String singerPhone; // 歌手电话
	private int userType; // 用户类型，1为普通用户，2为歌手

	public int getSingerId() {
		return singerId;
	}

	public void setSingerId(int singerId) {
		this.singerId = singerId;
	}

	public String getSingerName() {
		return singerName;
	}

	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}

	public String getSingerIntroduction() {
		return singerIntroduction;
	}

	public void setSingerIntroduction(String singerIntroduction) {
		this.singerIntroduction = singerIntroduction;
	}

	public String getSingerGender() {
		return singerGender;
	}

	public void setSingerGender(String singerGender) {
		this.singerGender = singerGender;
	}

	public String getLogoUrl() {
		// if (TextUtils.isEmpty(logoUrl))
		// return "drawable://" + R.drawable.img_loading_empty;
		//
		// //不包含http://或者ftp://
		// if (!(logoUrl.contains("http://") || logoUrl.contains("ftp://")))
		// return Urls.SERVERHOST + logoUrl;
		//
		// //包含了看下前缀是不是http开头或者ftp
		// String urlHead = logoUrl.substring(0, 7);
		// if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
		// return Urls.SERVERHOST + logoUrl;
		//
		// if (logoUrl.length() <= 7)//错误地址
		// return "drawable://" + R.drawable.img_loading_fail;
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getCharm() {
		return charm + "";
	}

	public void setCharm(int charm) {
		this.charm = charm;
	}

	public String getSingerAge() {
		return singerAge;
	}

	public void setSingerAge(String singerAge) {
		this.singerAge = singerAge;
	}

	public String getPageviews() {
		return pageviews + "";
	}

	/**
	 * 浏览量？还是评论量，服务器返回名字是num，先解析到pageviews存住
	 * 
	 * @param pageviews
	 */
	public void setPageviews(int pageviews) {
		this.pageviews = pageviews;
	}

	public int getSingerLevel() {
		return singerLevel;
	}

	public void setSingerLevel(int singerLevel) {
		this.singerLevel = singerLevel;
	}

	public String getPraiseTotal() {
		return TextUtils.isEmpty(praiseTotal) ? "0" : praiseTotal;
	}

	public void setPraiseTotal(String praiseTotal) {
		this.praiseTotal = praiseTotal;
	}

	public String getSingerPhone() {
		return singerPhone;
	}

	public void setSingerPhone(String singerPhone) {
		this.singerPhone = singerPhone;
	}

	public int getUserType() {
		return userType == 0 ? 2 : userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getUserLogoUrl() {
		return userLogoUrl;
	}

	public void setUserLogoUrl(String userLogoUrl) {
		this.userLogoUrl = userLogoUrl;
	}

}
