package com.fullteem.yueba.service;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.CommonModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.BaiduMapUtil;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.LogUtil;

public class LocationManager {
	public static LocationManager instance;
	public static BDLocation defaultLocation = new BDLocation(116.404, 39.915,
			0);// beijing tian anmen

	private LocationManager() {
	}

	public static LocationManager getInstance() {
		LogUtil.printUserEntryLog("LocationManager getInstance");
		if (instance == null) {
			instance = new LocationManager();
		}

		return instance;
	}

	public BDLocation getDefaultLocation() {
		return defaultLocation;
	}

	public void getLocation() {
		final Context context = AppContext.getApplication();
		new BaiduMapUtil(context, new BaiduMapUtil.IGetLocation() {
			@Override
			public void getlocation(BDLocation location) {
				LogUtil.printUserEntryLog("getlocation successful");

				((AppContext) context).setLocation(location);

				// inform server
				// sentLocationRequest();
			}
		});
	}

	public static void sentLocationRequest() {
		// 发送定位请求
		JSONObject ob = new JSONObject();
		ob.put("userId", AppContext.getApplication().getUserInfo().getUserId());
		ob.put("lng", AppContext.getApplication().getLocation().getLongitude());
		ob.put("lat", AppContext.getApplication().getLocation().getLatitude());

		// 插入定位请求
		HttpRequestFactory.getInstance().getRequest(Urls.location_acation, ob,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						CommonModel cm = new CommonModel();
						cm = JsonUtil.convertJsonToObject(content,
								CommonModel.class);

						if (cm.getCode() != null
								&& GlobleConstant.REQUEST_SUCCESS
										.equalsIgnoreCase(cm.getCode())) {
							LogUtil.printUserEntryLog("sentLocationRequest successful");
						}
					};

					@Override
					public void onFinish() {
					};
				});
	}

}
