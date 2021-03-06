package com.fullteem.yueba.engine.pay;

import com.fullteem.yueba.util.LogUtil;

public class PayStateMachine {
	/* all possible state of payment */
	public enum PayState {
		PAY_INIT, PAY_GOT_CONTEXT, PAY_UPDATED_ORDER, PAY_APPLIED_ID, PAY_ORDER_CREATED, PAY_SUCCEED, ERROR_OCCURRED
	}

	/* errors may occurred during payment */
	public enum PayError {
		PAY_GET_CONTEXT_FAIL, PAY_UPDATE_ORDER_FAIL, PAY_APPLY_ID_FAIL, PAY_FAIL
	}

	private static PayStateMachine instance;
	private PayState state;
	private IOrder order;
	private IPayment payment;

	private PayStateMachine() {
	}

	public static PayStateMachine getInstance() {
		if (instance == null) {
			instance = new PayStateMachine();
		}

		return instance;
	}

	public void initPayment(IOrder order, IPayment payment) {
		this.order = order;
		this.payment = payment;
		this.state = PayState.PAY_INIT;
	}

	public void startPay() {
		changeState(PayState.PAY_INIT);
	}

	public void changeState(PayState state) {
		onStateChanged(this.state, state);
	}

	public void reportError(PayError error, String detail) {
		LogUtil.printPayLog("the error id is:" + error + " " + detail);
		changeState(PayState.ERROR_OCCURRED);
	}

	private void onStateChanged(PayState oldState, PayState newState) {
		LogUtil.printPayLog("oid state:" + oldState + " new state:" + newState);
		this.state = newState;

		handlePayStateChange();
	}

	private void handlePayStateChange() {
		if (this.order == null || this.payment == null) {
			LogUtil.printPayLog("Have not initiated payment");
			return;
		}

		switch (this.state) {
		case PAY_INIT:
			order.getPayContext();
			break;
		case PAY_GOT_CONTEXT:
			order.createOrder();
			break;
		case PAY_UPDATED_ORDER:
		case PAY_APPLIED_ID:
		case PAY_ORDER_CREATED:
			payment.pay(order);
			break;

		case PAY_SUCCEED:
		case ERROR_OCCURRED:
			finishProcess();
			break;
		default:
			LogUtil.printPayLog("state is not correct!");
			finishProcess();
		}
	}

	private void finishProcess() {
		this.order = null;
		this.payment = null;
		this.state = PayState.PAY_INIT;
	}
}
