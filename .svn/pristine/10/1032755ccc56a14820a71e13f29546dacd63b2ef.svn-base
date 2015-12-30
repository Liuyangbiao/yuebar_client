package com.fullteem.yueba.util;

import java.util.Random;

public class MathUtil {

	public static Random random = new Random();

	/** 获取某一区间的整数 **/
	public static int getRandomIntNumber(int min, int max) {
		return Math.abs(random.nextInt() % (max - min + 1)) + min;
	}

	/** 判断一个数是否在某一区间 */
	public static boolean numberIsBetween(int min, int max, int num) {
		if (num >= min && num <= max) {
			return true;
		}

		return false;
	}
}
