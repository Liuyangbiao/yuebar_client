package com.fullteem.yueba.app.singer.model;

import java.io.Serializable;

public class VideoModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String videoDetail; // 视频简介,详情
	private String videoPlayUri; // 播放地址
	private String videoLogoUrl; // logo 地址
	private String videoTitle; // 视频标题
	private int videoId = -1; // id

	public String getVideoDetail() {
		return videoDetail;
	}

	public String getVideoPlayUri() {
		return videoPlayUri;
	}

	public String getVideoLogoUrl() {
		// if (TextUtils.isEmpty(videoLogoUrl))
		// return "drawable://" + R.drawable.img_loading_empty_big;
		//
		// //不包含http://或者ftp://
		// if (!(videoLogoUrl.contains("http://") ||
		// videoLogoUrl.contains("ftp://")))
		// return Urls.SERVERHOST + videoLogoUrl;
		//
		// //包含了看下前缀是不是http开头或者ftp
		// String urlHead = videoLogoUrl.substring(0, 7);
		// if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
		// return Urls.SERVERHOST + videoLogoUrl;
		//
		// if (videoLogoUrl.length() <= 7)//错误地址
		// return "drawable://" + R.drawable.img_loading_fail_big;
		return videoLogoUrl;
	}

	public String getVideoideoTitle() {
		return videoTitle;
	}

	public int getVideoId() {
		return videoId;
	}

	public void setVideoDetail(String videoDetail) {
		this.videoDetail = videoDetail;
	}

	public void setVideoPlayUri(String videoPlayUri) {
		this.videoPlayUri = videoPlayUri;
	}

	public void setVideoLogoUrl(String videoLogoUrl) {
		this.videoLogoUrl = videoLogoUrl;
	}

	public void setSingerPicUrl(String videoLogoUrl) {
		this.videoLogoUrl = videoLogoUrl;
	}

	public void setVideoideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public void setVideoId(int videoId) {
		this.videoId = videoId;
	}

}
