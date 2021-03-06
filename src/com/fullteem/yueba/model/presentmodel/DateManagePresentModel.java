package com.fullteem.yueba.model.presentmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import android.content.Context;
import android.content.Intent;

import com.fullteem.yueba.app.ui.PerssonalInfoAcitivity;
import com.fullteem.yueba.model.DateDetailModel;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月28日&emsp;下午4:25:10
 * @use 约会管理Model类
 */

@PresentationModel
public class DateManagePresentModel implements
		HasPresentationModelChangeSupport {
	private PresentationModelChangeSupport changesupport;
	// private String dateTitle; // 约会主题
	// private String dateLocation; // 约会地点
	// private String dateTime; // 约会时间
	// private String sponsor;// 约会发起人
	// private String fees; // 约会费用
	// private String describe;// 约会描述
	private DateDetailModel dateModel;
	private Context context;

	public DateManagePresentModel(Context context) {
		changesupport = new PresentationModelChangeSupport(this);
		dateModel = dateModel == null ? new DateDetailModel() : dateModel;
		this.context = context;
		setModel(dateModel);
	}

	public void setModel(DateDetailModel dateModel) {
		this.dateModel = dateModel;
		changesupport.firePropertyChange("dateTitle");
		changesupport.firePropertyChange("dateLocation");
		changesupport.firePropertyChange("dateTime");
		changesupport.firePropertyChange("sponsor");
		changesupport.firePropertyChange("fees");
		changesupport.firePropertyChange("describe");
	}

	public String getDateTitle() {
		return dateModel.getUserAppointmentTitle();
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

	public void onSponsorClick() {
		if (dateModel.getUserId() == -1)
			return;
		Intent intent = new Intent(context, PerssonalInfoAcitivity.class);
		intent.putExtra("userId", dateModel.getUserId() + "");
		context.startActivity(intent);
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return changesupport;
	}

}
