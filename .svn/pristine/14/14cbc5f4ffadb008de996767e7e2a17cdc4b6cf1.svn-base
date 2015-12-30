package com.fullteem.yueba.model.presentmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import com.fullteem.yueba.model.UserInfoModel;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月10日&emsp;上午10:27:07
 * @use 消费记录Model管理类
 */

@PresentationModel
public class ConsumeRecordsPresentModel implements
		HasPresentationModelChangeSupport {
	private UserInfoModel userInfo;
	private PresentationModelChangeSupport changeSupport;
	private String accumulatedConsume; // 累计消费

	public ConsumeRecordsPresentModel() {
		userInfo = UserInfoModel.getInstance();
		changeSupport = new PresentationModelChangeSupport(this);
	}

	public String getUsrNickname() {
		return userInfo.getUserNickname();
	}

	public String getCharm() {
		return userInfo.getUserCharm();
	}

	public String getUserNickname() {
		return userInfo.getUserNickname();
	}

	public void setUserNickname(String userNickname) {
		userInfo.setUserNickname(userNickname);
		changeSupport.firePropertyChange("userNickname");
	}

	public void setCharm(String charm) {
		userInfo.setUserCharm(charm);
		changeSupport.firePropertyChange("charm");
	}

	public String getAccumulatedConsume() {
		return accumulatedConsume;
	}

	public void setAccumulatedConsume(String accumulatedConsume) {
		this.accumulatedConsume = accumulatedConsume;
		changeSupport.firePropertyChange("accumulatedConsume");
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return changeSupport;
	}

}
