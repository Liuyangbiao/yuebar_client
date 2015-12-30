package com.fullteem.yueba.model;

import java.io.Serializable;

import android.text.TextUtils;

import com.fullteem.yueba.R;
import com.fullteem.yueba.util.LogUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月5日&emsp;下午1:54:56
 * @use 订单管理bean
 */
public class OrderManageModel implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 * members defined according to server response does not containe unit price
	 * in server, need to calculate by self
	 */
	private String amount;// no order unit price provided
	private String createTime;
	private String modifyTime;
	private String orderNo;// order No
	private String payNo;// pay No
	private String productDescription; // order name
	private String status;
	private String statusCode;
	private String ext1;// bar_logoUrl
	private String isRefund;
	private String tradeNo;// trade No

	private String extInt1;// order num
	private String extInt2;// bar id
	private String ext3;// bar name

	@Override
	public String toString() {
		return "OrderManageModel [amount=" + amount + ", createTime="
				+ createTime + ", modifyTime=" + modifyTime + ", orderNo="
				+ orderNo + ", payNo=" + payNo + ", productDescription="
				+ productDescription + ", status=" + status + ", statusCode="
				+ statusCode + ", ext1=" + ext1 + ", isRefund=" + isRefund
				+ ", tradeNo=" + tradeNo + ", extInt1=" + extInt1
				+ ", extInt2=" + extInt2 + ", ext3=" + ext3 + "]";
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getExt1() {
		if (TextUtils.isEmpty(ext1)) {
			LogUtil.printPayLog("bar_logoUrl is null, set to default");
			ext1 = "drawable://" + R.drawable.pub_icon;
		}
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getExtInt1() {
		extInt1 = validateValue(extInt1);
		LogUtil.printPayLog("order num:" + extInt1);
		return extInt1;
	}

	public void setExtInt1(String extInt1) {
		this.extInt1 = extInt1;
	}

	private String validateValue(String input) {
		String retVal = input;
		if (TextUtils.isEmpty(input)) {
			LogUtil.printPayLog("reset input");
			retVal = "1";
		}

		return retVal;
	}

	public String getExtInt2() {
		return extInt2;
	}

	public void setExtInt2(String extInt2) {
		this.extInt2 = extInt2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

}
