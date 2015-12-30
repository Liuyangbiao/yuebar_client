package com.fullteem.yueba.net.http;

import java.util.Map;

/**
 * 请求参数配置
 * 
 * @author ssy
 * 
 */
public class RequestConfig {
	public static final String GET = "get";// http get请求方式
	public static final String POST = "post";// http post请求方式
	private String requestCode = null;// 请求码
	private String method = null;// 请求方式
	private String webAddress = null;// 网络请求地址
	private String authName = null;// http basic认证用户名
	private String authPswd = null;// http basic认证用户密码
	private Map<String, String> data = null;// http请求数据（httpbody）
	private String requestdata;// 当采用raw方式进行数据请求时得请求数据，即没有键，只有值的情况
	private Map<String, String> header = null;// http请求头
	private Map<String, String> files = null;// http发送文件内容（key可以存放用户名，value存放文件路径，注意这边的路径是一个完整的路径）
	private Class<?> cls;// json解析数模板
	private Class<?> element;// 当cls是ArrayList的时候的列表元素
	private boolean needParse = true;// 是否需要对数据进行解析，如果不需要进行数据解析，那么就返回原始数据对象，包含http状态码
	private Map<String, Object> objectData = null;
	private int requestFlag;
	private String uploadPath;

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	/**
	 * 发送post请求方式
	 */
	private int PostFlag;

	public int getPostFlag() {
		return PostFlag;
	}

	public void setPostFlag(int postFlag) {
		PostFlag = postFlag;
	}

	public int getRequestFlag() {
		return requestFlag;
	}

	public void setRequestFlag(int requestFlag) {
		this.requestFlag = requestFlag;
	}

	public RequestConfig() {

	}

	public RequestConfig(String requestCode) {
		this.requestCode = requestCode;
	}

	public String getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	public Class<?> getCls() {
		return cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}

	public Class<?> getElement() {
		return element;
	}

	public void setElement(Class<?> element) {
		this.element = element;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public Map<String, Object> getObjectData() {
		return objectData;
	}

	public void setObjectData(Map<String, Object> objectData) {
		this.objectData = objectData;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getAuthPswd() {
		return authPswd;
	}

	public void setAuthPswd(String authPswd) {
		this.authPswd = authPswd;
	}

	public Map<String, String> getFiles() {
		return files;
	}

	public void setFiles(Map<String, String> files) {
		this.files = files;
	}

	public boolean isNeedParse() {
		return needParse;
	}

	public void setNeedParse(boolean needParse) {
		this.needParse = needParse;
	}

	public String getRequestdata() {
		return requestdata;
	}

	public void setRequestdata(String requestdata) {
		this.requestdata = requestdata;
	}

	@Override
	public String toString() {
		return "RequestConfig [requestCode=" + requestCode + ", method="
				+ method + ", webAddress=" + webAddress + ", authName="
				+ authName + ", authPswd=" + authPswd + ", data=" + data
				+ ", requestdata=" + requestdata + ", header=" + header
				+ ", files=" + files + ", cls=" + cls + ", element=" + element
				+ ", needParse=" + needParse + "]";
	}

}
