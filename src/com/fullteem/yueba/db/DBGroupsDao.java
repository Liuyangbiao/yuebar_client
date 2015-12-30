package com.fullteem.yueba.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fullteem.yueba.model.Group;

/**
 * 群组Dao
 * 
 * @author ssy
 * 
 */
public class DBGroupsDao {
	public static final String TABLE_NAME = "groups";
	public static final String COLUMN_NAME_GROUPName = "groupname";
	public static final String COLUMN_NAME_GROUPID = "groupid";
	public static final String COLUMN_NAME_GROUPIDIMG_URL = "imgurl";
	public static final String COLUME_NAME_OTHER1 = "other1";
	public static final String COLUME_NAME_OTHER2 = "other2";
	private GroupDBHelper dbHelper;

	public DBGroupsDao(Context context) {
		dbHelper = GroupDBHelper.getInstance(context);
	}

	/**
	 * 保存群组信息
	 */
	public synchronized void saveGroup(final Group group) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				if (db.isOpen()) {
					db.delete(TABLE_NAME,
							COLUMN_NAME_GROUPID + "='" + group.getGroupId()
									+ "'", null);
					ContentValues values = new ContentValues();
					values.put(COLUMN_NAME_GROUPID, group.getGroupId());
					values.put(COLUMN_NAME_GROUPIDIMG_URL, group.getGroupIcon());
					values.put(COLUMN_NAME_GROUPName, group.getGroupName());
					db.replace(TABLE_NAME, null, values);
				}
			}
		}).start();
	}

	/**
	 * 获取群组基本信息
	 */
	public Group getGroup(String groupId) {
		Group group = new Group();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
					+ " where " + COLUMN_NAME_GROUPID + " = " + "'" + groupId
					+ "'", null);
			while (cursor.moveToNext()) {
				String groupid = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_GROUPID));
				String groupName = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_GROUPName));
				String imgurl = cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_GROUPIDIMG_URL));
				group.setGroupId(groupid);
				group.setGroupIcon(imgurl);
				group.setGroupName(groupName);
			}
			cursor.close();
		}
		return group;
	}

	/**
	 * 删除一个群信息
	 * 
	 * @param username
	 */
	public void deleteContact(String groupId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(TABLE_NAME, COLUMN_NAME_GROUPID + " = ?",
					new String[] { groupId });
		}
	}

}
