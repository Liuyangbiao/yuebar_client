package com.fullteem.yueba.app.ui.fragment;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;
import org.robobinding.binder.BinderFactoryBuilder;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.engine.pay.PayManager;
import com.fullteem.yueba.engine.pay.PayType;
import com.fullteem.yueba.model.OrderPayModel;
import com.fullteem.yueba.model.presentmodel.OrderPayPresentModel;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UmengUtil;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月16日&emsp;下午3:21:25
 * @use 订单提交、支付页面
 */
@SuppressLint("ValidFragment")
public class OrderPayFragment extends Fragment {
	private Button btnOrderNumsAdd, btnOrderNumsSub;
	private RelativeLayout rlPayByAlipay, rlPayByWechat;
	private RadioButton rbPayByAlipay, rbPayByWechat;
	private Button btnSurePay;
	private EventListener mListener;
	private String topTitleStr;
	private OrderPayPresentModel presentMode;

	private PayType payType = PayType.PAYBYOTHER;

	// bill
	/* used to update order from server side */
	private String originalOrderNum;
	private final boolean SUPPORT_ORDER_CHANGE = true;

	public OrderPayFragment() {

	}

	public OrderPayFragment(OrderPayPresentModel orderPayPresentModel,
			String topTitle) {
		this.presentMode = orderPayPresentModel;
		this.topTitleStr = topTitle;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		BinderFactory binderFactory = new BinderFactoryBuilder().build();
		ViewBinder vb = binderFactory.createViewBinder(getActivity(), true);
		View rootView = vb.inflateAndBind(R.layout.fragment_order_pay,
				presentMode);
		initViews(rootView);
		initData();
		bindViews();
		return rootView;
	}

	private void initViews(View view) {
		// 防止点击到activity的东西
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
				((BaseActivity) getActivity()).getSupportFragmentManager()
						.popBackStack();
			}
		});
		topTitle.showCenterText(
				TextUtils.isEmpty(topTitleStr) ? getString(R.string.orderSumbit)
						: topTitleStr, null);

		// for body
		btnOrderNumsAdd = (Button) view.findViewById(R.id.btn_orderNumsAdd);
		btnOrderNumsSub = (Button) view.findViewById(R.id.btn_orderNumsSub);
		rlPayByAlipay = (RelativeLayout) view.findViewById(R.id.rl_payByAlipay);
		rlPayByWechat = (RelativeLayout) view.findViewById(R.id.rl_payByWechat);
		rbPayByAlipay = (RadioButton) view.findViewById(R.id.rb_payByAlipay);
		rbPayByWechat = (RadioButton) view.findViewById(R.id.rb_payByWechat);
		btnSurePay = (Button) view.findViewById(R.id.btn_surePay);

		// bill
		boolean isOrderCreated = TextUtils.isEmpty(presentMode
				.getOrderPayModel().getOrderId());
		boolean supportOrderChange = SUPPORT_ORDER_CHANGE;
		if (!supportOrderChange && isOrderCreated) {
			btnOrderNumsAdd.setVisibility(View.INVISIBLE);
			btnOrderNumsSub.setVisibility(View.INVISIBLE);
		}
	}

	private void initData() {
		mListener = new EventListener();
		checkSelected();
		this.originalOrderNum = presentMode.getOrderPayModel().getOrderNums();
	}

	private void bindViews() {
		btnOrderNumsSub.setOnClickListener(mListener);
		btnOrderNumsAdd.setOnClickListener(mListener);
		rlPayByAlipay.setOnClickListener(mListener);
		rlPayByWechat.setOnClickListener(mListener);
		rbPayByAlipay.setOnClickListener(mListener);
		rbPayByWechat.setOnClickListener(mListener);
		btnSurePay.setOnClickListener(mListener);
	}

	private class EventListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			presentMode.setErrorState(null);
			switch (v.getId()) {
			case R.id.btn_orderNumsAdd:
				int currentNums = Integer.valueOf(presentMode
						.getOrderPayModel().getOrderNums());
				if (currentNums >= 99) {
					presentMode
							.setErrorState(getString(R.string.hint_chouseAppropriateNums));
					presentMode.setOrderNums("99");
					return;
				}
				if (presentMode.getErrorStateVisibilly() == View.VISIBLE)
					presentMode.setErrorState(null);
				presentMode.setOrderNums(currentNums + 1 + "");
				LogUtil.printPayLog(getClass().getName()
						+ "add action, order num is:" + getOrderNum());
				break;
			case R.id.btn_orderNumsSub:
				int currentNum = Integer.valueOf(presentMode.getOrderPayModel()
						.getOrderNums());
				if (currentNum <= 1) {
					presentMode
							.setErrorState(getString(R.string.hint_chouseAppropriateNums));
					presentMode.setOrderNums(1 + "");
					return;
				}
				if (presentMode.getErrorStateVisibilly() == View.VISIBLE)
					presentMode.setErrorState(null);
				presentMode.setOrderNums(currentNum - 1 + "");
				LogUtil.printPayLog(getClass().getName()
						+ "sub action, order num is:" + getOrderNum());
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
						&& payType != PayType.PAYBYWECHAT) {
					presentMode
							.setErrorState(getString(R.string.select_pay_methord));
					return;
				} else {
					UmengUtil.onEvent(getActivity(), "alipay_hits");
					LogUtil.printUmengLog("alipay_hits");
					LogUtil.printPayLog(getClass().getName() + ":sure to pay");
					OrderPayModel payModel = presentMode.getOrderPayModel();
					payModel.setOrderChanged(isOrderNumChanged());
					LogUtil.printPayLog(getClass().getName() + "order changed:"
							+ isOrderNumChanged() + " order num:"
							+ payModel.getOrderNums());
					PayManager.pay(getActivity(), payModel, payType);
				}
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

	private boolean isOrderNumChanged() {
		return !(this.originalOrderNum.equals(getOrderNum()));
	}

	private String getOrderNum() {
		return presentMode.getOrderPayModel().getOrderNums();
	}

}
