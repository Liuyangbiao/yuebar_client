package com.fullteem.yueba.app.singer.model;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月19日&emsp;下午3:06:11
 * @use 歌手列表Bean
 */
public class SingerListModel extends SingerModel {
	private static final long serialVersionUID = 1L;

	private String singerStageName; // 艺名
	private String barName; // 酒吧名称
	private String singerDetail;
	private int barId;
	private String barLogoUrl;

	public String getBarLogoUrl() {
		return barLogoUrl;
	}

	public void setBarLogoUrl(String barLogoUrl) {
		this.barLogoUrl = barLogoUrl;
	}

	public String getSingerStageName() {
		return singerStageName;
	}

	public void setSingerStageName(String singerStageName) {
		this.singerStageName = singerStageName;
	}

	public void setSingerLogoUrl(String singerLogoUrl) {
		setLogoUrl(singerLogoUrl);
	}

	public String getBarName() {
		return barName;
	}

	public void setBarName(String barName) {
		this.barName = barName;
	}

	public String getSingerDetail() {
		return singerDetail;
	}

	public void setSingerDetail(String singerDetail) {
		this.singerDetail = singerDetail;
	}

	public int getBarId() {
		return barId;
	}

	public void setBarId(int barId) {
		this.barId = barId;
	}

	public void setTotal(int total) {
		super.setPraiseTotal(total + "");
	}

	public void setVisitorNum(int visitorNum) {
		super.setPageviews(visitorNum);
	}

	@Override
	public void setUserLogoUrl(String userLogoUrl) {
		super.setLogoUrl(userLogoUrl);
	}

}
