package com.fullteem.yueba.service;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.LogUtil;

public class ServerHostManager {
	enum HOST_FETCH_STATE {
		INITED, FETCHING, SUCCEED, FAILED
	};

	public static ServerHostManager instance;

	private static HOST_FETCH_STATE state;

	private Context context;

	private ServerHostManager(Context context) {
		this.context = context;
		state = HOST_FETCH_STATE.INITED;
	}

	public interface IHostFetchListener {
		void onHostFetched(boolean result);
	}

	public static ServerHostManager getInstance(Context context) {
		LogUtil.printUserEntryLog("ServerHostManager getInstance");
		if (instance == null) {
			instance = new ServerHostManager(context);
		}

		return instance;
	}

	/*
	 * When first time to call server api, need to make sure the host is ready.
	 */
	public boolean isHostReady() {
		LogUtil.printUserEntryLog("enter ServerHostManager isHostReady");
		return (state == HOST_FETCH_STATE.SUCCEED || state == HOST_FETCH_STATE.FAILED);
	}

	public void sendServerHostRequest(final IHostFetchListener listener) {
		LogUtil.printUserEntryLog("ServerHostManager sendServerHostRequest");
		state = HOST_FETCH_STATE.FETCHING;
		String version = "1.0.0";
		try {
			version = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		LogUtil.printUserEntryLog("版本号:" + version);
//		JSONObject job = new JSONObject();
//		job.put("version", version);
//		job.put("type", "2");

		HttpRequestFactory.getInstance().getRequestServerHost(Urls.GET_SERVER_HOST,
				version, "2", "", new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						LogUtil.printUserEntryLog("sendServerHostRequest content:"
								+ content);

						ServerHostModel rm = ServerHostModel.getInstance();
						rm = JSON.parseObject(content, ServerHostModel.class);
						if (rm != null) {
							AppContext appcontext = (AppContext) context
									.getApplicationContext();
							appcontext.setServerHostData(rm.getResult());
							String serverHost = rm.getResult().getHost();
							String imageUrl = rm.getResult().getImgUrl();
							LogUtil.printUserEntryLog("sendServerHostRequest succeed, server url is:"
									+ serverHost);
							Urls.setBaseServerUrl(serverHost);
							Urls.setBaseImageUrl(imageUrl);
							state = HOST_FETCH_STATE.SUCCEED;
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.printUserEntryLog("sendServerHostRequest failed:"
								+ content + " " + error.toString());
						Urls.setBaseServerUrl("");
						Urls.setBaseImageUrl("");
					};

					@Override
					public void onFinish() {
						LogUtil.printUserEntryLog("sendServerHostRequest finished");
						if (state != HOST_FETCH_STATE.SUCCEED) {
							state = HOST_FETCH_STATE.FAILED;
							Urls.setDefaultServerUrl();
						}

						listener.onHostFetched(state == HOST_FETCH_STATE.SUCCEED);
					};
				});

	}
}
