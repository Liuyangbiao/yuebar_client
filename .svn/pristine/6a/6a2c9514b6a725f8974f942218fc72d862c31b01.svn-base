package com.fullteem.yueba.engine.pay;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.engine.pay.PayStateMachine.PayError;
import com.fullteem.yueba.engine.pay.PayStateMachine.PayState;
import com.fullteem.yueba.model.CommonModel;
import com.fullteem.yueba.model.OrderPayModel;
import com.fullteem.yueba.model.PayContextModel;
import com.fullteem.yueba.model.PayNewOrderModel;
import com.fullteem.yueba.model.PaySellerModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.LogUtil;

public abstract class BaseOrder implements IOrder {

	protected String orderName;
	protected String orderDetail;
	protected String totalPrice;
	protected String tradeNo;

	protected String partner;
	protected String seller;
	protected String privateKey;

	private OrderPayModel orderPayModel;
	private String orderStr;

	public BaseOrder(OrderPayModel orderPayModel) {
		orderName = orderPayModel.getOrderName();
		totalPrice = orderPayModel.getOrderPriceTotal();
		orderDetail = orderPayModel.getBarCouponeDetail();
		tradeNo = orderPayModel.getTradeNo();

		this.orderPayModel = orderPayModel;
	}

	@Override
	public boolean checkOrder() {
		boolean retVal = true;

//		LogUtil.printPayLog("pay: order name:" + orderName + " total price:"
//				+ totalPrice + " orderDetail:" + orderDetail);
		if (TextUtils.isEmpty(orderName) || TextUtils.isEmpty(totalPrice)
				|| TextUtils.isEmpty(orderDetail)) {
			retVal = false;
		}

		return retVal;
	}

	@Override
	public void getPayContext() {
		PayContextModel payContext = AppContext.getApplication()
				.getAliPayContext();
		if (payContext != null) {
			LogUtil.printPayLog("pay context was fetched from memory");
			setPayContext(payContext);

			PayStateMachine.getInstance().changeState(PayState.PAY_GOT_CONTEXT);
		} else {
			LogUtil.printPayLog("request pay context");
			getPayContextRequest();
		}
	}

	private void setPayContext(PayContextModel payContext) {
		if (payContext != null) {
			LogUtil.printPayLog("set pay context");

			partner = payContext.getPartner();
			seller = payContext.getSeller();
			privateKey = payContext.getPrivateKey();

			if (PayUtil.checkContext(seller, partner, privateKey) == false) {
				LogUtil.printPayLog("context is not right after comparisoin!");
			} else {
				LogUtil.printPayLog("context is equal after comparisoin");
			}
		} else {
			LogUtil.printPayLog("pay context is null");
		}
	}

	@Override
	public void createOrder() {
		if (TextUtils.isEmpty(orderPayModel.getTradeNo())) {
			LogUtil.printPayLog("the order is new, get order id firstly");
			getOrderIdRequest();
		} else if (orderPayModel.isOrderChanged()) {
			LogUtil.printPayLog("the order should be updated, send order update quest");
			updateOrderRequest();
		} else {
			/*
			 * if order id has existed, and the order is not changed, then just
			 * generate order string
			 */
//			LogUtil.printPayLog("the order exists, and was not updatd. So generate order string directly");
//			LogUtil.printPayLog("orderNo: " + orderPayModel.getTradeNo());
			this.orderStr = generateOrder();
			PayStateMachine.getInstance().changeState(
					PayState.PAY_ORDER_CREATED);
		}
	}

	@Override
	public String getOrder() {
		return this.orderStr;
	}

