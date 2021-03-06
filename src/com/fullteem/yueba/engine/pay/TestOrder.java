package com.fullteem.yueba.engine.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.fullteem.yueba.model.OrderPayModel;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SignUtil;

/*for test purpose only*/
public class TestOrder extends BaseOrder {

	public TestOrder(OrderPayModel orderPayModel) {
		super(orderPayModel);
		// TODO Auto-generated constructor stub
	}

	public static final String PARTNER = "2088711729468081";
	public static final String SELLER = "borui2014@boruibrothers.com";
	public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMUZJgT/I7Lzm2pbvF35PzExKwTW1i0uK94vUAENt+UeY2ausMqTHFkMyMOj6Qt8pzr6adZb4YTHYgN7Hce+Ok3sBDTOfpIq7TCyTO77OsAf6SJfKuJhjlksnu1IaPMzEXE03LNrxH6HUTnIHD3DrnhLVKjy/kwtAuJnTxVeWDf7AgMBAAECgYEAhifX+Q6w/d1Sq9JFJLHCkXkmDuUsYxRKR7Fsg0sTIKDOh9XgAAKiCPrqrD+lIGx4Z2yLHZzso7QSwsvKOWsBw1UX/J4xGcCRkshAQSV2sKso16sftWVMEwKWcms5B9+AJZwdqPfU7eROWPiKhJxE1bzHLHTI3J49IhEdy4feHgECQQD14Y2vox9J7BwlNEw5F2EgaznjuABDj4ebllLsJ4CySMORx0dXQ3S+WMpsjYV9aSJYIKpGsjsAtI8tcQdFGfUlAkEAzTWmcTwXgaC9Wwkn3hlTyPybPLEiuy+2TZT4YGnsTVFHvGhmcoq9D4LrGCPa8b2E9COY9RTfu2aQez7a1FI+nwJBAPDhQzkqCWEWGoyoK3RS3ygvY8sfW8LUPfnCzwHjwUTn3BBYth9bSmef/M9T5c7yzF6hwa74tK0ANrRB4uljgLECQQCqXmJjHGq/mj3bOMy6nfhrox0W1FFrav9Fhep30Tj4MAUIrPxxGDJCkISyNAJwNNIPBwbUYpIlOc+2Isb3A5ktAkAmotiwGNHSM7nMtbtTw87c5/SP6EVa2wmpsYlmG6k18yd+xpoBavPiZAyQ4EUGoxm2si3rFXxAgbtARn/UolsP";

	@Override
	public String getOrder() {

		String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		return payInfo;

	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtil.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	@Override
	protected String generateOrder() {
		// TODO Auto-generated method stub
		String retVal = getOrder();
		LogUtil.printPayLog("order string for ali pay is:" + retVal);
		return retVal;
	}

}
