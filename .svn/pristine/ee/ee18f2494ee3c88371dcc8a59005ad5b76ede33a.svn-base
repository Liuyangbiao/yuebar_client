package com.fullteem.yueba.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupDBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static GroupDBHelper instance;

	private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
			+ DBGroupsDao.TABLE_NAME + " (" + DBGroupsDao.COLUMN_NAME_GROUPName
			+ " TEXT, " + DBGroupsDao.COLUMN_NAME_GROUPIDIMG_URL + " TEXT, "
			+ DBGroupsDao.COLUME_NAME_OTHER1 + " TEXT, "
			+ DBGroupsDao.COLUME_NAME_OTHER2 + " TEXT, "
			+ DBGroupsDao.COLUMN_NAME_GROUPID + " TEXT PRIMARY KEY);";

	public GroupDBHelper(Context context) {
		super(context, getUserDatabaseName(), null, DATABASE_VERSION);
	}

	private static String getUserDatabaseName() {
		return DBGroupsDao.TABLE_NAME + ".db";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(USERNAME_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public static GroupDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new GroupDBHelper(context.getApplicationContext());
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
