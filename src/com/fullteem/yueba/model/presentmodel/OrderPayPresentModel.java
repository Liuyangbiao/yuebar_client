package com.fullteem.yueba.model.presentmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.fullteem.yueba.R;
import com.fullteem.yueba.engine.pay.PayUtil;
import com.fullteem.yueba.model.OrderPayModel;
import com.fullteem.yueba.util.DisplayUtils;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月16日&emsp;下午3:21:25
 * @use 订单提交、支付页面Model
 */
@PresentationModel
public class OrderPayPresentModel implements HasPresentationModelChangeSupport {
	private Context context;
	private OrderPayModel orderPay;// 订单支付bean
	private PresentationModelChangeSupport presentationModelChangeSupport;
	private int errorStateVisibilly;// 错误可视状态
	private String errorState;// 错误提示

	public OrderPayPresentModel(Context context, OrderPayModel orderPayModel) {
		this.context = context;
		this.orderPay = orderPayModel;
		if (orderPay.getOrderNums() == null
				|| !(Integer.valueOf(orderPay.getOrderNums()) > 0))
			orderPay.setOrderNums("1");
		this.errorStateVisibilly = View.INVISIBLE;
		this.presentationModelChangeSupport = new PresentationModelChangeSupport(
				this);
	}

	public String getOrderName() {
		return orderPay.getOrderName();
	}

	public String getOrderPrice() {
		return orderPay.getOrderPrice() + context.getString(R.string.yuan);
	}

	public String getOrderNums() {
		return orderPay.getOrderNums();
	}

	public String getOrderPriceTotal() {
		return (TextUtils.isEmpty(orderPay.getOrderPriceTotal()) ? orderPay
				.getOrderPrice() : orderPay.getOrderPriceTotal())
				+ context.getString(R.string.yuan);
	}

	public OrderPayModel getOrderPayModel() {
		return orderPay;
	}

	public void setOrderName(String orderName) {
		orderPay.setOrderName(orderName);
		presentationModelChangeSupport.firePropertyChange("orderName");
	}

	public void setOrderPrices(String orderPrice) {
		orderPay.setOrderPrice(orderPrice);
		presentationModelChangeSupport.firePropertyChange("orderPrice");
	}

	public void setOrderNums(String orderNums) {
		orderPay.setOrderNums(orderNums);
		presentationModelChangeSupport.firePropertyChange("orderNums");

		String str = PayUtil.roundPrice(orderNums, orderPay.getOrderPrice());
		setOrderPriceTotal(DisplayUtils.subZeroAndDot(str));
	}

	public void setOrderPriceTotal(String orderPriceTotal) {
		orderPay.setOrderPriceTotal(orderPriceTotal);
		presentationModelChangeSupport.firePropertyChange("orderPriceTotal");
	}

	public int getErrorStateVisibilly() {
		return errorStateVisibilly;
	}

	public String getErrorState() {
		return errorState;
	}

	private void setErrorStateVisibilly(int errorStateVisibilly) {
		this.errorStateVisibilly = errorStateVisibilly;
		presentationModelChangeSupport
				.firePropertyChange("errorStateVisibilly");
	}

	/**
	 * 设置错误提示
	 * 
	 * @param errorState
	 *            null或空字符串隐藏提示
	 */
	public void setErrorState(String errorState) {
		this.errorState = errorState;
		if (TextUtils.isEmpty(errorState))
			setErrorStateVisibilly(View.INVISIBLE);
		else
			setErrorStateVisibilly(View.VISIBLE);
		presentationModelChangeSupport.firePropertyChange("errorState");
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return presentationModelChangeSupport;
	}
}
