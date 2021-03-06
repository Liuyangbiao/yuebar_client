package com.fullteem.yueba.model;

import java.io.Serializable;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月8日&emsp;下午4:07:59
 * @use 礼物bean
 */
public class GiftModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String giftLogoUrl; // 礼物头像Url
	private String giftName; // 礼物名称
	private int total; // 禮物总量
	private int gold; // 禮物價格-金幣
	private int score;// 禮物價格-積分
	private int charm; // 路无魅力值
	private int giftId;// id

	// 根据tag来决定当前图片是添加按钮还是相册图片
	private String typeTag;

	public String getTypeTag() {
		return typeTag;
	}

	public void setTypeTag(String typeTag) {
		this.typeTag = typeTag;
	}

	public String getGiftLogoUrl() {
		// if (TextUtils.isEmpty(giftLogoUrl))
		// return "drawable://" + R.drawable.img_loading_empty;
		//
		// //若是以mnt打头则不做变更
		// if(giftLogoUrl.contains("mnt") || giftLogoUrl.contains("storage")){
		// return "file://"+giftLogoUrl;
		// }
		//
		// //不包含http://或者ftp://
		// if (!(giftLogoUrl.contains("http://") ||
		// giftLogoUrl.contains("ftp://")))
		// return Urls.SERVERHOST + giftLogoUrl;
		//
		// //包含了看下前缀是不是http开头或者ftp
		// String urlHead = giftLogoUrl.substring(0, 7);
		// if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
		// return Urls.SERVERHOST + giftLogoUrl;
		//
		// if (giftLogoUrl.length() <= 7)//错误地址
		// return "drawable://" + R.drawable.img_loading_fail;
		return giftLogoUrl;
	}

	public String getGiftName() {
		return giftName;
	}

	public int getTotal() {
		return total;
	}

	public int getGold() {
		return gold;
	}

	public int getScore() {
		return score;
	}

	public void setGiftlogoUrl(String giftLogoUrl) {
		this.giftLogoUrl = giftLogoUrl;
	}

	public void setGiftLogoUrl(String giftLogoUrl) {
		this.giftLogoUrl = giftLogoUrl;
	}

	// 服务器返回的字段变化
	public void setUserPhotoImgUrl(String giftLogoUrl) {
		this.giftLogoUrl = giftLogoUrl;
	}

	public void setUserGroupImgUrl(String userGroupImgUrl) {
		this.giftLogoUrl = userGroupImgUrl;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public void setGiftGoldNum(int gold) {
		this.gold = gold;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getCharm() {
		return charm;
	}

	public int getGiftId() {
		return giftId;
	}

	public void setCharm(int charm) {
		this.charm = charm;
	}

	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof GiftModel))
			return false;
		final GiftModel other = (GiftModel) o;

		if (this.giftName.equals(other.getGiftName())
				&& this.giftId == other.getGiftId())
			return true;
		else
			return false;
	}

}
