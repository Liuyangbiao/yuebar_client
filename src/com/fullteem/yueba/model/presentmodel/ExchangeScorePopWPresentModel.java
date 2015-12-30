package com.fullteem.yueba.model.presentmodel;

import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.fullteem.yueba.R;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月30日&emsp;下午6:12:42
 * @use 积分兑换弹出popupwindow 的视图model
 */
@PresentationModel
public class ExchangeScorePopWPresentModel implements
		HasPresentationModelChangeSupport {
	private PresentationModelChangeSupport presentationModelChangeSupport;
	private int errorStateVisibilly;// 错误可视状态
	private String errorState;// 错误提示
	private Context context;
	private String nowScore;// 现有积分
	private String canExchange;// 可兑换金币
	private String exchangeScore;// 兑换数量
	private String exchangeGold;// 最终兑换金币数量

	public ExchangeScorePopWPresentModel(Context context) {
		presentationModelChangeSupport = new PresentationModelChangeSupport(
				this);
		this.context = context;
		this.errorStateVisibilly = View.INVISIBLE;
	}

	public int getErrorStateVisibilly() {
		return errorStateVisibilly;
	}

	public String getErrorState() {
		return errorState;
	}

	public Context getContext() {
		return context;
	}

	public String getNowScore() {
		return TextUtils.isEmpty(nowScore) ? "0" : nowScore;
	}

	public String getCanExchange() {
		return (TextUtils.isEmpty(canExchange) ? "0" : canExchange)
				+ context.getString(R.string.gold);
	}

	public String getExchangeScore() {
		return exchangeScore;
	}

	private void setErrorStateVisibilly(int errorStateVisibilly) {
		this.errorStateVisibilly = errorStateVisibilly;
		presentationModelChangeSupport
				.firePropertyChange("errorStateVisibilly");
	}

	public void setErrorState(String errorState) {
		this.errorState = errorState;
		if (TextUtils.isEmpty(errorState)) {
			if (getErrorStateVisibilly() != View.INVISIBLE)
				setErrorStateVisibilly(View.INVISIBLE);
			return;
		}
		setErrorStateVisibilly(View.VISIBLE);
		presentationModelChangeSupport.firePropertyChange("errorState");
	}

	/**
	 * 设置现有积分
	 * 
	 * @param nowScore
	 */
	public void setNowScore(String nowScore) {
		this.nowScore = nowScore;
		setCanExchange("" + (Integer.valueOf(nowScore) / 10));
		presentationModelChangeSupport.firePropertyChange("nowScore");
	}

	private void setCanExchange(String canExchange) {
		this.canExchange = canExchange;
		presentationModelChangeSupport.firePropertyChange("canExchange");
	}

	public void setExchangeScore(String exchangeScore) {
		this.exchangeScore = exchangeScore;
	}

	/**
	 * @return 根据输入的数得到能兑换的金币
	 */
	public String getExchangeGold() {
		return exchangeGold;
	}

	public void setExchangeGold(String exchangeGold) {
		this.exchangeGold = exchangeGold;
	}

	/**
	 * 文本内容发生改变时
	 */
	public void onChanged() {
		setErrorState(null);
		if (!TextUtils.isEmpty(getExchangeScore())) {
			if (Integer.valueOf(getExchangeScore()) > Integer
					.valueOf(getNowScore())
					|| Integer.valueOf(getExchangeScore()) <= 0) {
				setErrorState(context.getString(R.string.hint_inputValidValue));
				return;
			}
			setExchangeGold("" + (Integer.valueOf(getExchangeScore()) / 10));
		}
	}

	@Override
	public PresentationModelChangeSupport getPresentationModelChangeSupport() {
		return presentationModelChangeSupport;
	}

}
