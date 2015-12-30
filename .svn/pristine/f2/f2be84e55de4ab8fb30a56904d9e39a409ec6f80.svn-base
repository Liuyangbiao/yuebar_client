package com.fullteem.yueba.util;

import android.content.Context;

public class AppUtil {

	/**
	 * 解析城市名称
	 * 
	 * @param cName
	 * @return
	 */
	public static String parceCityName(String cName) {
		String resCName = cName;
		if (cName.contains("市")) {// 如果为空就去掉市字再试试
			String subStr[] = cName.split("市");
			resCName = subStr[0];
		} else if (cName.contains("县")) {// 或者去掉县字再试试
			String subStr[] = cName.split("县");
			resCName = subStr[0];
		}
		return resCName;
	}

	/**
	 * 返回当次请求的page号
	 * 
	 * @param listSize
	 * @param pageNum
	 * @return
	 */
	public static int getPageNum(int listSize, int pageNum) {
		int num;
		if (listSize == 0) {
			num = 1;
			return num;
		}
		if (listSize >= pageNum) {
			num = listSize / pageNum + 1;
		} else {
			num = 1;
		}
		return num;
	}

	/**
	 * dip转px
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px传dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
