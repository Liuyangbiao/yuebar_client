package com.fullteem.yueba.model;

import java.util.List;

/**
 * 酒吧动态model
 * 
 * @author jun
 */
public class BarDynamicModel extends DynamicModel {
	private int barId = -1;
	private List<BarDynamicCommentModel> barDynamicComment;

	public void setBarDynamicId(int dynamicId) {
		super.setDynamicId(dynamicId);
	}

	public void setBarDynamicContent(String dynamicContent) {
		super.setDynamicContent(dynamicContent);
	}

	public void setIsPraise(boolean isPraise) {
		super.setPraise(isPraise);
	}

	public void setTotal(int total) {
		super.setPraiseNum(total);
	}

	public List<BarDynamicCommentModel> getBarDynamicComment() {
		return barDynamicComment;
	}

	public void setBarDynamicComment(
			List<BarDynamicCommentModel> barDynamicComment) {
		this.barDynamicComment = barDynamicComment;
	}

	public int getBarId() {
		return barId;
	}

	public void setBarId(int barId) {
		this.barId = barId;
	}

}
