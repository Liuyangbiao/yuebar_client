package com.fullteem.yueba.net.http;

import java.io.File;
import java.io.FileNotFoundException;
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

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.engine.upload.UploadManager;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.Example;
import com.fullteem.yueba.model.RequestModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.MySSLSocketFactory.SSLSocketFactoryEx;
import com.fullteem.yueba.util.LogUtil;

public class HttpRequestFactory {
	private static boolean doHttpsRequest = true;
	
	private static HttpRequestFactory httpRequestFactory;

	private static AsyncHttpClient asynHttpClient;

	private static CustomAsyncHttpClient cusHttpClient;

	public static HttpRequestFactory getInstance() {
		if (httpRequestFactory == null) {
			httpRequestFactory = new HttpRequestFactory();
			asynHttpClient = new AsyncHttpClient();
			cusHttpClient = new CustomAsyncHttpClient(null);
		}
		return httpRequestFactory;
	}

	/**
	 * 需要显示对话框则调用该方法
	 * 
	 * @param context
	 * @return
	 */
	public static HttpRequestFactory getInstance(Context context) {
		cusHttpClient = new CustomAsyncHttpClient(context);
		if (httpRequestFactory == null) {
			synchronized (HttpRequest.class) {
				if (httpRequestFactory == null) {
					httpRequestFactory = new HttpRequestFactory();
				}
			}
		}
		return httpRequestFactory;
	}

	public void getRequest(String url, JSONObject jsonObj,
			AsyncHttpResponseHandler asyncHttpResponseHandler) {
		RequestParams requestParams = new RequestParams();
		if (jsonObj != null) {
			requestParams.put("p", jsonObj.toString());
		}
		LogUtil.LogDebug("HttpRequestFactory", url + requestParams.toString(),
				null);
		// asynHttpClient.setSSLSocketFactory(SSLSocketFactory.getSocketFactory());
		asynHttpClient.get(url, requestParams, asyncHttpResponseHandler);
	}

	public void getRequestServerHost(String url, String version, String type,
			String id, AsyncHttpResponseHandler asyncHttpResponseHandler) {
		RequestParams requestParams = new RequestParams();
		requestParams.put("version", version);
		requestParams.put("type", type);
		requestParams.put("id", id);
		LogUtil.LogDebug("HttpRequestFactory", url + requestParams.toString(),
				null);
		// asynHttpClient.setSSLSocketFactory(SSLSocketFactory.getSocketFactory());
		asynHttpClient.get(url, requestParams, asyncHttpResponseHandler);
	}

	public void postRequest(String url, JSONObject jsonObj,
			AsyncHttpResponseHandler asyncHttpResponseHandler) {
		RequestParams requestParams = new RequestParams();
		if (jsonObj != null) {
			requestParams.put("p", jsonObj.toString());
		}
		LogUtil.LogDebug("HttpRequestFactory", url + requestParams.toString(),
				null);
		// SSLSocketFactoryEx sf =
		// MySSLSocketFactory.getInstance().getSSLSocketFactory();
		// asynHttpClient.setSSLSocketFactory(sf);
		asynHttpClient.post(url, requestParams, asyncHttpResponseHandler);
	}

	/**
	 * post访问https接口
	 * 
	 * @param url
	 *            访问url
	 * @param jsonObj
	 *            上传的json格式参数
	 * @param asyncHttpResponseHandler
	 *            回调Handler
	 */
	public void doHttpsPostRequest(String url, JSONObject jsonObj,
			AsyncHttpResponseHandler asyncHttpResponseHandler) {
		RequestParams requestParams = new RequestParams();
		if (jsonObj != null) {
			requestParams.put("p", jsonObj.toString());
		}
		LogUtil.LogDebug("HttpRequestFactory", url + requestParams.toString(),
				null);
		// 访问https接口
		if(doHttpsRequest){
			SSLSocketFactoryEx sf = MySSLSocketFactory.getInstance().getSSLSocketFactory();
			asynHttpClient.setSSLSocketFactory(sf);
		}
		
		asynHttpClient.post(url, requestParams, asyncHttpResponseHandler);
	}

	/**
	 * 上传图片
	 * 
	 * @param http
	 * @param path
	 */
	public void uploadFile(IHttpTaskCallBack http, String path) {
		HttpRequestTask task = new HttpRequestTask(http);
		RequestConfig rc = new RequestConfig();
		rc.setUploadPath(path);
		rc.setPostFlag(GlobleConstant.UPLOAD);
		task.execute(rc);
	}

	public void uploadFile(Context context, File file,
			final CustomAsyncResponehandler handler) {
		file = UploadManager.getInstance(context).compressImage(file);

		RequestModel requestModel = new RequestModel();
		RequestParams params = new RequestParams();
		try {
			params.put("imgFile", file);
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		requestModel.setParams(params);
		// 控制回调，部分特殊回调需要重写AsyncHttpResponseHandler对应方法自行处理
		requestModel.setCls(Example.class);
		requestModel.setShowDialog(true);
		requestModel.setShowErrorMessage(true);
		requestModel.setUrl(Urls.uploadMemberImage_action);
		requestModel.setParams(params);
		cusHttpClient.post(requestModel, new CustomAsyncResponehandler() {
			@Override
			public void onSuccess(ResponeModel baseModel) {
				if (baseModel != null && baseModel.isStatus()) {
					handler.onSuccess(baseModel);
					LogUtil.LogDebug("HttpRequestFactory", baseModel.getJson(),
							null);
					// 将返回结果封装在对象中:baseModel.setResult(result)
					// 而后在界面中获取返回结果对象：baseModel.getResult()
					// 复杂的返回结果需要另外做处理
				}

			}

			@Override
			public void onStart() {
			}

			@Override
			public void onFailure(Throwable error, String content) {
			}

			@Override
			public void onFinish() {
			}
		});

	}
}
