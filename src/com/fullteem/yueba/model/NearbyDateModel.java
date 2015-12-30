package com.fullteem.yueba.model;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月1日&emsp;下午4:22:25
 * @use 附近约会bean
 */
public class NearbyDateModel {

	private String dateTitle;
	private String pubName;
	private String dateTime;
	private int attendPeoples;
	private int involved;
	private String expect;
	private String pubHeader;
	private String sponsor;
	private String sponsorHeader;

	public NearbyDateModel() {

	}

	/**
	 * @param dateTitle
	 *            约会标题
	 * @param pubName
	 *            酒吧名称
	 * @param dateTime
	 *            约会时间
	 * @param attendPeoples
	 *            参与人数
	 * @param involved
	 *            已参与人数
	 * @param expect
	 *            期望
	 * @param pubHeader
	 *            酒吧头像
	 * @param sponsor
	 *            发起人昵称
	 * @param sponsorHeader
	 *            发起人头像
	 */
	public NearbyDateModel(String dateTitle, String pubName, String dateTime,
			int attendPeoples, int involved, String expect, String pubHeader,
			String sponsor, String sponsorHeader) {
		super();
		this.dateTitle = dateTitle;
		this.pubName = pubName;
		this.dateTime = dateTime;
		this.attendPeoples = attendPeoples;
		this.involved = involved;
		this.expect = expect;
		this.pubHeader = pubHeader;
		this.sponsor = sponsor;
		this.sponsorHeader = sponsorHeader;
	}

	public String getPubHeader() {
		// if (TextUtils.isEmpty(pubHeader))
		// return "drawable://" + R.drawable.img_loading_empty;
		//
		// //不包含http://或者ftp://
		// if (!(pubHeader.contains("http://") || pubHeader.contains("ftp://")))
		// return Urls.SERVERHOST + pubHeader;
		//
		// //包含了看下前缀是不是http开头或者ftp
		// String urlHead = pubHeader.substring(0, 7);
		// if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
		// return Urls.SERVERHOST + pubHeader;
		//
		// if (pubHeader.length() <= 7)//错误地址
		// return "drawable://" + R.drawable.img_loading_fail;
		return pubHeader;
	}

	public void setPubHeader(String pubHeader) {
		this.pubHeader = pubHeader;
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getSponsorHeader() {
		// if (TextUtils.isEmpty(sponsorHeader))
		// return "drawable://" + R.drawable.img_loading_empty;
		//
		// //不包含http://或者ftp://
		// if (!(sponsorHeader.contains("http://") ||
		// sponsorHeader.contains("ftp://")))
		// return Urls.SERVERHOST + sponsorHeader;
		//
		// //包含了看下前缀是不是http开头或者ftp
		// String urlHead = sponsorHeader.substring(0, 7);
		// if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
		// return Urls.SERVERHOST + sponsorHeader;
		//
		// if (sponsorHeader.length() <= 7)//错误地址
		// return "drawable://" + R.drawable.img_loading_fail;
		return sponsorHeader;
	}

	public void setSponsorHeader(String sponsorHeader) {
		this.sponsorHeader = sponsorHeader;
	}

	public String getDateTitle() {
		return dateTitle == null ? AppContext.getApplication().getString(
				R.string.date_title_normal) : dateTitle;
	}

	public void setDateTitle(String dateTitle) {
		this.dateTitle = dateTitle;
	}

	public String getPubName() {
		return pubName;
	}

	public void setPubName(String pubName) {
		this.pubName = pubName;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public int getAttendPeoples() {
		return attendPeoples;
	}

	public void setAttendPeoples(int attendPeoples) {
		this.attendPeoples = attendPeoples;
	}

	public int getInvolved() {
		return involved;
	}

	public void setInvolved(int involved) {
		this.involved = involved;
	}

	public String getExpect() {
		return expect;
	}

	public void setExpect(String expect) {
		this.expect = expect;
	}

}
