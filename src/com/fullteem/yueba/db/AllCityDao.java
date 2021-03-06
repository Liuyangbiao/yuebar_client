package com.fullteem.yueba.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.model.City;
import com.fullteem.yueba.util.FileUtils;
import com.fullteem.yueba.util.LogUtil;

public class AllCityDao {
	private static String TAG = "AllCityDao";
	private static boolean isLog = true;
	// -------------
	public static final String ALL_CITYDB_NAME = "city.db";
	private static String sdcardPath = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + File.separator
			+ "com.fullteem.yueba" + File.separator + ALL_CITYDB_NAME;
	private static String assetPath = "city/city.db";
	public static final String CITY_TABLE_NAME = "city";
	private static SQLiteDatabase db;

	/**
	 * 获取所有的城市
	 * 
	 * @return
	 */
	public static List<City> getAllCity() {
		List<City> citiesList = new ArrayList<City>();
		Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME, null);
		int preID = -1;
		City city;
		while (c.moveToNext()) {
			// LogUtil.d(
			// TAG,
			// "getColumnCount:" + c.getColumnCount() + "---"
			// + c.getString(3), isLog);
			int CID = c.getInt(c.getColumnIndex("CID"));
			if (CID != preID) {
				String PName = c.getString(c.getColumnIndex("PName"));
				int PID = c.getInt(c.getColumnIndex("PID"));
				String CPinyin = c.getString(c.getColumnIndex("CPinyin"));
				String CName = c.getString(c.getColumnIndex("CName"));
				preID = CID;
				city = new City();
				city.setPID(PID);
				city.setPName(PName);
				city.setCID(CID);
				city.setCName(CName);
				city.setCPinyin(CPinyin);
				citiesList.add(city);
				// LogUtil.d(TAG, city.toString(), isLog);
			}
		}
		db.close();
		return citiesList;
	}

	/**
	 * 检查数据库是否存在
	 * 
	 * @return
	 */
	public static boolean isCityDbExit() {
		File file = new File(sdcardPath);
		if (!file.exists()) {
			return false;
		}

		LogUtil.LogDebug(TAG, file.getAbsolutePath(), isLog);
		return true;
	}

	/**
	 * 让数据库存在内存
	 */
	public static boolean letCityDbExit(Context context) {
		return FileUtils.carryAssetToCard(context, assetPath, sdcardPath);
	}

	/**
	 * 根据城市名称获取城市
	 * 
	 * @param cityName
	 * @return
	 */
	public static City getCityByName(String cityName) {
		if (AppContext.citiesList != null && AppContext.citiesList.size() > 0) {
			for (City city : AppContext.citiesList) {
				// LogUtil.d(TAG, cityName + "匹配" + city.getCName(),
				// isLog);
				if (cityName.equals(city.getCName())) {
					return city;
				}
			}
		}
		return null;
	}
	
	/**
	 * 一些初始化在前面的东西（必须初始化）
	 * 
	 * @param context
	 */
	public static void initPre(Context context) {
		if (!isCityDbExit()) {
			LogUtil.LogDebug(TAG, "save" + letCityDbExit(context), isLog);
		}
		LogUtil.LogDebug(TAG, "nosave", isLog);
		db = context.openOrCreateDatabase(sdcardPath, Context.MODE_PRIVATE,
				null);
	}
}
