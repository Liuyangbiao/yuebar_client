package com.fullteem.yueba.engine.pay;

public interface OrderCallBack {
	public void onSuccess();

	public void onFailure(String content);
}
