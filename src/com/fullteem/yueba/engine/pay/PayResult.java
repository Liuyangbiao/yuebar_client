package com.fullteem.yueba.engine.pay;

import android.text.TextUtils;

import com.fullteem.yueba.util.LogUtil;

public class PayResult {
	String resultStatus;
	String result;
	String memo;

	public PayResult(String rawResult) {
		try {
			String[] resultParams = rawResult.split(";");
			for (String resultParam : resultParams) {
				if (resultParam.startsWith("resultStatus")) {
					resultStatus = gatValue(resultParam, "resultStatus");
				}
				if (resultParam.startsWith("result")) {
					result = gatValue(resultParam, "result");
				}
				if (resultParam.startsWith("memo")) {
					memo = gatValue(resultParam, "memo");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean checkPayState() {
		boolean retVal = false;
		if (TextUtils.equals(resultStatus, "9000")) {
			retVal = true;
		}

		return retVal;
	}

	/*
	 * 1. when call alipay api to pay, the sync return status comes from ali
	 * server:
	 * “9000”则代表支付成功;非“9000”则代表可能支付失败--“8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
	 * 最终交易是否成功以服务端异步通知为准（小概率状态） 2. other return status comes from self server.
	 */
	public String getStatus() {
		String retVal = null;
		if (TextUtils.equals(resultStatus, "9000")) {
			
			retVal = "支付成功";
		} else if (TextUtils.equals(resultStatus, "8000")) {
			retVal = "支付结果确认中";
		} else if (TextUtils.equals(resultStatus, "200")) {
			retVal = "操作成功";
		} else if (TextUtils.equals(resultStatus, "500")) {
			retVal = "服务器出现问题";
		} else if (TextUtils.equals(resultStatus, "1005")) {
			retVal = "非法操作";
		} else if (TextUtils.equals(resultStatus, "3000")) {
			retVal = "支付宝验证失败";
		} else if (TextUtils.equals(resultStatus, "5000")) {
			retVal = "消息已处理";
		} else if (TextUtils.equals(resultStatus, "6000")) {
			retVal = "支付失败";
		} else if (TextUtils.equals(resultStatus, "6001")) {
			retVal = "取消支付";
		} else {
			// system may be busy, or other reasons.
			LogUtil.printPayLog("not valid status:" + resultStatus);
		}

		return retVal;
	}

	@Override
	public String toString() {
		return "resultStatus={" + resultStatus + "};memo={" + memo
				+ "};result={" + result + "}";
	}

	private String gatValue(String content, String key) {
		String prefix = key + "={";
		return content.substring(content.indexOf(prefix) + prefix.length(),
				content.lastIndexOf("}"));
	}
}
