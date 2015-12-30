package com.fullteem.yueba.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.LogUtil;

/**
 * @className：ResponeModel.java
 * @author: allen
 * @Function: 网络响应模型
 * @createDate: 2014-8-29
 * @update:
 */
public class ResponeModel {
	private String result;// 转换的Json
	private JSONObject dataResult;// result里面内容转化为对象的json
	private String json;// 服务器返回的整体Json
	private String code;// 错误码
	private boolean status;// 状态
	private String msg = "";// 错误信息
	private Object resultObject = "";// 数据
	private String totalCount;// 总条数
	private Class<?> cls;// 请求转换数据模型类

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public JSONObject getDataResult() {
		return dataResult;
	}

	public void setDataResult(JSONObject dataResult) {
		this.dataResult = dataResult;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public Class<?> getCls() {
		return cls;
	}

	public void setCls(Class<?> cls) {
		this.cls = cls;
	}

	/**
	 * 初始化所有数据
	 */
	public void init() {
		initStatus();
		// initMsg();
		initResult();
	}

	/**
	 * 判断接口是否正确
	 * 
	 */
	private void initStatus() {
		// if ((int) code == (int) GlobalConstant.RESPONCE_TRUE_CODE) {
		// setStatus(true);
		// } else {
		// setStatus(false);
		// }

		this.status = GlobleConstant.REQUEST_SUCCESS
				.equalsIgnoreCase(this.code);
	}

	/**
	 * 获取错误信息
	 */
	// private void initMsg() {
	// String msg = GlobalConstant.RESPONE_CODE_MAP.get(code);
	// setMsg(msg);
	// }

	/**
	 * 处理返回的结果，做一些简单的处理
	 */
	private void initResult() {
		if (isStatus()) {
			if (getResult() != null && getResult().length() > 0) {
				String Head = getResult().substring(0, 1);
				// 注：自动快捷解析的情况
				// 1.data直接是对象
				// 2.data直接是数组（不含条数）
				// 3.data直接是对象，对象里面包含“result”数组（条数可选）
				// 其它情况均需返回对象，在你的service中进行处理

				// Data是对象
				if ("{".equals(Head)) {
					JSONObject object;
					try {
						object = new JSONObject(getResult());

						setDataResult(object);

						if (!object.isNull("result")) {// list
							// 带分页
							setResultObject(JsonUtil.convertJsonToList(object
									.get("result").toString(), getCls()));
							if (!object.isNull("totalCount")) {// list
								// 带总条数
								setTotalCount(object.getString("totalCount"));
								// 设置其他分页信息
							}
						} else {
							setResultObject(JsonUtil.convertJsonToObject(
									getResult(), getCls()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if ("[".equals(Head)) {// data是数组
					setResultObject(JsonUtil.convertJsonToList(getResult(),
							getCls()));
				}
			}
		}
	}

	/**
	 * 获取在result里面的list
	 * 
	 * @param keyString
	 * @param cls
	 * @return
	 */
	public Object getListObject(String keyString, Class<?> cls) {
		if (getResultObject() == null || TextUtils.isEmpty(keyString))
			return null;
		try {
			JSONObject object = new JSONObject(getResultObject() + "");
			return JsonUtil.convertJsonToList(object.getString(keyString), cls);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取在result里面的对象
	 * 
	 * @param keyString
	 * @param cls
	 * @return
	 */
	public Object getObject(String keyString, Class<?> cls) {
		if (TextUtils.isEmpty(keyString) || getDataResult() == null)
			return null;
		try {
			return JsonUtil.convertJsonToObject(
					getDataResult().getString(keyString), cls);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取对象，不再rsult里面的情况
	 * 
	 * @param keyString
	 * @param cls
	 * @return
	 */
	public Object getObjectInJson(String keyString, Class<?> cls) {
		if (TextUtils.isEmpty(getJson()) || TextUtils.isEmpty(keyString))
			return null;
		try {
			JSONObject jObject = new JSONObject(getJson());
			if (jObject.isNull(keyString))
				return null;
			return JsonUtil.convertJsonToObject(jObject.getString(keyString),
					cls);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据key单独取result里内容
	 * 
	 * @param keyString
	 * @return
	 */
	public <T> T getTreeInResult(String keyString) {
		if (getDataResult() == null || TextUtils.isEmpty(keyString))
			return null;
		if (getDataResult().isNull(keyString))
			return null;
		try {
			return (T) getDataResult().get(keyString);
		} catch (JSONException e) {
			e.printStackTrace();
			LogUtil.LogError(getClass().getName(),
					"getTreeInResult转换失败" + e.toString(), null);
			return null;
		}
	}

	@Override
	public String toString() {
		return "ResponeModel [result=" + result + ", dataResult=" + dataResult
				+ ", json=" + json + ", code=" + code + ", status=" + status
				+ ", msg=" + msg + ", resultObject=" + resultObject
				+ ", totalCount=" + totalCount + ", cls=" + cls + "]";
	}

}
