package com.fullteem.yueba.model;

import java.io.Serializable;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月31日&emsp;下午3:05:29
 * @use 行业bean
 */
public class IndustryModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String industryName;
	private int industryId;

	public String getIndustryName() {
		return industryName;
	}

	public int getIndustryId() {
		return industryId;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public void setIndustryId(int industryId) {
		this.industryId = industryId;
	}

}
