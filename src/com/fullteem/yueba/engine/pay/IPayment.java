package com.fullteem.yueba.engine.pay;

public interface IPayment {

	public void pay(IOrder order);

	public void checkAccount();
}
