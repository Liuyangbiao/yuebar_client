package com.fullteem.yueba.model;

import java.io.Serializable;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月13日&emsp;上午9:54:21
 * @use 酒吧套餐bean
 */
public class PubPackageModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String barCouponDetail;
	private String barCouponPrice;
	private String barCouponName;
	private String barCouponId;
	private String barId;
	private String barCouponImgUrl;

	public String getBarCouponPrice() {
		return barCouponPrice;
	}

	public void setBarCouponPrice(String barCouponPrice) {
		this.barCouponPrice = barCouponPrice;
	}

	public String getBarCouponId() {
		return barCouponId;
	}

	public void setBarCouponId(String barCouponId) {
		this.barCouponId = barCouponId;
	}

	public String getBarId() {
		return barId;
	}

	public void setBarId(String barId) {
		this.barId = barId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getBarCouponDetail() {
		return barCouponDetail;
	}

	public void setBarCouponDetail(String barCouponDetail) {
		this.barCouponDetail = barCouponDetail;
	}

	public String getBarCouponName() {
		return barCouponName;
	}

	public void setBarCouponName(String barCouponName) {
		this.barCouponName = barCouponName;
	}

	public String getBarCouponImgUrl() {
		return barCouponImgUrl;
	}

	public void setBarCouponImgUrl(String barCouponImgUrl) {
		this.barCouponImgUrl = barCouponImgUrl;
	}

	@Override
	public String toString() {
		return "PubPackageModel [barCouponDetail=" + barCouponDetail
				+ ", barCouponPrice=" + barCouponPrice + ", barCouponName="
				+ barCouponName + ", barCouponId=" + barCouponId + ", barId="
				+ barId + ", barCouponImgUrl=" + barCouponImgUrl + "]";
	}

}
