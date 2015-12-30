package com.fullteem.yueba.model.presentmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import android.content.Context;
import android.view.View;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月28日&emsp;下午4:25:10
 * @use 附近约会Model管理类，MVVM设计模式
 */

@PresentationModel
public class NearbyDatePresentModel implements
		HasPresentationModelChangeSupport {
	private Context mContext;
	private PresentationModelChangeSupport changesupport;
	private int successfulVisibility;// 控制已成功约会是否显示，默认为显示
	private int addendusVisibility;// 控制可加入约会是否显示,默认为显示

	public NearbyDatePresentModel(Context mContext) {
		this.mContext = mContext;
		changesupport = new PresentationModelChangeSupport(this);
		successfulVisibility = View.VISIBLE;
		addendusVisibility = View.VISIBLE;
	}

	public int getSuccessfulVisibility() {
		return successfulVisibility;
	}

	/**
	 * 设置"已成功约会"Visibility
	 * 
	 * @param addendusVisibility
	 *            值为：View.VISIBLE, View.INVISIBLE, View.GONE
	 */
	public void setSuccessfulVisibility(int successfulVisibility) {
		this.successfulVisibility = successfulVisibility;
		changesupport.firePropertyChange("successfulVisibility");
	}

	public int getAddendusVisibility() {
		return addendusVisibility;
	}

	/**
	 * 设置"可加入约会"Visibility
	 * 
	 * @param addendusVisibility
	 *            值为：View.VISIBLE, View.INVISIBLE, View.GONE
	 */
	public void setAddendusVisibility(int addendusVisibility) {
		this.addendusVisibility = addendusVisibility;
		changesupport.firePropertyChange("addendusVisibility");
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return changesupport;
	}
}
