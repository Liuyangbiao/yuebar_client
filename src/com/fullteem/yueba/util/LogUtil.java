package com.fullteem.yueba.util;

import android.text.TextUtils;
import android.util.Log;

public class LogUtil {

	public static final boolean DEBUG = true;

	public static void printPushLog(String content) {
		LogUtil.LogDebug("Push", " " + content, null);
	}

	public static void printPayLog(String content) {
		LogUtil.LogDebug("Payment", " " + content, null);
	}

	public static void printUserEntryLog(String content) {
		LogUtil.LogDebug("UserEntry", " " + content, null);
	}

	public static void printIMLog(String content) {
		LogUtil.LogDebug("IMLog", " " + content, null);
	}

	public static void printLog(String tag, String content) {
		LogUtil.LogDebug(tag, " " + content, null);
	}

	public static void printDateLog(String content) {
		LogUtil.LogDebug("Date", " " + content, null);
	}

	public static void printBarLog(String content) {
		LogUtil.LogDebug("Bar", " " + content, null);
	}

	public static void printSingerLog(String content) {
		LogUtil.LogDebug("Singer", " " + content, null);
	}
	
	public static void printUmengLog(String content) {
		LogUtil.LogDebug("Umeng", "友盟自定义" + content, null);
	}

	/**
	 * 等同于log.e()方法
	 * 
	 * @param tag
	 *            为空时打印tag为LogUtil
	 * @param log
	 * 
	 * @param isPrint
	 *            null:完全交由LogUtil.DUBUG控制，true:与上LogUtil.DUBUG判断是否打印日志，
	 *            false则不打印日志
	 */
	public static void LogError(String tag, String log, Boolean isPrint) {
		if ((isPrint != null && !isPrint) || !DEBUG
				|| (isPrint == null && !DEBUG)) {
			return;
		}
		Log.e(TextUtils.isEmpty(tag) ? "LogUtil" : tag, "" + log);
	}

	/**
	 * 等同于log.d()方法
	 * 
	 * @param tag
	 *            为空时打印tag为LogUtil
	 * @param log
	 * @param isPrint
	 *            null:完全交由LogUtil.DUBUG控制，true:与上LogUtil.DUBUG判断是否打印日志，
	 *            false则不打印日志
	 */
	public static void LogDebug(String tag, String log, Boolean isPrint) {
		if ((isPrint != null && !isPrint) || !DEBUG
				|| (isPrint == null && !DEBUG)) {
			return;
		}
		Log.d(TextUtils.isEmpty(tag) ? "LogUtil" : tag, "" + log);
	}

	/**
	 * 等同于log.i()方法
	 * 
	 * @param tag
	 *            为空时打印tag为LogUtil
	 * @param log
	 * 
	 * @param isPrint
	 *            null:完全交由LogUtil.DUBUG控制，true:与上LogUtil.DUBUG判断是否打印日志，
	 *            false则不打印日志
	 */
	public static void LogInfo(String tag, String log, Boolean isPrint) {
		if ((isPrint != null && !isPrint) || !DEBUG
				|| (isPrint == null && !DEBUG)) {
			return;
		}
		Log.i(TextUtils.isEmpty(tag) ? "LogUtil" : tag, "" + log);
	}

	/**
	 * 等同于log.v()方法
	 * 
	 * @param tag
	 *            为空时打印tag为LogUtil
	 * @param log
	 * 
	 * @param isPrint
	 *            null:完全交由LogUtil.DUBUG控制，true:与上LogUtil.DUBUG判断是否打印日志，
	 *            false则不打印日志
	 */
	public static void LogVerbose(String tag, String log, Boolean isPrint) {
		if ((isPrint != null && !isPrint) || !DEBUG
				|| (isPrint == null && !DEBUG)) {
			return;
		}
		Log.v(TextUtils.isEmpty(tag) ? "LogUtil" : tag, "" + log);
	}

	/**
	 * 等同于log.w()方法
	 * 
	 * @param tag
	 *            为空时打印tag为LogUtil
	 * @param log
	 * 
	 * @param isPrint
	 *            null:完全交由LogUtil.DUBUG控制，true:与上LogUtil.DUBUG判断是否打印日志，
	 *            false则不打印日志
	 */
	public static void LogWarn(String tag, String log, Boolean isPrint) {
		if ((isPrint != null && !isPrint) || !DEBUG
				|| (isPrint == null && !DEBUG)) {
			return;
		}
		Log.w(TextUtils.isEmpty(tag) ? "LogUtil" : tag, "" + log);
	}
}
