package com.fullteem.yueba.model.presentmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import android.text.TextUtils;
import android.view.View;

@PresentationModel
public class ChangePasswdPresentModel implements
		HasPresentationModelChangeSupport {
	private PresentationModelChangeSupport changesupport;
	private String oldPasswd;// 旧密码
	private String newPasswd;// 新密码
	private String newPasswdAgain;// 再次输入的新密码
	private String phoneNum;// 手机号码
	private String validNum;// 验证码
	private String errorSate;// 错误状态
	private int errorVisibility;// 错误状态是否可见

	public ChangePasswdPresentModel() {
		changesupport = new PresentationModelChangeSupport(this);
		errorVisibility = View.INVISIBLE;
	}

	public String getOldPasswd() {
		return oldPasswd;
	}

	public void setOldPasswd(String oldPasswd) {
		this.oldPasswd = oldPasswd;
	}

	public String getNewPasswd() {
		return newPasswd;
	}

	public void setNewPasswd(String newPasswd) {
		this.newPasswd = newPasswd;
	}

	public String getNewPasswdAgain() {
		return newPasswdAgain;
	}

	public void setNewPasswdAgain(String newPasswdAgain) {
		this.newPasswdAgain = newPasswdAgain;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getValidNum() {
		return validNum;
	}

	public void setValidNum(String validNum) {
		this.validNum = validNum;
	}

	public String getErrorSate() {
		return errorSate;
	}

	public void setErrorSate(String errorSate) {
		this.errorSate = errorSate;
		if (TextUtils.isEmpty(errorSate)) {
			this.errorVisibility = View.INVISIBLE;
			changesupport.firePropertyChange("errorVisibility");
			return;
		}
		this.errorVisibility = View.VISIBLE;
		changesupport.firePropertyChange("errorVisibility");
		changesupport.firePropertyChange("errorSate");
	}

	public int getErrorVisibility() {
		return errorVisibility;
	}

	public void onChanged() {
		setErrorSate(null);
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return changesupport;
	}
}
