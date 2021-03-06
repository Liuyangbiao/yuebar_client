package com.fullteem.yueba.app.singer.model;

public class VideoDetailModel extends VideoModel {
	private String singerStageName;
	private int singerId = -1;
	private boolean isIssue;
	private int pubId = -1; // 驻唱酒吧id
	private String pubName;

	public String getSingerStageName() {
		return singerStageName;
	}

	public int getSingerId() {
		return singerId;
	}

	public boolean isIssue() {
		return isIssue;
	}

	public void setSingerVideoTitle(String singerVideoTitle) {
		setVideoideoTitle(singerVideoTitle);
	}

	public void setSingerStageName(String singerStageName) {
		this.singerStageName = singerStageName;
	}

	public void setSingerId(int singerId) {
		this.singerId = singerId;
	}

	public void setSingerVideoId(int singerVideoId) {
		setVideoId(singerVideoId);
	}

	public void setSingerVideoVideoUrl(String singerVideoVideoUrl) {
		setVideoPlayUri(singerVideoVideoUrl);
	}

	public void setSingerVideoDetail(String singerVideoDetail) {
		setVideoDetail(singerVideoDetail);
	}

	public void setIsIssue(boolean isIssue) {
		this.isIssue = isIssue;
	}

	public int getPubId() {
		return pubId;
	}

	public void setPubId(int pubId) {
		this.pubId = pubId;
	}

	public String getPubName() {
		return pubName;
	}

	/**
	 * 接口返回数据barName解析对应字段
	 * 
	 * @param pubName
	 */
	public void setBarName(String pubName) {
		this.pubName = pubName;
	}

	/**
	 * 接口返回数据barId解析对应字段
	 * 
	 * @param pubName
	 */
	public void setBarId(int barId) {
		this.pubId = barId;
	}

}
