package com.fullteem.yueba.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月18日&emsp;上午10:26:24
 * @use 酒吧bean
 */
public class PubModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String barAddress; // 酒吧地点
	private boolean isFollow; // 是否关注
	private boolean status;
	private String barLinkPhone; // 联系电话
	private float star;// 酒吧星级
	private String barLogoUrl;
	private String lastDate;
	private String dictName; // 酒吧描述
	private boolean isSingingBar; // 是否有驻唱歌手
	private String barIntroduction; // 酒吧简介
	private int barId; // 酒吧id
	private String barTelephone;
	private String barReadNum;
	private String dictCode;
	private int barCode;
	private int aa;
	private String barName;
	private String barLinkMan;
	private String createDate;
	private int barType; // 酒吧类型，1、清吧；2迪吧、3演绎
	private boolean isCoupon; // 是否团购
	private boolean isReservation;// 是否订座
	private boolean isPromotion;// 是否促销
	private int praise;// 点赞量
	private boolean isPraise;// 是否
	private int comments;// 评论量
	private int onOff;//酒吧支付状态 0  可支付  1 不能支付
	
	public int getOnOff() {
		return onOff;
	}

	public void setOnOff(int onOff) {
		this.onOff = onOff;
	}

	private List<BarDynamicModel> barDynamicList; // 酒吧动态列表

	public String getBarAddress() {
		return barAddress;
	}

	public void setBarAddress(String inbarAddress) {
		this.barAddress = inbarAddress;
	}

	public boolean isFollow() {
		return isFollow;
	}

	public void setIsFollow(boolean isFollow) {
		this.isFollow = isFollow;
	}

	public boolean isStatus() {
		return status;
	}

	public void setIsStatus(boolean status) {
		this.status = status;
	}

	public String getBarLinkPhone() {
		return barLinkPhone;
	}

	public void setBarLinkPhone(String barLinkPhone) {
		this.barLinkPhone = barLinkPhone;
	}

	public float getStar() {
		return star;
	}

	public void setStar(float star) {
		this.star = star;
	}

	public String getBarLogoUrl() {
		// if (TextUtils.isEmpty(barLogoUrl))
		// return "drawable://" + R.drawable.img_loading_empty;

		// // 不包含http://或者ftp://
		// if (!(barLogoUrl.contains("http://") ||
		// barLogoUrl.contains("ftp://")))
		// return Urls.SERVERHOST + barLogoUrl;
		//
		// // 包含了看下前缀是不是http开头或者ftp
		// String urlHead = barLogoUrl.substring(0, 7);
		// if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
		// return Urls.SERVERHOST + barLogoUrl;
		//
		// if (barLogoUrl.length() <= 7)// 错误地址
		// return "drawable://" + R.drawable.img_loading_fail;
		return barLogoUrl;
	}

	public void setBarLogoUrl(String barLogoUrl) {
		this.barLogoUrl = barLogoUrl;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getBarIntroduction() {
		return barIntroduction;
	}

	public void setBarIntroduction(String barIntroduction) {
		this.barIntroduction = barIntroduction;
	}

	public boolean isSingingBar() {
		return isSingingBar;
	}

	public void setIsSingingBar(boolean isSingingBar) {
		this.isSingingBar = isSingingBar;
	}

	public int getBarId() {
		return barId;
	}

	public void setBarId(int barId) {
		this.barId = barId;
	}

	public String getBarTelephone() {
		return barTelephone;
	}

	public void setBarTelephone(String barTelephone) {
		this.barTelephone = barTelephone;
	}

	public void setTelephone(String barTelephone) {
		this.barTelephone = barTelephone;
	}

	public String getBarReadNum() {
		return barReadNum;
	}

	public void setBarReadNum(String barReadNum) {
		this.barReadNum = barReadNum;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public int getBarCode() {
		return barCode;
	}

	public void setBarCode(int barCode) {
		this.barCode = barCode;
	}

	public int getAa() {
		return aa;
	}

	public void setAa(int aa) {
		this.aa = aa;
	}

	public String getBarName() {
		return barName;
	}

	public void setBarName(String barName) {
		this.barName = barName;
	}

	public String getBarLinkMan() {
		return barLinkMan;
	}

	public void setBarLinkMan(String barLinkMan) {
		this.barLinkMan = barLinkMan;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getBarType() {
		return barType;
	}

	public void setBarType(int barType) {
		this.barType = barType;
	}

	public void setBarTypeId(int barType) {
		this.barType = barType;
	}

	public boolean isCoupon() {
		return isCoupon;
	}

	public void setIsCoupon(boolean isCoupon) {
		this.isCoupon = isCoupon;
	}

	public boolean isReservation() {
		return isReservation;
	}

	public boolean isPromotion() {
		return isPromotion;
	}

	public void setIsReservation(boolean isReservation) {
		this.isReservation = isReservation;
	}

	public void setIsPromotion(boolean isPromotion) {
		this.isPromotion = isPromotion;
	}

	public int getPraise() {
		return praise;
	}

	public int getComments() {
		return comments;
	}

	public void setPraise(int praise) {
		this.praise = praise;
	}

	public void setTotal(int praise) {
		this.praise = praise;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public boolean isPraise() {
		return isPraise;
	}

	public void setPraise(boolean isPraise) {
		this.isPraise = isPraise;
	}

	public List<BarDynamicModel> getBarDynamicList() {
		return barDynamicList;
	}

	public void setBarDynamicList(List<BarDynamicModel> barDynamicList) {
		this.barDynamicList = barDynamicList;
	}

}
