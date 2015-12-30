package com.fullteem.yueba.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DataBase {
	public final static String SHAPENAME = "yueba";
	public final static String HOTCITYVERSION_KEY = "hotcityversionkey";// 热门城市数据库版本key

	// boolean数据
	public static void saveBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(SHAPENAME,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public static boolean getBoolean(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SHAPENAME, 0);
		return sp.getBoolean(key, false);
	}

	// int数据
	public static void saveInt(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(SHAPENAME,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public static int getInt(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SHAPENAME, 0);
		return sp.getInt(key, 0);
	}

	// long数据
	public static void saveLong(Context context, String key, long value) {
		SharedPreferences sp = context.getSharedPreferences(SHAPENAME,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putLong(key, value);
		edit.commit();
	}

	public static long getLong(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SHAPENAME, 0);
		return sp.getLong(key, 0);
	}

	// String数据
	public static void saveString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(SHAPENAME,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static String getString(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(SHAPENAME, 0);
		return sp.getString(key, "");
	}
}
