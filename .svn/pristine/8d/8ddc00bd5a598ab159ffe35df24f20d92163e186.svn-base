package com.fullteem.yueba.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContacterDBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static ContacterDBHelper instance;

	private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
			+ DBFriendListDao.TABLE_NAME + " ("
			+ DBFriendListDao.COLUMN_NAME_USERID + " TEXT, "
			+ DBFriendListDao.COLUMN_NAME_NICKNAME + " TEXT, "
			+ DBFriendListDao.COLUMN_NAME_IMG_URL + " TEXT, "
			+ DBFriendListDao.COLUME_NAME_OTHER1 + " TEXT, "
			+ DBFriendListDao.COLUME_NAME_OTHER2 + " TEXT, "
			+ DBFriendListDao.COLUMN_NAME_MOBILE + " TEXT PRIMARY KEY);";

	public ContacterDBHelper(Context context) {
		super(context, getUserDatabaseName(), null, DATABASE_VERSION);
	}

	private static String getUserDatabaseName() {
		return "contacters.db";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(USERNAME_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public static ContacterDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new ContacterDBHelper(context.getApplicationContext());
		}
		return instance;
	}

	public void closeDB() {
		if (instance != null) {
			try {
				SQLiteDatabase db = instance.getWritableDatabase();
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			instance = null;
		}
	}

}
