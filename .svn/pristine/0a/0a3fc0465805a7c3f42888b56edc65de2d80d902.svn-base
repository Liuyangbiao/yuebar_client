package com.fullteem.yueba.model;

import com.fullteem.yueba.engine.pay.PayUtil;
import com.fullteem.yueba.util.Base64Utils;

public class PayContextModel {
	private static final long serialVersionUID = 1L;

	private String seller;
	private String partner;
	private String privateKey;

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		if (PayUtil.supportPayContextEncrypt) {
			this.seller = Base64Utils.decode(seller);
		} else {
			this.seller = seller;
		}
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		if (PayUtil.supportPayContextEncrypt) {
			this.partner = Base64Utils.decode(partner);
		} else {
			this.partner = partner;
		}
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		if (PayUtil.supportPayContextEncrypt) {
			this.privateKey = Base64Utils.decode(privateKey);
		} else {
			this.privateKey = privateKey;
		}
	}
}
