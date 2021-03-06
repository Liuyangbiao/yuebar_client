package com.fullteem.yueba.engine.pay;

import java.text.DecimalFormat;

import android.text.TextUtils;

import com.fullteem.yueba.util.LogUtil;

public class PayUtil {

	public static final boolean supportPayContextEncrypt = true;

	public static final String DES_ALGORITHM = "DES";

	public static final String MUMU_KEY = "m2u0m1u4";
	public static final String partner = "2088711729468081";
	public static final String seller = "borui2014@boruibrothers.com";
	public static final String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMUZJgT/I7Lzm2pbvF35PzExKwTW1i0uK94vUAENt+UeY2ausMqTHFkMyMOj6Qt8pzr6adZb4YTHYgN7Hce+Ok3sBDTOfpIq7TCyTO77OsAf6SJfKuJhjlksnu1IaPMzEXE03LNrxH6HUTnIHD3DrnhLVKjy/kwtAuJnTxVeWDf7AgMBAAECgYEAhifX+Q6w/d1Sq9JFJLHCkXkmDuUsYxRKR7Fsg0sTIKDOh9XgAAKiCPrqrD+lIGx4Z2yLHZzso7QSwsvKOWsBw1UX/J4xGcCRkshAQSV2sKso16sftWVMEwKWcms5B9+AJZwdqPfU7eROWPiKhJxE1bzHLHTI3J49IhEdy4feHgECQQD14Y2vox9J7BwlNEw5F2EgaznjuABDj4ebllLsJ4CySMORx0dXQ3S+WMpsjYV9aSJYIKpGsjsAtI8tcQdFGfUlAkEAzTWmcTwXgaC9Wwkn3hlTyPybPLEiuy+2TZT4YGnsTVFHvGhmcoq9D4LrGCPa8b2E9COY9RTfu2aQez7a1FI+nwJBAPDhQzkqCWEWGoyoK3RS3ygvY8sfW8LUPfnCzwHjwUTn3BBYth9bSmef/M9T5c7yzF6hwa74tK0ANrRB4uljgLECQQCqXmJjHGq/mj3bOMy6nfhrox0W1FFrav9Fhep30Tj4MAUIrPxxGDJCkISyNAJwNNIPBwbUYpIlOc+2Isb3A5ktAkAmotiwGNHSM7nMtbtTw87c5/SP6EVa2wmpsYlmG6k18yd+xpoBavPiZAyQ4EUGoxm2si3rFXxAgbtARn/UolsP";

	public static final boolean testMode = true;

	public static final String ORDER_STATUS_UN_PAYED = "0";
	public static final String ORDER_STATUS_PAYED_UN_CONSUME = "10";
	public static final String ORDER_STATUS_CONSUMED = "7000";
	public static final String ORDER_STATUS_REFUNDED = "5000";
	public static final String ORDER_STATUS_REFUNDING = "4000";

	public static boolean checkContext(String seller, String partner, String key) {
		boolean retVal = false;
		if (TextUtils.equals(seller, PayUtil.seller)
				&& TextUtils.equals(partner, PayUtil.partner)
				&& TextUtils.equals(key, PayUtil.privateKey)) {
			retVal = true;
		}
		return retVal;

	}

	public static String roundPrice(float price) {
		String retVal = null;

		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		retVal = decimalFormat.format(price);

		LogUtil.printPayLog("after round, value is " + retVal);
		return retVal;
	}

	public static String roundPrice(String orderNum, String unitPrice) {
//		LogUtil.printPayLog("roundPrice--num:" + orderNum + " unit price:"
//				+ unitPrice);
		int num = Integer.parseInt(orderNum);
		float price = Float.valueOf(unitPrice);
		float amount = num * (price);

		return roundPrice(amount);
	}
}
