package com.fullteem.yueba.model;

import java.util.List;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月9日&emsp;下午6:15:48
 * @use 用户信息bean,getInstance()则获取的是当前登录的用户
 */
public class UserInfoModel {
	private static UserInfoModel instance;

	public static synchronized UserInfoModel getInstance() {
		if (instance == null) {
			instance = new UserInfoModel();
			// TODO 从数据库读取当前灯枯用户的信息
		}
		return instance;
	}

	/**
	 * 保存当前登录的用户信息
	 * 
	 * @param userInfoModel
	 */
	public static synchronized void saveUserInfo(UserInfoModel userInfoModel) {
		// TODO 保存到数据库
	}

	private int userId;
	private String userNickname; // 用户昵称
	private byte[] userHeader;// 用户头像
	private String userScore;// 用户积分
	private String userGold;// 用户金币
	private String userAccount;// 用户帐号
	private String userCharm;// 用户魅力值
	private String userGender; // 用户性别
	private String userAge;// 用户年龄
	private String userViews;// 用户浏览量
	private String userLevel;// 用户等级
	private String userPersonalitySignature;// 用户个性签名
	private String userDateOfBirth; // 用户出生日期
	private String userConstellation;// 用户星座
	private String userIndustry;// 用户行业
	private String userHobby;// 用户爱好
	private String userOftenPub;// 用户经常出没酒吧
	private String userFavoriteWine;// 用户喜欢的酒水
	private String userFavoriteMusicStyle;// 用户喜欢的音乐风格
	private boolean userIsHeart;// 用户是否已关注
	private String userMood;// 用户心情记录（最新一条）
	private String userHeaderUrl;// 用户头像Url
	private String userMoodHeader;// 用户心情记录头像地址
	private List<String> userAlbum;// 用户相册图片链接集合
	private String userSign;// 用户签名

	public String getUserSign() {
		return userSign;
	}

	public void setUserSign(String userSign) {
		this.userSign = userSign;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public byte[] getUserHeader() {
		return userHeader;
	}

	public void setUserHeader(byte[] userHeader) {
		this.userHeader = userHeader;
	}

	public String getUserScore() {
		return userScore;
	}

	public void setUserScore(String userScore) {
		this.userScore = userScore;
	}

	public String getUserGold() {
		return userGold;
	}

	public void setUserGold(String userGold) {
		this.userGold = userGold;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserCharm() {
		return userCharm;
	}

	public void setUserCharm(String userCharm) {
		this.userCharm = userCharm;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserViews() {
		return userViews;
	}

	public void setUserViews(String userViews) {
		this.userViews = userViews;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getUserPersonalitySignature() {
		return userPersonalitySignature;
	}

	public void setUserPersonalitySignature(String userPersonalitySignature) {
		this.userPersonalitySignature = userPersonalitySignature;
	}

	public String getUserDateOfBirth() {
		return userDateOfBirth;
	}

	public void setUserDateOfBirth(String userDateOfBirth) {
		this.userDateOfBirth = userDateOfBirth;
	}

	public String getUserConstellation() {
		return userConstellation;
	}

	public void setUserConstellation(String userConstellation) {
		this.userConstellation = userConstellation;
	}

	public String getUserIndustry() {
		return userIndustry;
	}

	public void setUserIndustry(String userIndustry) {
		this.userIndustry = userIndustry;
	}

	public String getUserHobby() {
		return userHobby;
	}

	public void setUserHobby(String userHobby) {
		this.userHobby = userHobby;
	}

	public String getUserOftenPub() {
		return userOftenPub;
	}

	public void setUserOftenPub(String userOftenPub) {
		this.userOftenPub = userOftenPub;
	}

	public String getUserFavoriteWine() {
		return userFavoriteWine;
	}

	public void setUserFavoriteWine(String userFavoriteWine) {
		this.userFavoriteWine = userFavoriteWine;
	}

	public String getUserFavoriteMusicStyle() {
		return userFavoriteMusicStyle;
	}

	public void setUserFavoriteMusicStyle(String userFavoriteMusicStyle) {
		this.userFavoriteMusicStyle = userFavoriteMusicStyle;
	}

	public boolean isUserIsHeart() {
		return userIsHeart;
	}

	public String getUserMood() {
		return userMood;
	}

	public void setUserMood(String userMood) {
		this.userMood = userMood;
	}

	public String getUserHeaderUrl() {
		// if (TextUtils.isEmpty(userHeaderUrl))
		// return "drawable://" + R.drawable.img_loading_empty;
		//
		// // 不包含http://或者ftp://
		// if (!(userHeaderUrl.contains("http://") ||
		// userHeaderUrl.contains("ftp://")))
		// return Urls.SERVERHOST + userHeaderUrl;
		//
		// // 包含了看下前缀是不是http开头或者ftp
		// String urlHead = userHeaderUrl.substring(0, 7);
		// if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
		// return Urls.SERVERHOST + userHeaderUrl;
		//
		// if (userHeaderUrl.length() <= 7)// 错误地址
		// return "drawable://" + R.drawable.img_loading_fail;
		return userHeaderUrl;
	}

	public UserInfoModel setUserHeader(String userHeader) {
		this.userHeaderUrl = userHeader;
		return this;
	}

	public void setUserIsHeart(boolean userIsHeart) {
		this.userIsHeart = userIsHeart;
	}

	public String getUserMoodHeader() {
		return userMoodHeader;
	}

	public void setUserMoodHeader(String userMoodHeader) {
		this.userMoodHeader = userMoodHeader;
	}

	public List<String> getUserAlbum() {
		return userAlbum;
	}

	public void setUserAlbum(List<String> userAlbum) {
		this.userAlbum = userAlbum;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
