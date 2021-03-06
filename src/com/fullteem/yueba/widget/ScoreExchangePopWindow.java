package com.fullteem.yueba.widget;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.model.presentmodel.ExchangeScorePopWPresentModel;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月30日&emsp;下午3:47:52
 * @use 积分兑换Popupwindow
 */
public class ScoreExchangePopWindow {
	private View rootView;
	private Activity mContext;
	private PopupWindow mPopupWindow;
	private ExchangeScorePopWPresentModel presentModel;
	private Button btnSurePay;
	private EventListener mListener;

	public ScoreExchangePopWindow(Activity context,
			ExchangeScorePopWPresentModel presentModel) {
		this.mContext = context;
		this.presentModel = presentModel;
		BinderFactory binderFactory = new BinderFactoryBuilder().build();
		ViewBinder vb = binderFactory.createViewBinder(context, true);
		this.rootView = vb
				.inflateAndBind(
						R.layout.popwindow_score_exchange,
						presentModel = presentModel == null ? new ExchangeScorePopWPresentModel(
								context) : presentModel);
		mPopupWindow = new PopupWindow(rootView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
		mPopupWindow
				.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		initChlidViews();
	}

	private void initChlidViews() {
		btnSurePay = (Button) rootView.findViewById(R.id.btn_surePay);
		mListener = mListener == null ? new EventListener() : mListener;
		rootView.setOnClickListener(mListener);
		rootView.findViewById(R.id.llContent).setOnClickListener(mListener);
	}

	public ExchangeScorePopWPresentModel getModel() {
		return presentModel;
	}

	public void setNowScore(int nowScore) {
		presentModel.setNowScore("" + nowScore);
	}

	/**
	 * @param btnText
	 *            null即使用xml指定的，""可起不显示作用
	 * @param onClickListener
	 */
	public void setPayButton(String btnText,
			final OnPayButtonClickListener onClickListener) {
		if (btnText != null)
			btnSurePay.setText(btnText);
		btnSurePay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onClickListener != null)
					onClickListener.onPayClick(v, presentModel);
			}
		});
	}

	private class EventListener implements OnClickListener {
		@Override
		public void onClick(View v) {

			if (v == rootView)
				if (getPopupWindow().isShowing())
					getPopupWindow().dismiss();
		}
	}

	/**
	 * @param title
	 *            null即使用xml指定的，""可起不显示作用
	 */
	public void setTitle(String title) {
		if (title == null)
			return;
		TextView tvContent = (TextView) rootView.findViewById(R.id.tvTitle);
		tvContent.setText(title);
	}

	public PopupWindow getPopupWindow() {
		return mPopupWindow;
	}

	public void showWindow() {
		showWindow(null, null, null);
	}

	public void showWindow(String title, String btnContent,
			OnPayButtonClickListener onClickListener) {
		setTitle(title);
		setPayButton(btnContent, onClickListener);
		if (presentModel.getErrorStateVisibilly() == View.VISIBLE)
			presentModel.setErrorState(null);
		mPopupWindow.showAtLocation(mContext.getWindow().getDecorView(),
				Gravity.CENTER, 0, 0);
	}

	public void showWindow(OnPayButtonClickListener onClickListener) {
		showWindow(null, null, onClickListener);
	}

	public interface OnPayButtonClickListener {
		void onPayClick(View v, ExchangeScorePopWPresentModel presentModel);
	}
}
