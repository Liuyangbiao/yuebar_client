package com.fullteem.yueba.model;

import java.io.Serializable;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月5日&emsp;下午1:54:56
 * @use 订单管理bean
 */
public class OrderModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String orderId;
	private String orderName; // 订单名
	private String orderPrice;// 订单单位价格

	public OrderModel(String orderId, String orderName, String orderPrice) {
		super();
		this.orderId = orderId;
		this.orderName = orderName;
		this.orderPrice = orderPrice;
	}

	public OrderModel() {
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
