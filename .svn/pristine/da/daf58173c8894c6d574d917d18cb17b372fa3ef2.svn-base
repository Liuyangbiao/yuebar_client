package com.fullteem.yueba.model.presentmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import com.fullteem.yueba.model.UserInfoModel;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年11月28日&emsp;下午4:25:10
 * @use “我”界面Model管理类
 */

@PresentationModel
public class PersonalMainPresentModel implements
		HasPresentationModelChangeSupport {
	private PresentationModelChangeSupport changesupport;
	private UserInfoModel userInfo;

	public PersonalMainPresentModel() {
		userInfo = UserInfoModel.getInstance();
		changesupport = new PresentationModelChangeSupport(this);
	}

	public String getCharm() {
		return userInfo.getUserCharm();
	}

	public void setCharm(String charm) {
		userInfo.setUserCharm(charm);
		changesupport.firePropertyChange("charm");
	}

	public String getUsrNickname() {
		return userInfo.getUserNickname();
	}

	public void setUsrNickname(String usrNickname) {
		userInfo.setUserNickname(usrNickname);
		changesupport.firePropertyChange("usrNickname");
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return changesupport;
	}
}
