package com.fullteem.yueba.model.presentmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import android.content.Context;

import com.fullteem.yueba.R;
import com.fullteem.yueba.model.UserInfoModel;
import com.fullteem.yueba.util.TimeUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月9日&emsp;下午6:16:16
 * @use 积分查询Model管理类
 */

@PresentationModel
public class ScoreQueryPresentModel implements
		HasPresentationModelChangeSupport {
	private PresentationModelChangeSupport changesupport;
	private UserInfoModel userInfo;
	private String accumulatedIncomeScore;// 累计收入积分
	private String accumulatedConsumeScore;// 累计消费积分
	// private String amonthIncomeScore;// 近一月收入积分
	// private String aweekIncomeScore;// 近一周收入积分
	private String scoreRule;// 积分规则
	private Context context;

	public ScoreQueryPresentModel(Context context) {
		this.userInfo = UserInfoModel.getInstance();
		this.context = context;
		changesupport = new PresentationModelChangeSupport(this);
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
		changesupport.firePropertyChange("userNickname");
	}

	public void setCharm(String charm) {
		userInfo.setUserCharm(charm);
		changesupport.firePropertyChange("charm");
	}

	public String getQueryDate() {
		return TimeUtil.getTodayData() + context.getString(R.string.now_score);
	}

	public String getRemnantsScore() {
		return userInfo.getUserScore();
	}

	public void setRemnantsScore(String remnantsScore) {
		userInfo.setUserScore(remnantsScore);
		changesupport.firePropertyChange("remnantsScore");
	}

	public String getAccumulatedIncomeScore() {
		return accumulatedIncomeScore;
	}

	public void setAccumulatedIncomeScore(String accumulatedIncomeScore) {
		this.accumulatedIncomeScore = accumulatedIncomeScore;
		changesupport.firePropertyChange("accumulatedIncomeScore");
	}

	public String getAccumulatedConsumeScore() {
		return accumulatedConsumeScore;
	}

	public void setAccumulatedConsumeScore(String accumulatedConsumeScore) {
		this.accumulatedConsumeScore = accumulatedConsumeScore;
		changesupport.firePropertyChange("accumulatedConsumeScore");
	}

	// public String getAmonthIncomeScore() {
	// return amonthIncomeScore;
	// }
	//
	// public void setAmonthIncomeScore(String amonthIncomeScore) {
	// this.amonthIncomeScore = amonthIncomeScore;
	// changesupport.firePropertyChange("amonthIncomeScore");
	// }
	//
	// public String getAweekIncomeScore() {
	// return aweekIncomeScore;
	// }
	//
	// public void setAweekIncomeScore(String aweekIncomeScore) {
	// this.aweekIncomeScore = aweekIncomeScore;
	// changesupport.firePropertyChange("aweekIncomeScore");
	// }

	public String getScoreRule() {
		return scoreRule;
	}

	public void setScoreRule(String scoreRule) {
		this.scoreRule = scoreRule;
		changesupport.firePropertyChange("scoreRule");
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return changesupport;
	}

}
