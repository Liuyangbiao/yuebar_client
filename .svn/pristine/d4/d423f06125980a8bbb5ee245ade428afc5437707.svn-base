package com.fullteem.yueba.model;

import java.util.List;

import android.text.TextUtils;

import com.fullteem.yueba.util.DisplayUtils;

public class GroupsModel extends CommonModel {
	private List<Groups> result;

	public List<Groups> getResult() {
		return result;
	}

	public void setResult(List<Groups> result) {
		this.result = result;
	}

	public class Groups {
		private int total;
		private String groupId;
		private int num;
		private String groupName;
		private String host;
		private String groupIcon;
		private String groupDesc;
		private String status;
		private String userId;
		private String userGroupId;
		private String distance;
		private String lng;
		private String lat;

		public String getDistance() {
			return distance;
		}

		public void setDistance(String distance) {
			if (TextUtils.isEmpty(distance)) {
				this.distance = "100米以内";
				return;
			}
			Float disflo = Float.valueOf(distance);
			if (disflo < 0.1) {
				this.distance = "100米以内";
				return;
			}
			if (disflo < 0.5) {
				this.distance = "500米以内";
				return;
			}
			if (disflo < 0.8) {
				this.distance = "800米以内";
				return;
			}
			if (disflo < 1) {
				this.distance = "1000米以内";
				return;
			}
			this.distance = DisplayUtils.subZeroAndDot(distance) + "KM";
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

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
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

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getUserGroupId() {
			return userGroupId;
		}

		public void setUserGroupId(String userGroupId) {
			this.userGroupId = userGroupId;
		}

	}
}
