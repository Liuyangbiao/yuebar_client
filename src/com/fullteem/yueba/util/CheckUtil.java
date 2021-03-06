package com.fullteem.yueba.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

public class CheckUtil {
	private static final int USER_NAME_LEN_MIN = 6;
	private static final int USER_NAME_LEN_MAX = 10;
	private static final int PHONE_NUM_LEN = 11;

	private static final int USER_PASSWORD_LEN_MIN = 8;
	private static final int USER_PASSWORD_LEN_MAX = 16;

	/**
	 * 检测手机号正确性
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean checkPhoneNum(String phone) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		// 验证手机号
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$");
		m = p.matcher(phone);
		b = m.matches();
		return b;
	}

	/**
	 * 检测登录账号。若为暮暮号（account），则为6-10位数字，否则为手机号。
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkUserName(String name) {
		boolean retVal = false;
		if (TextUtils.isEmpty(name)) {
			return retVal;
		}

		int len = name.length();
		if (len == PHONE_NUM_LEN) {
			return checkPhoneNum(name);
		} else if (len < USER_NAME_LEN_MIN || len > USER_NAME_LEN_MAX) {
			return retVal;
		} else {
			char ch;
			for (int i = 0; i < len; i++) {
				ch = name.charAt(i);
				if (i == 0 && ch == '0') {
					return retVal;
				}

				if (ch >= '0' && ch <= '9') {
					continue;
				} else {
					return retVal;
				}
			}
			retVal = true;
		}

		return retVal;
	}

	/**
	 * 检测登录密码的正确性
	 * 
	 * @param password
	 * @return
	 */
	public static boolean checkUserPassword(String password) {
		if (TextUtils.isEmpty(password)
				|| password.length() < USER_PASSWORD_LEN_MIN
				|| password.length() > USER_PASSWORD_LEN_MAX) {
			return false;
		}

		return true;
	}

	/**
	 * 检查有无版本升级
	 * 
	 * @param version
	 * @return
	 */
	public static boolean isUpdate(Context context, float version) {
		float currentVersion = 0;
		try {
			String versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
			String num = versionCode.substring(0, versionCode.length() - 2);
			currentVersion = Float.valueOf(num);
			System.out.println("版本号："+num);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			currentVersion = 0;
		}
		return version > currentVersion;
	}
}
