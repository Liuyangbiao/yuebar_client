package com.fullteem.yueba.engine.pay;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.model.CommonModel;
import com.fullteem.yueba.model.OrderPayModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.LogUtil;

public class PayManager {
	public static void pay(Context context, OrderPayModel orderPayModel,
			PayType payType) {
		LogUtil.printPayLog("PayManager" + "start pay");
		IOrder order = PayManager.getOrder(orderPayModel, payType);
		// IOrder order = new TestOrder(orderPayModel);
		if (order.checkOrder()) {
			IPayment payment = PayManager.getPayment(context, payType);

			PayStateMachine machine = PayStateMachine.getInstance();
			machine.initPayment(order, payment);
			machine.startPay();
		}
	}

	private static IPayment getPayment(Context context, PayType type) {
		if (type == PayType.PAYBYALIPAY) {
			return new AliPayment(context);
		} else if (type == PayType.PAYBYWECHAT) {
			return new WeChatPayment(context);
		} else {
			throw new RuntimeException("does not support such pay type");
		}
	}

	private static IOrder getOrder(OrderPayModel orderPayModel, PayType type) {
		if (type == PayType.PAYBYALIPAY) {
			return new AliOrder(orderPayModel);
		} else if (type == PayType.PAYBYWECHAT) {
			return new WeChatOrder(orderPayModel);
		} else {
			throw new RuntimeException("does not support such pay type");
		}
	}

	public static void refund(String userId, String orderNo,
			final OrderCallBack orderCallBack) {
		LogUtil.printPayLog("PayManager" + "start refund");

		JSONObject ob = new JSONObject();
		ob.put("userId", userId);
		ob.put("orderNo", orderNo);

		HttpRequestFactory.getInstance().doHttpsPostRequest(Urls.ALI_PAY_REFUND, ob,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

						LogUtil.printPayLog("refund request is handled");

						CommonModel rm = new CommonModel();
						rm = JSON.parseObject(content, CommonModel.class);

						if (rm != null && rm.getCode() != null
								&& "200".equalsIgnoreCase(rm.getCode())) {
							LogUtil.printPayLog("refund succeed");
							orderCallBack.onSuccess();

						} else {
							LogUtil.printPayLog("refund code is not correct");
							orderCallBack
									.onFailure("refund code is not correct");
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.printPayLog("failed to refund");
						orderCallBack.onFailure(content);

					};

					@Override
					public void onFinish() {
						LogUtil.printPayLog("on refund finish");

					};
				});
	}

	public static void delete(String userId, String tradeNo,
			final OrderCallBack orderCallBack) {
		LogUtil.printPayLog("PayManager" + "start delete");

		JSONObject ob = new JSONObject();
		ob.put("userId", userId);
		ob.put("trade_no", tradeNo);

		HttpRequestFactory.getInstance().doHttpsPostRequest(Urls.ALI_PAY_DELETE, ob,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

						LogUtil.printPayLog("delete request is handled");

						CommonModel rm = new CommonModel();
						rm = JSON.parseObject(content, CommonModel.class);

						if (rm != null && rm.getCode() != null
								&& "200".equalsIgnoreCase(rm.getCode())) {
							LogUtil.printPayLog("delete succeed");
							orderCallBack.onSuccess();
						} else {
							LogUtil.printPayLog("delete code is not correct");
							orderCallBack
									.onFailure("delete code is not correct");

						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.printPayLog("failed to delete");
						orderCallBack.onFailure(content);
					};

					@Override
					public void onFinish() {
						LogUtil.printPayLog("on delete finish");

					};
				});
	}

}
