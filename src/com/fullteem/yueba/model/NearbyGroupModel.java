package com.fullteem.yueba.model;

import java.util.List;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月4日&emsp;下午6:14:59
 * @use 附近群组bean
 */

public class NearbyGroupModel extends CommonModel {
	private List<Group> result;

	public List<Group> getResult() {
		return result;
	}

	public void setResult(List<Group> result) {
		this.result = result;
	}
}