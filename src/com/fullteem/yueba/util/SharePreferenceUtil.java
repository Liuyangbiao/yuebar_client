/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fullteem.yueba.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

public class SharePreferenceUtil {

	/**
	 * 保存Preference的name
	 */
	public static final String PREFERENCE_NAME = "saveInfo";
	private static SharedPreferences mSharedPreferences;
	private static SharePreferenceUtil mPreferenceUtils;
	private static SharedPreferences.Editor editor;

	private String SHARED_KEY_SETTING_NOTIFICATION = "shared_key_setting_notification";
	private String SHARED_KEY_SETTING_SOUND = "shared_key_setting_sound";
	private String SHARED_KEY_SETTING_VIBRATE = "shared_key_setting_vibrate";
	private String SHARED_KEY_SETTING_SPEAKER = "shared_key_setting_speaker";
	private String SHARED_KEY_SETTING_CITYNAME = "shared_key_setting_cityname";
	private String SHARED_KEY_SETTING_CITYID = "shared_key_setting_citId";

	private SharePreferenceUtil(Context cxt) {
		mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * 单例模式，获取instance实例
	 * 
	 * @param cxt
	 * @return
	 */
	public static SharePreferenceUtil getInstance(Context cxt) {
		if (mPreferenceUtils == null) {
			mPreferenceUtils = new SharePreferenceUtil(cxt);
		}
		editor = mSharedPreferences.edit();
		return mPreferenceUtils;
	}

	public void setSettingMsgNotification(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_NOTIFICATION, paramBoolean);
		editor.commit();
	}

	public boolean getSettingMsgNotification() {
		return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_NOTIFICATION,
				true);
	}

	public void setSettingMsgSound(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_SOUND, paramBoolean);
		editor.commit();
	}

	public boolean getSettingMsgSound() {

		return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SOUND, true);
	}

	public void setSettingMsgVibrate(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_VIBRATE, paramBoolean);
		editor.commit();
	}

	public boolean getSettingMsgVibrate() {
		return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_VIBRATE, true);
	}

	public void setSettingMsgSpeaker(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_SPEAKER, paramBoolean);
		editor.commit();
	}

	public boolean getSettingMsgSpeaker() {
		return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SPEAKER, true);
	}

	public void setSettingCityName(String paramString) {
		editor.putString(SHARED_KEY_SETTING_CITYNAME, paramString);
		editor.commit();
	}

	public String getSettingCityName() {
		return mSharedPreferences.getString(SHARED_KEY_SETTING_CITYNAME, "北京");
	}

	public void setSettingCityId(String paramString) {
		editor.putString(SHARED_KEY_SETTING_CITYID, paramString);
		editor.commit();
	}

	public String getSettingCityId() {
		return mSharedPreferences.getString(SHARED_KEY_SETTING_CITYID, "11");
	}

	/**
	 * 提取储存数据对象
	 * 
	 * @param key
	 * @param object
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getObjectFromShare(Context context, String name,
			String key) {
		try {
			SharedPreferences sp = context.getSharedPreferences(name,
					Context.MODE_PRIVATE);
			String payCityMapBase64 = sp.getString(key, "");
			if (payCityMapBase64.length() == 0) {
				return null;
			}
			byte[] base64Bytes = Base64
					.decode(payCityMapBase64, Base64.DEFAULT);
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (T) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 存储对象到sp公共方法,注：若要使用这个方法bean类请务必继承 Serializable接口进行数列化 add by ssy
	 * 2014.09.07
	 * 
	 * @param context
	 * @param name
	 * @param key
	 * @param t
	 * @return
	 */

	public static <T> boolean saveObjectToShare(Context context, String name,
			String key, T t) {
		try {
			SharedPreferences sp = context.getSharedPreferences(name,
					Context.MODE_PRIVATE);
			// 存储
			Editor editor = sp.edit();
			if (t == null) {
				editor.putString(key, "");
				editor.commit();
				return true;
			}
			ByteArrayOutputStream toByte = new ByteArrayOutputStream();
			ObjectOutputStream oos;

			oos = new ObjectOutputStream(toByte);
			oos.writeObject(t);
			// 对byte[]进行Base64编码
			String payCityMapBase64 = new String(Base64.encode(
					toByte.toByteArray(), Base64.DEFAULT));

			editor.putString(key, payCityMapBase64);
			editor.commit();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 保存bool类型到sharepreferences
	 */
	public void saveBooleanToShare(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 根据key从sharepreferences得到bool类型
	 */
	public boolean getBooleanFromShare(String key, boolean defValue) {
		return mSharedPreferences.getBoolean(key, defValue);
	}

	/**
	 * 保存int类型到sharepreferences
	 */
	public void saveIntToShare(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 根据key从sharepreferences得到int类型
	 */
	public int getIntFromShare(String key, int defValue) {
		return mSharedPreferences.getInt(key, defValue);
	}

	/**
	 * 保存String类型到sharepreferences
	 */
	public void saveStringToShare(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 根据key从sharepreferences得到String类型
	 */
	public String getStringFromShare(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}

}
