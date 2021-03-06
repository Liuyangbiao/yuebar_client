package com.fullteem.yueba.engine.push;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.fullteem.yueba.service.NetworkManager;
import com.fullteem.yueba.util.LogUtil;

public class PushService {
	static Context context = null;
	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;

	/*
	 * JPush SDK 提供的推送服务是默认开启的。 可以通过调用停止推送服务API来停止极光推送服务。
	 * 当又需要使用极光推送服务时，则必须要调用恢复推送服务 API
	 */
	public static void initPush(Context appContext) {
		LogUtil.printPushLog("Jpush initPush");

		context = appContext;
		JPushInterface.setDebugMode(true);
		JPushInterface.init(context);
	}

	// when called initPush(), push process started even without calling
	// resumePush
	public static void resumePush() {
		LogUtil.printPushLog("Jpush resumePush");
		JPushInterface.resumePush(context);
	}

	public static void stopPush() {
		LogUtil.printPushLog("Jpush stopPush");
		JPushInterface.stopPush(context);
	}

	/*
	 * when user leave one specified bar, not stopPush, but pausePush by set
	 * default tag
	 */
	public static void pauseBarPush() {
		setPushTag("0");
	}

	/*
	 * 本API 用于“用户使用时长”，“活跃用户”，“用户打开次数”的统计，并上报到服务器，在 Portal 上展示给开发者。
	 */
	public static void onResume(String tag, Activity activity) {
		if (NetworkManager.isFastNetwork()) {
			JPushInterface.onResume(activity);
			LogUtil.printPushLog("Jpush onResume:" + tag);
		}
	}

	public static void onPause(String tag, Activity activity) {
		if (NetworkManager.isFastNetwork()) {
			JPushInterface.onPause(activity);
			LogUtil.printPushLog("Jpush onPause:" + tag);
		}
	}

	public static boolean isPushStopped() {
		boolean isStopped = JPushInterface.isPushStopped(context);
		LogUtil.printPushLog("Jpush stopped:" + isStopped);
		return isStopped;
	}

	/**
	 * 设置允许接收通知时间
	 */
	public static void setPushTime() {
		// JPushInterface.setPushTime(context, days, startime, endtime);
	}

	public static void setPushTag(String barId) {
		LogUtil.printPushLog("set push tag:" + barId);
		if (isValidTagAndAlias(barId)) {
			Set<String> tagSet = new LinkedHashSet<String>();
			tagSet.add(barId);

			// 调用JPush API设置Tag
			mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
		} else {
			LogUtil.printPushLog("invalid tag");
		}

	}

	// 校验Tag Alias 只能是数字,英文字母和中文
	public static boolean isValidTagAndAlias(String s) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
	}

	private final static TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				LogUtil.printPushLog(logs);
				break;

			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				LogUtil.printPushLog(logs);
				break;

			default:
				logs = "Failed with errorCode = " + code;
				LogUtil.printPushLog(logs);
			}

		}

	};

	private final static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			case MSG_SET_TAGS:
				LogUtil.printPushLog("Set tags in handler");

				if (context != null) {
					JPushInterface.setAliasAndTags(context, null,
							(Set<String>) msg.obj, mTagsCallback);
				} else {
					LogUtil.printPushLog("app context is null!");
				}

				break;

			default:
				LogUtil.printPushLog("Unhandled msg - " + msg.what);
			}
		}
	};

}
