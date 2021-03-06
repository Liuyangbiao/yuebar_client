package com.fullteem.yueba.engine.push;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.BarChatModel;
import com.fullteem.yueba.model.CommonModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.JsonUtil;
import com.fullteem.yueba.util.LogUtil;

public class MessageUtil {
	public static final String MESSAGE_RECEIVED_ACTION = "MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public static final int MESSAGE_TYPE_TXT = 1;
	public static final int MESSAGE_TYPE_IMAGE = 2;
	public static final int MESSAGE_TYPE_TEX_AND_IMAGE = 3;

	public static final boolean ENABLE_LOCAL_SEND_OUT_MESSAGE = false;

	@SuppressLint("SimpleDateFormat")
	// 'yyyy-MM-dd HH:mm:ss a'(12小时制)
	// 'yyyy-MM-dd HH:mm:ss'(24小时制)
	private static DateFormat dateFormatWithYear = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");
	@SuppressLint("SimpleDateFormat")
	private static DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

	private static final String ICON_PREFIX = "#<";
	private static final String ICON_SUFFIX = ">#";

	public static BarChatModel parseMessage(Intent intent) {
		String message = intent.getStringExtra(KEY_MESSAGE);
		String extras = intent.getStringExtra(KEY_EXTRAS);

		/*
		 * 03-02 10:39:41.483: D/Push(30517): extras :
		 * {"userSex":"","userLogoUrl":"","userName":"bill","msgType":"1",
		 * "createDate":"2015-03-02 10:38:38","barId":"4","userId":"1"}
		 */

		BarChatModel model = JSON.parseObject(extras, BarChatModel.class);

		String dateStr = model.getCreateDate();
		LogUtil.printPushLog("date:" + dateStr);
		try {
			Date date = dateFormatWithYear.parse(dateStr);
			dateStr = dateFormat.format(date);
			LogUtil.printPushLog("after transformed. date:" + dateStr);
			model.setCreateDate(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			LogUtil.printPushLog("exception:" + e.toString());
		}

		model.setMsgContent(message);

		return model;
	}

	public static BarChatModel createNewMessage(String barId, int msgType,
			String msgContent) {
		BarChatModel model = new BarChatModel();

		model.setBarId(barId);
		model.setMsgContent(msgContent);
		model.setMsgType(msgType);

		Date now = new Date();
		model.setCreateDate(dateFormat.format(now));

		String userId = AppContext.getApplication().getUserInfo().getUserId();
		String userName = AppContext.getApplication().getUserInfo()
				.getUserName();
		String userLogoUrl = AppContext.getApplication().getUserInfo()
				.getUserLogoUrl();
		String userSex = AppContext.getApplication().getUserInfo().getUserSex();
		String userAge = AppContext.getApplication().getUserInfo().getUserAge();

		model.setUserId(userId);
		model.setUserLogoUrl(userLogoUrl);
		model.setUserName(userName);
		model.setUserSex(userSex);
		model.setUserAge(userAge);

		return model;
	}

	public static BarChatModel createWelcomeMessage(String user,
			String msgContent) {
		BarChatModel model = new BarChatModel();

		Date now = new Date();
		model.setCreateDate(dateFormatWithYear.format(now));

		model.setUserName(user);
		model.setMsgContent(msgContent);
		model.setMsgType(MessageUtil.MESSAGE_TYPE_TXT);

		String userAge = AppContext.getApplication().getUserInfo().getUserAge();

		return model;
	}

	public static String composeMessageContent(String str, int id) {
		if (str == null) {
			str = "";
		}

		return str + ICON_PREFIX + id + ICON_SUFFIX;
	}

	public static Map<String, Integer> parseMessageContent(final String content) {
		Map<String, Integer> map = new HashMap<String, Integer>();

		Integer type;

		String tempStr = content;
		StringBuffer str;
		int start = 0;
		int end = 0;

		int prefixLenth = ICON_PREFIX.length();
		int suffixLenth = ICON_SUFFIX.length();

		while (true) {
			str = new StringBuffer(tempStr);
			start = str.indexOf(ICON_PREFIX);
			end = str.indexOf(ICON_SUFFIX);

			if (start == -1 || end == -1) {
				// pure string
				type = MESSAGE_TYPE_TXT;
				map.put(tempStr, type);
				return map;
			} else if (start == 0 && (end > start + prefixLenth + 1)) {
				tempStr = str.substring(start + prefixLenth + 1, end - 1);

				type = MESSAGE_TYPE_IMAGE;
				map.put(tempStr, type);

				// continue
				if (end + suffixLenth == str.length()) {
					return map;
				} else {
					tempStr = str.substring(end + suffixLenth + 1);
				}

			} else {
				// includes the case: end < start, regard suffix as common
				// string
				tempStr = str.substring(0, start - 1);
				type = MESSAGE_TYPE_TXT;
				map.put(tempStr, type);

				tempStr = str.substring(start);
			}
		}
	}

	public static int judgeMessageType(Map<String, Integer> map) {
		int type = 0;// 01, 10, 11
		String key;

		Iterator<String> iter = map.keySet().iterator();

		while (iter.hasNext()) {

			key = iter.next();

			type = map.get(key);
			if (map.get(key) == MESSAGE_TYPE_TXT) {
				type = type | 0x01;
			} else if (map.get(key) == MESSAGE_TYPE_IMAGE) {
				type = type | 0x10;
			}

		}

		return type;
	}

	public static Date parseDate(String dateStr) {
		Date date = null;

		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	public static boolean isMessageReceiver(String userId) {
		String selfId = AppContext.getApplication().getUserInfo().getUserId();
		LogUtil.printPushLog("isMessageReceiver. selfId" + selfId);
		if (selfId.equals(userId)) {
			return false;// sender
		} else {
			return true;// receiver
		}
	}

	public static void sendNewMessageRequest(BarChatModel model) {
		LogUtil.printPushLog("enter sendNewMessageRequest");

		JSONObject ob = (JSONObject) JSON.toJSON(model);

		HttpRequestFactory.getInstance().postRequest(
				Urls.BAR_CHAT_PUSH_MESSAGE, ob, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

						LogUtil.printPushLog("send message request is handled");

						CommonModel cm = new CommonModel();
						cm = JsonUtil.convertJsonToObject(content,
								CommonModel.class);
						if (cm != null
								&& GlobleConstant.REQUEST_SUCCESS
										.equalsIgnoreCase(cm.getCode())) {
							LogUtil.printPushLog("send message succeed");
						} else {
							LogUtil.printPushLog("server failed to handle message request");
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.printPushLog("send message failed");
					};

				});

	}

}
