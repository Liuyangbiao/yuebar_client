package com.fullteem.yueba.model;

import java.io.Serializable;
import java.util.List;

import android.text.TextUtils;

import com.fullteem.yueba.util.DisplayUtils;

/**
 * 吧友model
 * 
 * @author ssy
 * 
 */
public class NearbyPubFriendsModel extends CommonModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<PubFriendsModel> result;

	public List<PubFriendsModel> getResult() {
		return result;
	}

	public void setResult(List<PubFriendsModel> result) {
		this.result = result;
	}

	public class PubFriendsModel implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String createDate1;
		private String createDate;
		private String distance;
		private String status;
		private String userId;
		private String userMobile;
		private String userSex;
		private String userName;
		private String lng;
		private String isSinger;
		private String userAccount;
		private String userAsign;
		private String userAge;
		private String lat;
		private String userLogoUrl;
		private String imgUrl;
		private String imServerId;
		private String userToken;

		public String getUserToken() {
			return userToken;
		}

		public void setUserToken(String userToken) {
			this.userToken = userToken;
		}

		public String getImServerId() {
			return imServerId;
		}

		public void setImServerId(String imServerId) {
			this.imServerId = imServerId;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public String getUserLogoUrl() {
			return userLogoUrl;
		}

		public void setUserLogoUrl(String userLogoUrl) {
			this.userLogoUrl = userLogoUrl;
		}

		public String getCreateDate1() {
			return createDate1;
		}

		public void setCreateDate1(String createDate1) {
			if (TextUtils.isEmpty(createDate1)) {
				this.createDate1 = "刚刚";
				return;
			}
			if (createDate1.indexOf("分钟前") >= 1) {
				String createDateint = createDate1.substring(0,
						createDate1.indexOf("分钟前"));
				Integer timeInt = Integer.valueOf(createDateint);
				if (timeInt < 6) // 6分钟内设置为刚刚
				{
					this.createDate1 = "刚刚";
					return;
				}
			}
			this.createDate1 = createDate1;
		}

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

		public String getUserMobile() {
			return userMobile;
		}

		public void setUserMobile(String userMobile) {
			this.userMobile = userMobile;
		}

		public String getUserSex() {
			return userSex;
		}

		public void setUserSex(String userSex) {
			this.userSex = userSex;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getLng() {
			return lng;
		}

		public void setLng(String lng) {
			this.lng = lng;
		}

		public String getIsSinger() {
			return isSinger;
		}

		public void setIsSinger(String isSinger) {
			this.isSinger = isSinger;
		}

		public String getUserAccount() {
			return userAccount;
		}

		public void setUserAccount(String userAccount) {
			this.userAccount = userAccount;
		}

		public String getUserAsign() {
			return userAsign;
		}

		public void setUserAsign(String userAsign) {
			this.userAsign = userAsign;
		}

		public String getUserAge() {
			if (TextUtils.isEmpty(userAge))
				return "0";
			return userAge;
		}

		public void setUserAge(String userAge) {
			this.userAge = userAge;
		}

		public String getLat() {
			return lat;
		}

		public void setLat(String lat) {
			this.lat = lat;
		}

		public String getCreateDate() {
			return createDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}
	}

}
