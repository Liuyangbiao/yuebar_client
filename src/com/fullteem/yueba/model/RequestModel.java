package com.fullteem.yueba.model;

import com.fullteem.yueba.net.http.RequestParams;

/**
 * RequestModel.java
 * 
 * @author: allen
 * @Function: 网络请求模型
 * @createDate: 2014-8-29
 * @update:
 */
public class RequestModel {
	private String url;// 请求网络地址
	private RequestParams params;// 请求参数
	private Class<?> cls;// 请求转换数据模型类
	private boolean showDialog = false;// 是否显示网络加载对话框
	private boolean showErrorMessage = false;// 是否显示错误的信息

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RequestParams getParams() {
		return params;
	}

	public void setParams(RequestParams params) {
		this.params = params;
	}

	public Class<?> getCls() {
		return cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}

	public boolean isShowDialog() {
		return showDialog;
	}

	public void setShowDialog(boolean showDialog) {
		this.showDialog = showDialog;
	}

	public boolean isShowErrorMessage() {
		return showErrorMessage;
	}

	public void setShowErrorMessage(boolean showErrorMessage) {
		this.showErrorMessage = showErrorMessage;
	}

	@Override
	public String toString() {
		return "RequestModel [url=" + url + ", params=" + params + ", cls="
				+ cls + ", showDialog=" + showDialog + ", showErrorMessage="
				+ showErrorMessage + "]";
	}

}
