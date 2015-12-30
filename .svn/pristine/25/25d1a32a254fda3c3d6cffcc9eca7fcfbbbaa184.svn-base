package com.fullteem.yueba.db;

import java.util.List;

import android.content.Context;

import com.fullteem.yueba.model.City;
import com.simple.util.db.operation.TemplateDAO;

public class HotCityDao extends TemplateDAO<City> {
	private static HotCityDao mInstance;
	// public static final String hotCityTableName = "t_user";
	private int currentVersion = 5;// 当前版本

	private HotCityDao(Context context) {
		super(new DBHelper(context));
	}

	public static HotCityDao getInstance(Context context) {
		// 双重锁定
		if (mInstance == null) {
			synchronized (HotCityDao.class) {
				if (mInstance == null) {
					mInstance = new HotCityDao(context);
				}
			}
		}

		return mInstance;
	}

	/**
	 * 获取所有的热门城市
	 * 
	 * @return
	 */
	public List<City> getAllHotCity() {
		List<City> hotCitiesList = find();
		return hotCitiesList;
	}

	/**
	 * 检查是否需要跟新
	 * 
	 * @return
	 */
	public boolean checkIsNeedUpdate(Context context) {
		int oldVersion = DataBase.getInt(context, DataBase.HOTCITYVERSION_KEY);
		if (currentVersion > oldVersion) {
			return true;
		}
		return false;
	}

	/**
	 * 添加热门城市
	 * 
	 * @param hotCities
	 */
	public void addHotCity(List<City> hotCities, Context context) {
		for (City entity : hotCities) {
			insert(entity);
		}
		DataBase.saveInt(context, DataBase.HOTCITYVERSION_KEY, currentVersion);
	}
}
