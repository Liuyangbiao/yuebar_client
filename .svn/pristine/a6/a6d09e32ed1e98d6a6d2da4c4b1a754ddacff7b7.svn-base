package com.fullteem.yueba.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fullteem.yueba.model.UserCommonModel;

/**
 * 保存聊天对象信息
 * 
 * @author ssy
 * 
 */
public class DBFriendListDao {
	// 新增三个字段，分别为用户昵称和头像url，顺便加上两个个扩展字段
	public static final String TABLE_NAME = "contacters";
	public static final String COLUMN_NAME_USERID = "userid";
	public static final String COLUMN_NAME_NICKNAME = "nickname";
	public static final String COLUMN_NAME_IMG_URL = "imgurl";
	public static final String COLUMN_NAME_MOBILE = "mobile";
	public static final String COLUME_NAME_OTHER1 = "other1";
	public static final String COLUME_NAME_OTHER2 = "other2";
	private ContacterDBHelper dbHelper;

	public DBFriendListDao(Context context) {
		dbHelper = ContacterDBHelper.getInstance(context);
	}

	/**
	 * 保存联系人信息(联系人个人信息)
	 */
	public void saveContacter(UserCommonModel user) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME,
					COLUMN_NAME_MOBILE + "='" + user.getImServerId() + "'",
					null);
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME_USERID, user.getUserId());
			values.put(COLUMN_NAME_NICKNAME, user.getUserName());
			values.put(COLUMN_NAME_IMG_URL, user.getUserLogoUrl());
			values.put(COLUMN_NAME_MOBILE, user.getImServerId());
			db.replace(TABLE_NAME, null, values);
		}
	}

	/**
	 * 获取联系人基本信息
	 */
	public UserCommonModel getContacter(String imserverid) {
		UserCommonModel uc = new UserCommonModel();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
					+ " where " + COLUMN_NAME_MOBILE + " = " + "'" + imserverid
					+ "'", null);
			while (cursor.moveToNext()) {
				String userid = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_USERID));
				String nickName = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_NICKNAME));
				String imgurl = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_IMG_URL));
				uc.setUserLogoUrl(imgurl);
				uc.setUserId(userid);
				uc.setUserName(nickName);
				//uc.setUserMobile(imserverid);
				uc.setImServerId(imserverid);
			}
			cursor.close();
		}
		return uc;
	}

	/**
	 * 删除一个联系人
	 * 
	 * @param username
	 */
	public void deleteContact(String imserverid) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, COLUMN_NAME_MOBILE + " = ?",
					new String[] { imserverid });
		}
	}
}
