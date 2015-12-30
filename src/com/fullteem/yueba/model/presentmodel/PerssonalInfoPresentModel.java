package com.fullteem.yueba.model.presentmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import android.text.TextUtils;
import android.view.View;

import com.fullteem.yueba.model.UserInfoModel;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月11日&emsp;下午2:02:43
 * @use 资料管理Model
 */
@PresentationModel
public class PerssonalInfoPresentModel implements
		HasPresentationModelChangeSupport {
	private PresentationModelChangeSupport changeSupport;
	private UserInfoModel userInfo;
	private int giftVisibility, visitorVisibility; // 礼品列表、访客可视情况
	private int albumNoneVisibility;// 没有相册内容提示可视情况
	private int albumVisibility;// 相册内容可视情况

	public PerssonalInfoPresentModel(UserInfoModel userInfo) {
		this.userInfo = userInfo;
		this.giftVisibility = View.VISIBLE;
		this.visitorVisibility = View.VISIBLE;
		this.albumVisibility = View.VISIBLE;
		this.albumNoneVisibility = View.GONE;
		changeSupport = new PresentationModelChangeSupport(this);
	}

	public String getUserSign() {
		return userInfo.getUserSign();
	}

	public void setUserSign(String userSign) {
		userInfo.setUserSign(userSign);
		changeSupport.firePropertyChange("userSign");
	}

	public String getUserNickname() {
		return userInfo.getUserNickname();
	}

	public void setUserNickname(String userNickname) {
		userInfo.setUserNickname(userNickname);
		changeSupport.firePropertyChange("userNickname");
	}

	public String getUserCharm() {
		return userInfo.getUserCharm();
	}

	public void setUserCharm(String charm) {
		userInfo.setUserCharm(charm);
		changeSupport.firePropertyChange("charm");
	}

	public String getUserAge() {
		return userInfo.getUserAge();
	}

	public void setUserAge(String userAge) {
		userInfo.setUserAge(userAge);
		changeSupport.firePropertyChange("userAge");
	}

	public String getUserViews() {
		return userInfo.getUserViews();
	}

	public void setUserViews(String userViews) {
		userInfo.setUserViews(userViews);
		changeSupport.firePropertyChange("userViews");
	}

	public String getUserPersonalitySignature() {
		return TextUtils.isEmpty(userInfo.getUserPersonalitySignature()) ? "<编辑状态下点此编辑个性签名>"
				: userInfo.getUserPersonalitySignature();
	}

	public void setUserPersonalitySignature(String userPersonalitySignature) {
		userInfo.setUserPersonalitySignature(userPersonalitySignature);
		changeSupport.firePropertyChange("userPersonalitySignature");
	}

	public String getUserMood() {
		return userInfo.getUserMood();
	}

	public void setUserMood(String userMood) {
		userInfo.setUserMood(userMood);
		changeSupport.firePropertyChange("userMood");
	}

	public String getUserAccount() {
		return userInfo.getUserAccount();
	}

	public void setUserAccount(String userAccount) {
		userInfo.setUserAccount(userAccount);
		changeSupport.firePropertyChange("userAccount");
	}

	public String getUserDateOfBirth() {
		return userInfo.getUserDateOfBirth();
	}

	public void setUserDateOfBirth(String userDateOfBirth) {
		userInfo.setUserDateOfBirth(userDateOfBirth);
		changeSupport.firePropertyChange("userDateOfBirth");
	}

	public String getUserConstellation() {
		return userInfo.getUserConstellation();
	}

	public void setUserConstellation(String userConstellation) {
		userInfo.setUserConstellation(userConstellation);
		changeSupport.firePropertyChange("userConstellation");
	}

	public String getUserGender() {
		return userInfo.getUserGender();
	}

	public void setUserGender(String userGender) {
		userInfo.setUserGender(userGender);
		changeSupport.firePropertyChange("userGender");
	}

	public String getUserIndustry() {
		return userInfo.getUserIndustry();
	}

	public void setUserIndustry(String userIndustry) {
		userInfo.setUserIndustry(userIndustry);
		changeSupport.firePropertyChange("userIndustry");
	}

	public String getUserHobby() {
		return userInfo.getUserHobby();
	}

	public void setUserHobby(String userHobby) {
		userInfo.setUserHobby(userHobby);
		changeSupport.firePropertyChange("userHobby");
	}

	public String getUserOftenPub() {
		return userInfo.getUserOftenPub();
	}

	public void setUserOftenPub(String userOftenPub) {
		userInfo.setUserOftenPub(userOftenPub);
		changeSupport.firePropertyChange("userOftenPub");
	}

	public String getUserFavoriteWine() {
		return userInfo.getUserFavoriteWine();
	}

	public void setUserFavoriteWine(String userFavoriteWine) {
		userInfo.setUserFavoriteWine(userFavoriteWine);
		changeSupport.firePropertyChange("userFavoriteWine");
	}

	public String getUserFavoriteMusicStyle() {
		return userInfo.getUserFavoriteMusicStyle();
	}

	public void setUserFavoriteMusicStyle(String userFavoriteMusicStyle) {
		userInfo.setUserFavoriteMusicStyle(userFavoriteMusicStyle);
		changeSupport.firePropertyChange("userFavoriteMusicStyle");
	}

	public UserInfoModel getModel() {
		return userInfo;
	}

	public int getGiftVisibility() {
		return giftVisibility;
	}

	/**
	 * 设置礼品列表布局可视情况：如该用户没有礼品则可传View.GONE
	 * 
	 * @param giftVisibility
	 */
	public void setGiftVisibility(int giftVisibility) {
		this.giftVisibility = giftVisibility;
		changeSupport.firePropertyChange("giftVisibility");
	}

	public int getVisitorVisibility() {
		return visitorVisibility;
	}

	/**
	 * 设置访客布局可视情况：如该用户没有访客则可传View.GONE
	 * 
	 * @param visitorVisibility
	 */
	public void setVisitorVisibility(int visitorVisibility) {
		this.visitorVisibility = visitorVisibility;
		changeSupport.firePropertyChange("visitorVisibility");
	}

	public int getAlbumNoneVisibility() {
		return albumNoneVisibility;
	}

	/**
	 * 如果相册没有图片可展示，则传VIew.VISIBLE即可
	 * 
	 * @param albumNoneVisibility
	 */
	public void setAlbumNoneVisibility(int albumNoneVisibility) {
		this.albumNoneVisibility = albumNoneVisibility;
		changeSupport.firePropertyChange("albumNoneVisibility");
		if (albumNoneVisibility == View.GONE) { // 不显示“暂无图片内容”，则显示相册的布局
			setAlbumVisibility(View.VISIBLE);
		} else {
			setAlbumVisibility(View.GONE);
		}
	}

	public int getAlbumVisibility() {
		return albumVisibility;
	}

	private void setAlbumVisibility(int albumVisibility) {
		this.albumVisibility = albumVisibility;
		changeSupport.firePropertyChange("albumVisibility");
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return changeSupport;
	}

}
