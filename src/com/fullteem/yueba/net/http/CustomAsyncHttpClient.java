package com.fullteem.yueba.net.http;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.view.View;

import com.fullteem.yueba.model.RequestModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.http.MySSLSocketFactory.SSLSocketFactoryEx;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UIHelper;
import com.fullteem.yueba.widget.MyProgressDialog;

public class CustomAsyncHttpClient {
	private String TAG = "CustomAsyncHttpClient";

	private AsyncHttpClient asyncHttpClient;
	private MyProgressDialog dialog;
	private Context mContext;
	private ResponeModel baseModel;

	public CustomAsyncHttpClient(Context context) {
		asyncHttpClient = new AsyncHttpClient();
		mContext = context;
		if (mContext != null) {
			dialog = new MyProgressDialog(mContext, "", true);
			dialog.tv_value.setVisibility(View.GONE);
		}
		baseModel = new ResponeModel();
	}

	public void post(final RequestModel requestModel,
			final CustomAsyncResponehandler responseHandler) {
		RequestParams newParams = null;
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		if (requestModel.getParams() != null) {
			newParams = new RequestParams();
			for (BasicNameValuePair param : requestModel.getParams()
					.getParamsList()) {
				jsonObject.put(param.getName(), param.getValue());
			}
			for (BasicNameValuePair param : requestModel.getParams()
					.getIntParamsList()) {
				jsonObject.put(param.getName(),
						(int) Integer.valueOf(param.getValue()));
			}

			newParams.fileParams = requestModel.getParams().fileParams;

			newParams.put("p", jsonObject.toString());
		}

		LogUtil.LogDebug(TAG, "sendPost：" + requestModel.getUrl()
				+ (newParams == null ? "" : "?" + newParams.toString()), null);
		//访问https接口
		asyncHttpClient.post(requestModel.getUrl(), newParams,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						LogUtil.LogDebug(TAG, "onStart___", null);
						if (requestModel.isShowDialog()) {// 显示网络对话框
							if (mContext != null) {
								dialog.show();
							}
						}
						responseHandler.onStart();
					}

					@Override
					public void onFinish() {
						LogUtil.LogDebug(TAG, "onFinish___", null);
						if (requestModel.isShowDialog()) {// 隐藏网络对话框
							if (mContext != null) {
								dialog.dismiss();
							}
						}
						responseHandler.onFinish();
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						LogUtil.LogDebug(TAG, "onSuccess___" + content, null);

						// TODO:解密返回的参数
						baseModel = JsonUtil.convertJsonToObject(content,
								ResponeModel.class);

						if (baseModel != null) {
							baseModel.setCls(requestModel.getCls());
							baseModel.setJson(content);
							baseModel.init();
						}
						if (baseModel == null
								|| (baseModel != null && !baseModel.isStatus())) {
							if (requestModel.isShowErrorMessage()) {
								if (mContext != null) {
									UIHelper.ShowMessage(mContext,
											baseModel.getMsg());
								}
							}
						}

						// responseHandler.onSuccess(baseModel);
						responseHandler.onSuccess(baseModel);
					}

					// @Override
					// public void onSuccess(String content) {
					// LogUtil.LogDebug(TAG, "onSuccess___" + content, isTag);
					//
					// // TODO:解密返回的参数
					// baseModel = JsonUtil.convertJsonToObject(content,
					// ResponeModel.class);
					//
					// if (baseModel != null) {
					// if (baseModel.isStatus()) {
					// baseModel.setJson(content);
					// if (baseModel.getData() != null &&
					// baseModel.getData().length() > 0) {
					// String Head = baseModel.getData().substring(0, 1);
					// // 注：自动快捷解析的情况
					// // 1.data直接是对象
					// // 2.data直接是数组（不含条数）
					// // 3.data直接是对象，对象里面包含“result”数组（条数可选）
					// // 其它情况均需返回对象，在你的service中进行处理
					//
					// // Data是对象
					// if ("{".equals(Head)) {
					// JSONObject object;
					// try {
					// object = new JSONObject(baseModel.getData());
					//
					// baseModel.setDataResult(object);
					//
					// if (!object.isNull("result")) {// list
					// // 带分页
					// baseModel.setResult(JsonUtil.convertJsonToList(object.get("result").toString(),
					// requestModel.getCls()));
					// if (!object.isNull("totalCount")) {// list
					// // 带总条数
					// baseModel.setTotalCount(object.getString("totalCount"));
					// // 设置其他分页信息
					// }
					// } else {
					// baseModel.setResult(JsonUtil.convertJsonToObject(baseModel.getData(),
					// requestModel.getCls()));
					// }
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
					//
					// } else if ("[".equals(Head)) {// data是数组
					// baseModel.setResult(JsonUtil.convertJsonToList(baseModel.getData(),
					// requestModel.getCls()));
					// }
					// }
					// } else {
					// if (requestModel.isShowErrorMessage()) {
					// if (mContext != null) {
					// UIHelper.ShowMessage(mContext, baseModel.getMsg());
					// }
					// }
					// }
					// responseHandler.onSuccess(baseModel);
					// }
					// }

					@Override
					public void onFailure(Throwable error, String content) {
						responseHandler.onFailure(error, content);
						LogUtil.LogDebug(TAG, "onFailure___" + content, null);
					}
				});
	}
	
	public void doHttpsPost(final RequestModel requestModel,
			final CustomAsyncResponehandler responseHandler) {
		RequestParams newParams = null;
		boolean doHttpsRequest = true;
		com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		if (requestModel.getParams() != null) {
			newParams = new RequestParams();
			for (BasicNameValuePair param : requestModel.getParams()
					.getParamsList()) {
				jsonObject.put(param.getName(), param.getValue());
			}
			for (BasicNameValuePair param : requestModel.getParams()
					.getIntParamsList()) {
				jsonObject.put(param.getName(),
						(int) Integer.valueOf(param.getValue()));
			}

			newParams.fileParams = requestModel.getParams().fileParams;

			newParams.put("p", jsonObject.toString());
		}

		LogUtil.LogDebug(TAG, "sendPost：" + requestModel.getUrl()
				+ (newParams == null ? "" : "?" + newParams.toString()), null);
		//访问https接口
		if(doHttpsRequest){
			SSLSocketFactoryEx sf = MySSLSocketFactory.getInstance().getSSLSocketFactory();
			asyncHttpClient.setSSLSocketFactory(sf);
		}
		asyncHttpClient.post(requestModel.getUrl(), newParams,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						LogUtil.LogDebug(TAG, "onStart___", null);
						if (requestModel.isShowDialog()) {// 显示网络对话框
							if (mContext != null) {
								dialog.show();
							}
						}
						responseHandler.onStart();
					}

					@Override
					public void onFinish() {
						LogUtil.LogDebug(TAG, "onFinish___", null);
						if (requestModel.isShowDialog()) {// 隐藏网络对话框
							if (mContext != null) {
								dialog.dismiss();
							}
						}
						responseHandler.onFinish();
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						LogUtil.LogDebug(TAG, "onSuccess___" + content, null);

						baseModel = JsonUtil.convertJsonToObject(content,
								ResponeModel.class);

						if (baseModel != null) {
							baseModel.setCls(requestModel.getCls());
							baseModel.setJson(content);
							baseModel.init();
						}
						if (baseModel == null
								|| (baseModel != null && !baseModel.isStatus())) {
							if (requestModel.isShowErrorMessage()) {
								if (mContext != null) {
									UIHelper.ShowMessage(mContext,
											baseModel.getMsg());
								}
							}
						}

						responseHandler.onSuccess(baseModel);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						responseHandler.onFailure(error, content);
						LogUtil.LogDebug(TAG, "onFailure___" + content, null);
					}
				});
	}
}