	private void getPayContextRequest() {
		JSONObject ob = new JSONObject();
		String str = Urls.ALI_PAY_CONTEXT;
		// String str =
		// "http://192.168.0.127:8080/bro/api/bbro/alipay/checkAlipayConfig.do";
		HttpRequestFactory.getInstance().doHttpsPostRequest(str, ob,

		new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);

				LogUtil.printPayLog("getPayContext request is handled");
				LogUtil.printPayLog(content);

				PaySellerModel rm = new PaySellerModel();
				rm = JSON.parseObject(content, PaySellerModel.class);

				if (rm != null && rm.getCode() != null
						&& "200".equalsIgnoreCase(rm.getCode())) {
					AppContext.getApplication()
							.setAliPayContext(rm.getResult());
					setPayContext(rm.getResult());
					PayStateMachine.getInstance().changeState(
							PayState.PAY_GOT_CONTEXT);

				} else {
					PayStateMachine.getInstance().reportError(
							PayError.PAY_GET_CONTEXT_FAIL, "code is not right");
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				PayStateMachine.getInstance().reportError(
						PayError.PAY_GET_CONTEXT_FAIL,
						"failed to get pay context");

			};

			@Override
			public void onFinish() {
			};
		});
	}

	abstract protected String generateOrder();

	private void updateOrderRequest() {
		JSONObject ob = new JSONObject();
		ob.put("amount", this.totalPrice);
		ob.put("userId", orderPayModel.getUserId());
		ob.put("count", orderPayModel.getOrderNums());
		ob.put("trade_no", orderPayModel.getTradeNo());
		LogUtil.printPayLog("update request data:" + orderPayModel.toString());

		HttpRequestFactory.getInstance().doHttpsPostRequest(Urls.ALI_PAY_UPDATE_ORDER,
				ob,new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

						LogUtil.printPayLog("updateOrder request is handled");

						CommonModel rm = new CommonModel();
						rm = JSON.parseObject(content, CommonModel.class);

						if (rm.getCode() != null
								&& "200".equalsIgnoreCase(rm.getCode())) {
							orderStr = generateOrder();
							PayStateMachine.getInstance().changeState(
									PayState.PAY_UPDATED_ORDER);

						} else {
							PayStateMachine.getInstance().reportError(
									PayError.PAY_UPDATE_ORDER_FAIL,
									"code is not right");
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						PayStateMachine.getInstance().reportError(
								PayError.PAY_UPDATE_ORDER_FAIL,
								"failed to update order");

					};

					@Override
					public void onFinish() {
						LogUtil.LogDebug("Payment", "on updateOrder finish",
								null);

					};
				});
	}

	private void getOrderIdRequest() {
		JSONObject ob = new JSONObject();
		ob.put("amount", orderPayModel.getOrderPriceTotal());

		// / server define order name as "product description"
		ob.put("productDescription", orderPayModel.getOrderName());

		ob.put("userId", orderPayModel.getUserId());
		ob.put("barCoupon", orderPayModel.getOrderId());
		ob.put("barId", orderPayModel.getBarId());
		ob.put("count", orderPayModel.getOrderNums());
		LogUtil.printPayLog("get order id request data:"
				+ orderPayModel.toString());

		HttpRequestFactory.getInstance().doHttpsPostRequest(Urls.ALI_PAY_APPLY, ob,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

						LogUtil.printPayLog("get order id request is handled");

						PayNewOrderModel rm = new PayNewOrderModel();
						rm = JSON.parseObject(content, PayNewOrderModel.class);

						if (rm.getCode() != null
								&& "200".equalsIgnoreCase(rm.getCode())) {
							tradeNo = rm.getResult().getTrade_no();
							LogUtil.printPayLog("succeed to get order id:"
									+ tradeNo);

							orderStr = generateOrder();
							PayStateMachine.getInstance().changeState(
									PayState.PAY_APPLIED_ID);

						} else {
							PayStateMachine.getInstance().reportError(
									PayError.PAY_APPLY_ID_FAIL,
									"code is not right");
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						PayStateMachine.getInstance().reportError(
								PayError.PAY_APPLY_ID_FAIL,
								"failed to get order id");

					};

					@Override
					public void onFinish() {
						LogUtil.LogDebug("Payment", "on get order id finish",
								null);

					};
				});
	}

}
