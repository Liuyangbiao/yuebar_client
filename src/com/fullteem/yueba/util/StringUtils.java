package com.fullteem.yueba.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.fullteem.yueba.R;

/**
 * 字符串操作工具包
 * 
 * @author allen
 * @version 1.0
 * @created 2014-4-16
 */
@SuppressLint({ "SimpleDateFormat", "ResourceAsColor" })
public class StringUtils {

	public static Pattern shuzi = Pattern.compile("[0-9]*");
	public static Pattern zimu = Pattern.compile("[a-zA-Z]*");
	public static Pattern hanzi = Pattern.compile("[\u4e00-\u9fa5]*");
	private final static Pattern EMAILER = Pattern
			.compile("^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,}([\\.][A-Za-z]{2,})?$");
	private final static Pattern PHONE_NUM = Pattern
			.compile("^((13[0-9])|(15[0-9])|(18[0-9])|17[0-9]|16[0-9]|19[0-9])\\d{8}$");

	// 只能是数字,英文字母和中文
	public static boolean isValidTagAndAlias(String s) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
	}

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(Date sdate) {
		Date time = sdate;
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	// /**
	// * 判断是不是一个合法的电子邮件地址
	// *
	// * @param email
	// * @return
	// */
	// public static boolean isEmail(String email) {
	// if (email == null || email.trim().length() == 0)
	// return false;
	// return EMAILER.matcher(email).matches();
	// }

	/**
	 * 验证邮箱格式是否正确
	 * 
	 * @param strEmailAdr邮箱地址
	 *            是邮箱格式返回true，否则返回false
	 * */
	public static boolean isEmail(String strEmailAdr) {
		if (TextUtils.isEmpty(strEmailAdr))
			return false;

		// 验证邮箱的正则表达式
		// String format =
		// "\\p{Alpha}\\w{2,15}[@][a-z0-9_-]{2,}[.]\\p{Lower}{2,}";
		// //如QQ邮箱可能数字开头，所以\\p{Alpha}不能加
		return EMAILER.matcher(strEmailAdr).matches();
	}

	/**
	 * 验证手机号格式是否正确
	 * */
	public static boolean isPhoneNum(String strPhoneNum) {
		if (TextUtils.isEmpty(strPhoneNum))
			return false;
		return PHONE_NUM.matcher(strPhoneNum).matches();
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 字符串数组转List
	 * 
	 * @param data
	 * @return
	 */
	public static List<String> stringToList(String[] data) {
		if (data == null || data.length == 0) {
			return null;
		}
		List<String> res = new ArrayList<String>();
		for (int i = 0; i < data.length; i++) {
			res.add(data[i]);
		}

		return res;
	}

	public static String getIMEI(Context context) {
		return ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	/**
	 * 判别手机是否为正确手机号码； 号码段分配如下：
	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
	 */
	public static boolean isMobileNum(String mobiles) {
		Pattern p = Pattern
				.compile("^((\\(\\d{3}\\))|(\\d{3}\\-))?13[0-9]\\d{8}|15[89]\\d{8}");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;

		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";

		String expression2 = "^\\(?(\\d{2})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";

		CharSequence inputStr = phoneNumber;

		Pattern pattern = Pattern.compile(expression);

		Matcher matcher = pattern.matcher(inputStr);

		Pattern pattern2 = Pattern.compile(expression2);

		Matcher matcher2 = pattern2.matcher(inputStr);
		if (matcher.matches() || matcher2.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumberValid2(String phoneNumber) {
		boolean isValid = false;

		String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
		CharSequence inputStr = phoneNumber;

		Pattern pattern = Pattern.compile(expression);

		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches()) {
			isValid = true;
		}

		return isValid;

	}

	/**
	 * 把毫秒转化为制定格式的时间
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String getTime(long time, String format) {
		DateFormat formatter = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}

	/**
	 * 把毫秒转化为制定格式的时间
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String getTime(String time, String format) {
		DateFormat formatter = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();

		try {
			calendar.setTimeInMillis(Long.valueOf(time));
			return formatter.format(calendar.getTime());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 时间转化毫秒
	 * 
	 * @param dt
	 * @return
	 */
	public static long getMillisecond(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d = null;
		try {
			d = sdf.parse(str);
			return d.getTime();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	/** 字符串是否为空或者为"" */
	public static boolean isStringNone(String str) {
		if (str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
			return true;
		}

		return false;
	}

	// 格式：2014-05-29 08:00:00
	public static String formetDateTime(int year, int mounth, int day,
			int hour, int minute) {
		StringBuffer sb = new StringBuffer();
		sb.append(year + "-");
		if (mounth < 10) {
			sb.append("0" + mounth + "-");
		} else {
			sb.append(mounth + "-");
		}

		if (day < 10) {
			sb.append("0" + day + " ");
		} else {
			sb.append(day + " ");
		}

		if (hour < 10) {
			sb.append("0" + hour + ":");
		} else {
			sb.append(hour + ":");
		}

		if (minute < 10) {
			sb.append("0" + hour + ":00");
		} else {
			sb.append(hour + ":00");
		}
		return sb.toString();

	}

	/**
	 * 性别
	 * 
	 * @author jun
	 */
	public enum Gender {
		GENDER_BOY, GENDER_GIRL
	}

	/**
	 * * 修改男女style样式
	 * 
	 * @param tv
	 * @param type
	 *            1:男 2:女
	 */
	public static void changeStyle(Context context, TextView tv, Gender gender) {
		Drawable drawable = null;
		if (gender == Gender.GENDER_BOY) {
			drawable = context.getResources().getDrawable(R.drawable.sex_boy);
			tv.setEnabled(false); // 背景选择器更换颜色，setBackground后文字出现问题
		} else {
			drawable = context.getResources().getDrawable(R.drawable.sex_girl);
			tv.setEnabled(true);
		}
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		tv.setCompoundDrawables(drawable, null, null, null);
	}

	/**
	 * 按照strings.xml文件内部的format格式
	 */
	public static String formatStrD2Str(Context context, int res, Object... num) {
		String sFormat = context.getResources().getString(res);
		String sFinalNum = String.format(sFormat, num);
		return sFinalNum;
	}

	/**
	 * 是否需要加载更多，原理：默认一页有10个数据，若不满10个则说明没有更多
	 * 
	 * @param listSize
	 * @param pageNum
	 * @return
	 */
	public static boolean isLoadMoreVisible(int listSize, int pageNum) {
		if (listSize % pageNum > 0) {
			return false;
		} else
			return true;
	}
}
