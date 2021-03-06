package com.fullteem.yueba.model;

import java.io.Serializable;

/**
 * 用户喜欢的音乐
 * 
 * @author ssy
 * 
 */
public class UserMusicListModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String musicStyleName;

	public String getMusicStyleName() {
		return musicStyleName;
	}

	public void setMusicStyleName(String musicStyleName) {
		this.musicStyleName = musicStyleName;
	}

	public String getUserStyleId() {
		return userStyleId;
	}

	public void setUserStyleId(String userStyleId) {
		this.userStyleId = userStyleId;
	}

	public String getUserMusicId() {
		return userMusicId;
	}

	public void setUserMusicId(String userMusicId) {
		this.userMusicId = userMusicId;
	}

	private String userStyleId;
	private String userMusicId;

}
