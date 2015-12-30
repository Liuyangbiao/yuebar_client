package com.fullteem.yueba.model;

import java.io.Serializable;

public class PayNewOrderModel extends CommonModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private PayApplyModel result;

	public PayApplyModel getResult() {
		return result;
	}

	public void setResult(PayApplyModel result) {
		this.result = result;
	}

}
