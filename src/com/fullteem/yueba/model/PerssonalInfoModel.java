package com.fullteem.yueba.model;

import java.io.Serializable;
import java.util.List;

public class PerssonalInfoModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String total;
	private String num;
	private String userLogNum;
	private List<UserHobbyListModel> userHobbyList;
	private List<UserMoodeRecordListModel> userMoodeRecordList;
	private List<UserMusicListModel> userMusicList;
	private UserMapModel userMap;
	private List<UserIndustryListModel> userIndustryList;
	private List<AlbumModel> userPhototList;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getUserLogNum() {
		return userLogNum;
	}

	public void setUserLogNum(String userLogNum) {
		this.userLogNum = userLogNum;
	}

	public List<UserHobbyListModel> getUserHobbyList() {
		return userHobbyList;
	}

	public void setUserHobbyList(List<UserHobbyListModel> userHobbyList) {
		this.userHobbyList = userHobbyList;
	}

	public List<UserMoodeRecordListModel> getUserMoodeRecordList() {
		return userMoodeRecordList;
	}

	public void setUserMoodeRecordList(
			List<UserMoodeRecordListModel> userMoodeRecordList) {
		this.userMoodeRecordList = userMoodeRecordList;
	}

	public List<UserMusicListModel> getUserMusicList() {
		return userMusicList;
	}

	public void setUserMusicList(List<UserMusicListModel> userMusicList) {
		this.userMusicList = userMusicList;
	}

	public UserMapModel getUserMap() {
		return userMap;
	}

	public void setUserMap(UserMapModel userMap) {
		this.userMap = userMap;
	}

	public List<UserIndustryListModel> getUserIndustryList() {
		return userIndustryList;
	}

	public void setUserIndustryList(List<UserIndustryListModel> userIndustryList) {
		this.userIndustryList = userIndustryList;
	}

	public List<AlbumModel> getUserPhototList() {
		return userPhototList;
	}

	public void setUserPhototList(List<AlbumModel> userPhototList) {
		this.userPhototList = userPhototList;
	}

}
