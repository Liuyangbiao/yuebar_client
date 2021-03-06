package com.fullteem.yueba.model;

import android.text.TextUtils;

import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.util.LogUtil;

/**
 * 订单支付model
 * 
 * @author jun
 * 
 */
public class OrderPayModel extends OrderModel {
	private static final long serialVersionUID = -119921373385823050L;

	/* OrderModel is just to save the very base info: id, name, unit price */

	/* order nums and total price: are set by OrderPayPresendModel */
	private String orderNums; // 订单数量
	private String orderPriceTotal;// 总价

	/* orderChanged: set by OrderPayFragment */
	private boolean orderChanged;

	/* user id: get from app context */
	private String userId;// 用户id

	/* bar info: get from Pub package model */
	private String barId;
	private String barCouponDetail;

	/*
	 * for existing order(has been generated once): get from OrderManageModel
	 * for new order: it is null
	 */
	private String tradeNo;

	/* for pay a new order */
	public OrderPayModel(PubPackageModel packageModel) {
		super(packageModel.getBarCouponId() + "", packageModel
				.getBarCouponName(), packageModel.getBarCouponPrice() + "");

		LogUtil.printPayLog("create order pay model to pay a new order");

		this.barId = packageModel.getBarId() + "";
		this.barCouponDetail = packageModel.getBarCouponDetail();

		this.setTradeNo(null);
		this.setUserId();

		LogUtil.printPayLog(toString());
	}

	/* for pay order right now */
	public OrderPayModel(OrderManageModel orderManageModel) {
		super(orderManageModel.getOrderNo(), orderManageModel
				.getProductDescription(), "");

		LogUtil.printPayLog("create order pay model to pay order right now");
		LogUtil.printPayLog("orderManageModel info:"
				+ orderManageModel.toString());

		// set total price
		String totalPrice = orderManageModel.getAmount();
		this.setOrderPriceTotal(filterOrderPrice(totalPrice));

		// set order num
		this.orderNums = orderManageModel.getExtInt1();
		LogUtil.printPayLog("order num:" + this.orderNums);

		// calculate unit price, and set it
		float amount = Float.parseFloat(totalPrice);
		int num = Integer.parseInt(this.orderNums);
		if (num < 1) {
			LogUtil.printPayLog("order num should be no less than one!");
			num = 1;
		}
		this.setOrderPrice(amount / num + "");

		this.setTradeNo(orderManageModel.getTradeNo());
		this.setUserId();
		LogUtil.printPayLog(toString());
	}

	// for gift buy. TBD
	public OrderPayModel() {
		LogUtil.printPayLog("create order pay model for gift buy");

		// set user id
		this.setUserId();

		LogUtil.printPayLog(toString());
	}

	public String getBarCouponeDetail() {
		if (TextUtils.isEmpty(barCouponDetail)) {
			barCouponDetail = this.getOrderName();
		}
		return barCouponDetail;
	}

	public void setBarCouponeDetail(String barCouponeDetail) {
		this.barCouponDetail = barCouponeDetail;
	}

	public void setBarId(String barId) {
		this.barId = barId;
	}

	public String getBarId() {
		return barId;
	}

	public String getUserId() {
		return userId;
	}

	public boolean isOrderChanged() {
		return orderChanged;
	}

	public void setOrderChanged(boolean orderChanged) {
		this.orderChanged = orderChanged;
	}

	@Override
	public String toString() {
		return "OrderPayModel [orderNums=" + orderNums + ", orderPriceTotal="
				+ orderPriceTotal + ", orderChanged=" + orderChanged
				+ ", userId=" + userId + ", barId=" + barId
				+ ", barCouponDetail=" + barCouponDetail + ", tradeNo="
				+ tradeNo + "]";
	}

	// before hand over to borui
	public String getOrderNums() {
		return TextUtils.isEmpty(orderNums) ? "1" : orderNums;
	}

	public void setOrderNums(String orderNums) {
		this.orderNums = orderNums;
	}

	public String getOrderPriceTotal() {
		return TextUtils.isEmpty(orderPriceTotal) ? getOrderPrice()
				: orderPriceTotal;
	}

	public void setOrderPriceTotal(String orderPriceTotal) {
		this.orderPriceTotal = orderPriceTotal;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	/* when use fake data, need to filter */
	private String filterOrderPrice(String priceWithUnit) {
		String retVal = priceWithUnit.trim();
		if (retVal.endsWith("元")) {
			retVal = retVal.substring(0, retVal.length() - 1);
		}
		return retVal;
	}

	private void setUserId() {
		this.userId = AppContext.getApplication().getUserInfo().getUserId();
	}
}
