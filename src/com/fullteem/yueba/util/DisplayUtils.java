package com.fullteem.yueba.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.net.Urls;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月26日&emsp;上午11:15:00
 * @use 显示类工具，如获取屏幕宽高等
 */
public class DisplayUtils {
	/**
	 * @param context
	 * @return 屏幕高度
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

	/**
	 * @param context
	 * @return 屏幕宽度
	 */
	public static int getScreenWidht(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	/**
	 * @param context
	 * @param dpValue
	 *            dp单位的待转换值
	 * @return px
	 */
	public static int dp2px(Context context, int dpValue) {
		DisplayMetrics metrics = new DisplayMetrics();
		metrics = context.getResources().getDisplayMetrics();
		return (int) (dpValue * metrics.density + 0.5f); // dp值x像素密度，取四舍五入值
	}

	/**
	 * 去掉float转string后多余的.与0
	 * 
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {

			// 去掉多余的0
			s = s.replaceAll("0+?$", "");
			// 如最后一位是.则去掉
			s = s.replaceAll("[.]$", "");
		}
		return s;
	}

	/**
	 * 200毫秒后将view背景设置为resid
	 * 
	 * @param view
	 * @param resid
	 */
	public static void resetBack(final View view, final int resid) {
		Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (view != null) {
					view.setBackgroundResource(resid);
				}
			}
		};
		mHandler.sendEmptyMessageDelayed(0, 200);
	}

	/**
	 * 改变关注TextView的左边图标
	 * 
	 * @param viewPraise
	 *            textView
	 * @param isPraise
	 *            是否关注
	 */
	public static void praiseChanged(TextView viewPraise, boolean isPraise) {
		Drawable drawablePraise = isPraise ? viewPraise.getContext()
				.getResources().getDrawable(R.drawable.singer_praise_icon)
				: viewPraise.getContext().getResources()
						.getDrawable(R.drawable.singer_unpraise_icon);
		drawablePraise.setBounds(0, 0, drawablePraise.getMinimumWidth(),
				drawablePraise.getMinimumHeight());
		viewPraise.setCompoundDrawables(drawablePraise,
				viewPraise.getCompoundDrawables()[1],
				viewPraise.getCompoundDrawables()[2],
				viewPraise.getCompoundDrawables()[3]);
	}

	/**
	 * 根据魅力值获取等级对应图标 * {@value}
	 * 等级分为木牌(<300)，铁牌(<1000)，铜牌(<2000)，银牌(<5000)，金牌（<9000)，一钻（<10000），二钻（<
	 * 20000），三钻（<30000），四钻（<40000），五钻（<50000），皇冠等，不同的魅力值计算可以获得不同的等级
	 * 
	 * @param charm
	 *            魅力值
	 * @return 魅力值对应图片资源
	 */
	public static int getLevelRes(int charm) {
		return charm < 300 ? R.drawable.img_user_level_wood
				: charm < 1000 ? R.drawable.img_user_level_fe
						: charm < 2000 ? R.drawable.img_user_level_cu
								: charm < 5000 ? R.drawable.img_user_level_ag
										: charm < 900 ? R.drawable.img_user_level_au
												: charm < 10000 ? R.drawable.img_user_level_diamond1
														: charm < 20000 ? R.drawable.img_user_level_diamond2
																: charm < 30000 ? R.drawable.img_user_level_diamond3
																		: charm < 40000 ? R.drawable.img_user_level_diamond4
																				: charm < 50000 ? R.drawable.img_user_level_diamond5
																						: R.drawable.img_user_level_crown;

	}

	/**
	 * 根据传进来的路径返回绝对路径、传绝对路径则不变
	 * 
	 * @param relativeUrl
	 *            原url
	 * @param defaultImg
	 *            默认图
	 * @return
	 */
	public static String getAbsolutelyUrl(String relativeUrl,
			Integer... defaultImg) {
		if (TextUtils.isEmpty(relativeUrl))
			return defaultImg == null || defaultImg.length <= 0 ? "drawable://"
					+ R.drawable.img_loading_empty : "drawable://"
					+ defaultImg[0];

		// 包含drawable或者file的本地图片
		if (relativeUrl.contains("drawable://")
				|| relativeUrl.contains("file://"))
			return relativeUrl;

		// 不包含http://或者ftp://
		if (!(relativeUrl.contains("http://") || relativeUrl.contains("ftp://")))
			if ("/".equals(relativeUrl.substring(0, 1))) {
				relativeUrl = relativeUrl.substring(1, relativeUrl.length());
				return Urls.IMAGEHOST + relativeUrl;
			}

		if (relativeUrl.length() <= 7)// 错误地址
			return defaultImg == null || defaultImg.length <= 0 ? "drawable://"
					+ R.drawable.img_loading_fail
					: defaultImg.length >= 2 ? "drawable://" + defaultImg[1]
							: "drawable://" + defaultImg[0];

		// 包含了看下前缀是不是http开头或者ftp)
		String urlHead = relativeUrl.substring(0, 6);
		if (!("http:/".equals(urlHead) || "ftp://".equals(urlHead)))
			if ("/".equals(relativeUrl.substring(0, 1))) {
				relativeUrl = relativeUrl.substring(1, relativeUrl.length());
				return Urls.IMAGEHOST + relativeUrl;
			}

		return relativeUrl;
	}
}
