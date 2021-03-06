package com.fullteem.yueba.engine.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.fullteem.yueba.model.OrderPayModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SignUtil;

public class AliOrder extends BaseOrder {

	boolean isTest = false;

	public AliOrder(OrderPayModel orderPayModel) {
		super(orderPayModel);
	}

	@Override
	protected String generateOrder() {
		String retVal = doCreateOrder(partner, seller, privateKey);
		LogUtil.printPayLog("order string for ali pay is:" + retVal);
		return retVal;
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String doCreateOrder(String partner, String seller,
			String privateKey) {
		String orderInfo = getOrderInfo(partner, seller);
		String sign = SignUtil.sign(orderInfo, privateKey);

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

	private String getOrderInfo(String partner, String seller) {
		String orderInfo;
		if (isTest) {

			// 合作者身份ID
			orderInfo = "partner=" + "\"" + PayUtil.partner + "\"";

			// 卖家支付宝账号
			orderInfo += "&seller_id=" + "\"" + PayUtil.seller + "\"";

			// 商户网站唯一订单号

			orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

			// 商品名称
			String subject = "测试的商品";
			orderInfo += "&subject=" + "\"" + subject + "\"";

			// 商品详情
			String body = "该测试商品的详细描述";
			orderInfo += "&body=" + "\"" + body + "\"";

			// 商品金额
			String price = "0.01";
			orderInfo += "&total_fee=" + "\"" + price + "\"";

			// 服务器异步通知页面路径
			orderInfo += "&notify_url=" + "\""
					+ "http://notify.msp.hk/notify.htm" + "\"";

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
		} else {
			// 合作者身份ID
			orderInfo = "partner=" + "\"" + partner + "\"";

			// 卖家支付宝账号
			orderInfo += "&seller_id=" + "\"" + seller + "\"";

			// 商户网站唯一订单号
			orderInfo += "&out_trade_no=" + "\"" + tradeNo + "\"";

			// 商品名称
			orderInfo += "&subject=" + "\"" + orderName + "\"";

			// 商品详情
			orderInfo += "&body=" + "\"" + orderDetail + "\"";

			// 商品金额
			orderInfo += "&total_fee=" + "\"" + totalPrice + "\"";
			// orderInfo += "&total_fee=" + "\"" + "0.01" + "\"";

			// 服务器异步通知页面路径
			orderInfo += "&notify_url=" + "\"" + Urls.ALI_PAY_NOTIFY + "\"";

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

			// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径.
			// orderInfo += "&return_url=\"m.alipay.com\"";
			// Bill: this item must not be empty! though the api demo said it
			// can be.
			orderInfo += "&return_url=\"m.alipay.com\"";

			// 调用银行卡支付，需配置此参数，参与签名， 固定值
			// orderInfo += "&paymethod=\"expressGateway\"";
		}

		return orderInfo;
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/* for test purpose only */
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

}
