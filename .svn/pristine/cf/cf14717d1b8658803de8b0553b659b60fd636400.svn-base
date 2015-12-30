package com.fullteem.yueba.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.format.Time;

public class TimeUtil {
	public static DateFormat dateFormat = null;

	/**
	 * 获取当天日期
	 * 
	 * @return 年-月-日
	 */
	public static String getTodayData() {
		Time time = new Time("GMT+8");
		time.setToNow();
		int year = time.year;
		int month = time.month + 1;
		int day = time.monthDay;
		return year + "-" + month + "-" + day + "";
	}

	/**
	 * 日期对象转换成 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTime(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 当前日期和时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentDateTime() {
		return getDateTime(new Date(System.currentTimeMillis()));
	}

	public static String format(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(date);
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 判断是否为合法的日期时间字符串
	 * 
	 * @param str_input
	 * @param str_input
	 * @return boolean;符合为true,不符合为false
	 */
	public static boolean isDate(String str_input, String rDateFormat) {
		if (!isNull(str_input)) {
			SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
			formatter.setLenient(false);
			try {
				formatter.format(formatter.parse(str_input));
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		return false;
	}

	public static boolean isNull(String str) {
		if (str == null)
			return true;
		else
			return false;
	}

	/**
	 * 时间字符串传入，获取时间戳
	 * 
	 * @param dateNow
	 *            如：2004/12/11 00:00:00
	 * @return 时间戳
	 */
	public static long getTimestamp(String date) {
		try {
			Date dateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
			// Date dateRefer = new
			// SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("1970/01/01 08:00:00");
			// long lontTime = dateNow.getTime() - dateRefer.getTime() > 0 ?
			// dateNow.getTime() - dateRefer.getTime() : dateRefer.getTime() -
			// dateNow.getTime();
			// long rand = (int) (Math.random() * 1000);
			return dateNow.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			LogUtil.LogError("TimeUtil",
					"getTimestamp()" + date + "转换失败" + e.toString(), null);
		}
		return -1;
	}

}
