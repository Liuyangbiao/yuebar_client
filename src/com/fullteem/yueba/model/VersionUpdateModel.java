package com.fullteem.yueba.model;

/**
 * @author jun
 * @version 1.0.0
 * @created 2015年2月4日&emsp;下午1:43:06
 * @use 版本升级model
 */
public class VersionUpdateModel {
	private String appUrl;
	private String appContent;
	private String appTitle;
	private float versionNum;

	public String getAppUrl() {
		return appUrl;
	}

	public String getAppContent() {
		return appContent;
	}

	public String getAppTitle() {
		return appTitle;
	}

	public float getVersionNum() {
		return versionNum;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public void setAppContent(String appContent) {
		this.appContent = appContent;
	}

	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}

	public void setVersionNum(float versionNum) {
		this.versionNum = versionNum;
	}

}
