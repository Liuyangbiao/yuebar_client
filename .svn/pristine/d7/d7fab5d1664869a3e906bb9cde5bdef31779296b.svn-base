package com.fullteem.yueba.model.presentmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import android.view.View;

import com.fullteem.yueba.app.singer.model.SingerDetailModel;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月13日&emsp;上午9:54:21
 * @use 歌手视频列表试图管理Model
 */
@PresentationModel
public class SingerVideoListPresentModel implements
		HasPresentationModelChangeSupport {
	private PresentationModelChangeSupport presentationModelChangeSupport;
	private SingerDetailModel singer;
	private String userMood;// 心情记录，查询的列表第一条传过来即可
	private int albumNoneVisibility;
	private int albumVisibility;
	private int videoVisibility;
	private int giftVisibility;
	private int moodNoneVisibility;
	private int moodVisibility;

	public SingerVideoListPresentModel(SingerDetailModel singer) {
		this.albumNoneVisibility = View.GONE;
		this.albumVisibility = View.VISIBLE;
		this.videoVisibility = View.VISIBLE;
		this.giftVisibility = View.VISIBLE;
		this.moodVisibility = View.VISIBLE;
		this.moodNoneVisibility = View.GONE;
		presentationModelChangeSupport = new PresentationModelChangeSupport(
				this);
		setModel(singer);
	}

	public void setModel(SingerDetailModel singer) {
		this.singer = singer;
		presentationModelChangeSupport.firePropertyChange("userNickname");
		presentationModelChangeSupport.firePropertyChange("userCharm");
		presentationModelChangeSupport.firePropertyChange("userAge");
		presentationModelChangeSupport.firePropertyChange("userViews");
		presentationModelChangeSupport
				.firePropertyChange("userPersonalitySignature");
		presentationModelChangeSupport.firePropertyChange("singerIntroduction");
	}

	public String getUserNickname() {
		return singer.getSingerName();
	}

	public void setUserNickname(String userNickname) {
		singer.setSingerName(userNickname);
		presentationModelChangeSupport.firePropertyChange("userNickname");
	}

	public String getUserCharm() {
		return singer.getCharm();
	}

	public String getUserAge() {
		return singer.getSingerAge();
	}

	public String getUserViews() {
		return singer.getPageviews();
	}

	public String getUserPersonalitySignature() { // 个性签名
		return singer.getSingerIntroduction();
	}

	public String getUserMood() {
		return userMood;
	}

	public void setUserMood(String userMood) {
		this.userMood = userMood;
		presentationModelChangeSupport.firePropertyChange("userMood");
	}

	public String getUserGender() {
		return singer.getSingerGender();
	}

	public void setUserGender(String userGender) {
		singer.setSingerGender(userGender);
		presentationModelChangeSupport.firePropertyChange("userGender");
	}

	public String getSingerIntroduction() {
		return singer.getSingerIntroduction();
	}

	public void setSingerIntroduction(String singerIntroduction) {
		singer.setSingerIntroduction(singerIntroduction);
		presentationModelChangeSupport.firePropertyChange("singerIntroduction");
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
		presentationModelChangeSupport
				.firePropertyChange("albumNoneVisibility");
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
		presentationModelChangeSupport.firePropertyChange("albumVisibility");
	}

	public int getVideoVisibility() {
		return videoVisibility;
	}

	/**
	 * 如果歌手没有视频列表可设为View.GONE
	 * 
	 * @param videoVisibility
	 */
	public void setVideoVisibility(int videoVisibility) {
		this.videoVisibility = videoVisibility;
		presentationModelChangeSupport.firePropertyChange("videoVisibility");
	}

	public int getGiftVisibility() {
		return giftVisibility;
	}

	/**
	 * 如果歌手没有礼品列表可设为View.GONE
	 * 
	 * @param giftVisibility
	 */
	public void setGiftVisibility(int giftVisibility) {
		this.giftVisibility = giftVisibility;
		presentationModelChangeSupport.firePropertyChange("giftVisibility");
	}

	public int getMoodVisibility() {
		return moodVisibility;
	}

	private void setMoodVisibility(int moodVisibility) {
		this.moodVisibility = moodVisibility;
		presentationModelChangeSupport.firePropertyChange("moodVisibility");
	}

	public int getMoodNoneVisibility() {
		return moodNoneVisibility;
	}

	public void setMoodNoneVisibility(int moodNoneVisibility) {
		this.moodNoneVisibility = moodNoneVisibility;
		presentationModelChangeSupport.firePropertyChange("moodNoneVisibility");
		if (moodNoneVisibility == View.GONE) { // 不显示“暂无图片内容”，则显示相册的布局
			setMoodVisibility(View.VISIBLE);
		} else {
			setMoodVisibility(View.GONE);
		}
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return presentationModelChangeSupport;
	}

}
