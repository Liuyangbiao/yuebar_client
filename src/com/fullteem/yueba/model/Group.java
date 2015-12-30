package com.fullteem.yueba.model;

public class Group {

	private String total;
	private String groupId;
	private int num;
	private String groupName;
	private String host;
	private String groupIcon;
	private String groupDesc; // 简介
	private String status;
	private int userId = -1;
	private String userGroupId;
	private String distance;
	private String lng;
	private String lat;
	private String signature;// 群签名
	private String location;// 群地点
	private String harmastName;// 群主昵称
	private String harmastLogoUrl;// 群主头像
	private boolean isInGroup;// 是否在群组里面

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getGroupIcon() {
		return groupIcon;
	}

	public void setGroupIcon(String groupIcon) {
		this.groupIcon = groupIcon;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setGroupLocale(String location) {
		this.location = location;
	}

	public String getHarmastName() {
		return harmastName;
	}

	public String getHarmastLogoUrl() {
		return harmastLogoUrl;
	}

	public void setHarmastName(String harmastName) {
		this.harmastName = harmastName;
	}

	public void setUserName(String harmastName) {
		this.harmastName = harmastName;
	}

	public void setHarmastLogoUrl(String harmastLogoUrl) {
		this.harmastLogoUrl = harmastLogoUrl;
	}

	public void setUserLogoUrl(String harmastLogoUrl) {
		this.harmastLogoUrl = harmastLogoUrl;
	}

	public boolean isInGroup() {
		return isInGroup;
	}

	public void setIsInGroup(boolean isInGroup) {
		this.isInGroup = isInGroup;
	}

}
