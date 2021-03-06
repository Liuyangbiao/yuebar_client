package com.fullteem.yueba.model.presentmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import android.content.Context;
import android.content.Intent;

import com.fullteem.yueba.app.ui.PerssonalInfoAcitivity;
import com.fullteem.yueba.model.DateModel;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UmengUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月28日&emsp;下午4:25:10
 * @use 附近约会详情Model管理类，MVVM设计模式
 */

@PresentationModel
public class NearbyDateDetailsPresentModel implements
		HasPresentationModelChangeSupport {
	private PresentationModelChangeSupport changesupport;
	private Context context;
	// private String dateTitle; // 约会主题
	// private String dateLocation; // 约会地点
	// private String dateTime; // 约会时间
	// private String sponsor;// 约会发起人
	// private String fees; // 约会费用
	// private String describe;// 约会描述
	private int enroll;// 已报名人数
	private int partake;// 已参与人数
	private String enrollAndPartake;// 已参与/已报名
	private DateModel dateModel;

	public NearbyDateDetailsPresentModel(Context context) {
		changesupport = new PresentationModelChangeSupport(this);
		dateModel = dateModel == null ? new DateModel() : dateModel;
		this.context = context;
	}

	public DateModel getModel() {
		return dateModel;
	}

	public void setModel(DateModel dateModel) {
		this.dateModel = dateModel;
		changesupport.firePropertyChange("dateTitle");
		changesupport.firePropertyChange("dateLocation");
		changesupport.firePropertyChange("dateTime");
		changesupport.firePropertyChange("sponsor");
		changesupport.firePropertyChange("fees");
		changesupport.firePropertyChange("describe");
		setEnroll(dateModel.getCount());
		setPartake(dateModel.getHasCount());
	}

	public String getDateTitle() {
		// here should set bar name, not appointment title
		return dateModel.getBarName();
	}

	public String getDateLocation() {
		return dateModel.getUserAppointmentLocation();
	}

	public String getDateTime() {
		return dateModel.getUserAppointmentDate();
	}

	public String getSponsor() {
		return dateModel.getUserName();
	}

	public String getFees() {
		return dateModel.getUserAppointmentFee();
	}

	public String getDescribe() {
		return dateModel.getUserAppointmentDescrip();
	}

	public int getEnroll() {
		return enroll;
	}

	public void setEnroll(int enroll) {
		this.enroll = enroll;
		setEnrollAndPartake("(" + getEnroll() + "/" + getPartake() + ")");
	}

	public int getPartake() {
		return partake;
	}

	public void setPartake(int partake) {
		this.partake = partake;
		setEnrollAndPartake("(" + getEnroll() + "/" + getPartake() + ")");
	}

	public String getEnrollAndPartake() {
		return enrollAndPartake;
	}

	private void setEnrollAndPartake(String enrollAndPartake) {
		this.enrollAndPartake = enrollAndPartake;
		changesupport.firePropertyChange("enrollAndPartake");
	}

	public void onSponsorClick() {
		if (dateModel.getUserId() == -1)
			return;
		UmengUtil.onEvent(context, "organiser_hits");
		LogUtil.printUmengLog("organiser_hits");
		Intent intent = new Intent(context, PerssonalInfoAcitivity.class);
		intent.putExtra("userId", dateModel.getUserId() + "");
		intent.putExtra("isFromDateDetail", true);//是否是从约会发起人页面点击到个人详情
		context.startActivity(intent);
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return changesupport;
	}
}
