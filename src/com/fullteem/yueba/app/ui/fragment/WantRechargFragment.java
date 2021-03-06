package com.fullteem.yueba.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.RechargRecordsActivity;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.ListPopupWindow;
import com.fullteem.yueba.widget.ListPopupWindow.ISettext;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月9日&emsp;上午11:14:16
 * @use 我要充值界面
 */
public class WantRechargFragment extends Fragment {
	private TextView tvAccountBalance, tvRechargSelect;
	private RelativeLayout rlPayByAlipay, rlPayByWechat;
	private RadioButton rbPayByAlipay, rbPayByWechat;
	private EventListener mListener;
	private PopupWindow mPopupWindow;
	private Button btnSurePay;
	private TextView tvErrorState;

	private enum PayType {
		PAYBYALIPAY, PAYBYWECHAT, PAYBYOTHER
	}

	private PayType payType = PayType.PAYBYOTHER;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_want_recharg, container,
				false);
		initViews(view);
		initData();
		bindViews();
		return view;
	}

	private void initViews(View view) {
		// 防止你呢个点击到activity的东西
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		// for title
		TopTitleView topTitle = (TopTitleView) view
				.findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				((RechargRecordsActivity) getActivity())
						.getSupportFragmentManager().popBackStack();
			}
		});
		topTitle.showCenterText(getString(R.string.want_recharg), null);

		// for body
		tvAccountBalance = (TextView) view.findViewById(R.id.tv_accountBalance);
		tvRechargSelect = (TextView) view.findViewById(R.id.tv_rechargSelect);
		rlPayByAlipay = (RelativeLayout) view.findViewById(R.id.rl_payByAlipay);
		rlPayByWechat = (RelativeLayout) view.findViewById(R.id.rl_payByWechat);
		rbPayByAlipay = (RadioButton) view.findViewById(R.id.rb_payByAlipay);
		rbPayByWechat = (RadioButton) view.findViewById(R.id.rb_payByWechat);
		btnSurePay = (Button) view.findViewById(R.id.btn_surePay);
		tvErrorState = (TextView) view.findViewById(R.id.tv_errorState);
	}

	private void initData() {
		mListener = new EventListener();
		checkSelected();
	}

	private void bindViews() {
		tvRechargSelect.setOnClickListener(mListener);
		rlPayByAlipay.setOnClickListener(mListener);
		rlPayByWechat.setOnClickListener(mListener);
		rbPayByAlipay.setOnClickListener(mListener);
		rbPayByWechat.setOnClickListener(mListener);
		btnSurePay.setOnClickListener(mListener);
	}

	private class EventListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			showErrorState(null);
			switch (v.getId()) {
			case R.id.tv_rechargSelect:
				if (mPopupWindow == null)
					initPopupWindow();
				mPopupWindow.showAsDropDown(tvRechargSelect, 0, -5);
				break;

			case R.id.rl_payByAlipay:
				switchSelected(PayType.PAYBYALIPAY);
				break;
			case R.id.rl_payByWechat:
				switchSelected(PayType.PAYBYWECHAT);
				break;
			case R.id.rb_payByAlipay:
				switchSelected(PayType.PAYBYALIPAY);
				break;
			case R.id.rb_payByWechat:
				switchSelected(PayType.PAYBYWECHAT);
				break;

			case R.id.btn_surePay:
				if (payType != PayType.PAYBYALIPAY
						&& payType != PayType.PAYBYWECHAT)
					showErrorState(getString(R.string.select_pay_methord));
				break;
			}
		}
	}

	/**
	 * 支付宝和微信单选
	 */
	private void switchSelected(PayType payType) {
		if (this.payType == payType) {
			return;
		}
		this.payType = payType;
		if (payType == PayType.PAYBYALIPAY) {
			rbPayByAlipay.setChecked(true);
			if (rbPayByWechat.isChecked())
				rbPayByWechat.setChecked(false);
		} else if (payType == PayType.PAYBYWECHAT) {
			rbPayByWechat.setChecked(true);
			if (rbPayByAlipay.isChecked())
				rbPayByAlipay.setChecked(false);
		} else {
			if (rbPayByAlipay.isChecked())
				rbPayByAlipay.setChecked(false);
			if (rbPayByWechat.isChecked())
				rbPayByWechat.setChecked(false);
		}
	}

	private void checkSelected() {
		if (rbPayByAlipay.isChecked())
			this.payType = PayType.PAYBYALIPAY;
		if (rbPayByWechat.isChecked())
			this.payType = PayType.PAYBYWECHAT;
	}

	private void initPopupWindow() {
		final List<String> lists = new ArrayList<String>();
		lists.add(getString(R.string.recharg_20));
		lists.add(getString(R.string.recharg_48));
		ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity(),
				lists, new ISettext() {
					@Override
					public void settext(String str, int positon) {
						tvRechargSelect.setText(str);
					}
				}, tvRechargSelect.getWidth(), tvRechargSelect.getWidth());
		mPopupWindow = listPopupWindow.initPopupWindowList();
		mPopupWindow.setAnimationStyle(R.style.listpopupwindow_style);
	}

	/**
	 * 显示错误提示，传null或者空字符串则隐藏
	 */
	private void showErrorState(String showContent) {
		if (TextUtils.isEmpty(showContent)) {
			if (tvErrorState.getVisibility() == View.VISIBLE)
				tvErrorState.setVisibility(View.INVISIBLE);
			return;
		}
		if (tvErrorState.getVisibility() != View.VISIBLE)
			tvErrorState.setVisibility(View.VISIBLE);
		tvErrorState.setText(showContent);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		UmengUtil.onPageStart(getActivity(),"MainScreen");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		UmengUtil.onPageEnd(getActivity(),"MainScreen");
	}
}
