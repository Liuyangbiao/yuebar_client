package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.OrderManageAdapter;
import com.fullteem.yueba.engine.pay.PayUtil;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.BaseListModel;
import com.fullteem.yueba.model.OrderManageModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.widget.ExpandListView;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月5日&emsp;上午10:53:58
 * @use 订单管理页面
 */

public class OrderManageActivity extends BaseActivity {
	public enum OrderType {
		NONPAYMENT, NONCONSUME, CONSUMED, BACKORDER, NORMAL
	}

	private TopTitleView topTitle;
	private TextView tvNonPayment, tvNonConsume, tvConsumed, tvBackOrder;
	private ExpandListView lvNonPayment, lvNonConsume, lvConsumed, lvBackOrder;
	private EventListener mListener;
	private List<OrderManageModel> listNonPayment, listNonConsume,
			listConsumed, listBackOrder;
	private OrderManageAdapter adapterNonPayment, adapterNonConsume,
			adapterConsumed, adapterBackOrder;
	private Drawable[] drawableArrow; // drawableArrow[0]
										// down_arrow,drawableArrow[1] up_arrow,

	public OrderManageActivity() {
		super(R.layout.activity_order_manage);
	}

	@Override
	public void initViews() {
		topTitle = (TopTitleView) findViewById(R.id.top_title);
		initTopTitle();
		tvNonPayment = (TextView) findViewById(R.id.tv_nonPayment);
		tvNonConsume = (TextView) findViewById(R.id.tv_nonConsume);
		tvConsumed = (TextView) findViewById(R.id.tv_consumed);
		tvBackOrder = (TextView) findViewById(R.id.tv_backOrder);

		lvNonPayment = (ExpandListView) findViewById(R.id.lv_nonPayment);
		lvNonConsume = (ExpandListView) findViewById(R.id.lv_nonConsume);
		lvConsumed = (ExpandListView) findViewById(R.id.lv_consumed);
		lvBackOrder = (ExpandListView) findViewById(R.id.lv_backOrder);

	}

	@Override
	public void initData() {
		mListener = new EventListener();

		drawableArrow = new Drawable[2];
		drawableArrow[0] = getResources().getDrawable(
				R.drawable.down_arrow_icon);
		drawableArrow[1] = getResources().getDrawable(R.drawable.up_arrow_icon);
		drawableArrow[0].setBounds(0, 0, drawableArrow[0].getMinimumWidth(),
				drawableArrow[0].getMinimumHeight());
		drawableArrow[1].setBounds(0, 0, drawableArrow[1].getMinimumWidth(),
				drawableArrow[1].getMinimumHeight());

		listNonPayment = new ArrayList<OrderManageModel>();
		listNonConsume = new ArrayList<OrderManageModel>();
		listConsumed = new ArrayList<OrderManageModel>();
		listBackOrder = new ArrayList<OrderManageModel>();

		adapterNonPayment = new OrderManageAdapter(this, listNonPayment,
				OrderType.NONPAYMENT);
		adapterNonConsume = new OrderManageAdapter(this, listNonConsume,
				OrderType.NONCONSUME);
		adapterConsumed = new OrderManageAdapter(this, listConsumed,
				OrderType.CONSUMED);
		adapterBackOrder = new OrderManageAdapter(this, listBackOrder,
				OrderType.BACKORDER);

		lvNonPayment.setAdapter(adapterNonPayment);
		lvNonConsume.setAdapter(adapterNonConsume);
		lvConsumed.setAdapter(adapterConsumed);
		lvBackOrder.setAdapter(adapterBackOrder);

		getMyOrdersRequest();

	}

	private void initTopTitle() {
		topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.perssonal_order_manage),
				null);
	}

	@Override
	public void bindViews() {
		tvNonPayment.setOnClickListener(mListener);
		tvNonConsume.setOnClickListener(mListener);
		tvConsumed.setOnClickListener(mListener);
		tvBackOrder.setOnClickListener(mListener);
	}

	private class EventListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_nonPayment:
				if (lvNonPayment.getVisibility() == View.GONE) {
					tvNonPayment.setCompoundDrawables(null, null,
							drawableArrow[1], null);
					lvNonPayment.setVisibility(View.VISIBLE);
					if (null != listNonPayment && listNonPayment.size() > 0) {
						tvNonPayment
								.setBackgroundResource(R.drawable.content_top_bg);
					}
				} else {
					tvNonPayment.setCompoundDrawables(null, null,
							drawableArrow[0], null);
					lvNonPayment.setVisibility(View.GONE);
					tvNonPayment
							.setBackgroundResource(R.drawable.date_item_content);
				}
				break;

			case R.id.tv_nonConsume:
				if (lvNonConsume.getVisibility() == View.GONE) {
					tvNonConsume.setCompoundDrawables(null, null,
							drawableArrow[1], null);
					lvNonConsume.setVisibility(View.VISIBLE);
					if (null != listNonPayment && listNonConsume.size() > 0) {
						tvNonConsume
								.setBackgroundResource(R.drawable.content_top_bg);
					}
				} else {
					tvNonConsume.setCompoundDrawables(null, null,
							drawableArrow[0], null);
					lvNonConsume.setVisibility(View.GONE);
					tvNonConsume
							.setBackgroundResource(R.drawable.date_item_content);
				}
				break;

			case R.id.tv_consumed:
				if (lvConsumed.getVisibility() == View.GONE) {
					tvConsumed.setCompoundDrawables(null, null,
							drawableArrow[1], null);
					lvConsumed.setVisibility(View.VISIBLE);
					if (null != listNonPayment && listConsumed.size() > 0) {
						tvConsumed
								.setBackgroundResource(R.drawable.content_top_bg);
					}
				} else {
					tvConsumed.setCompoundDrawables(null, null,
							drawableArrow[0], null);
					lvConsumed.setVisibility(View.GONE);
					tvConsumed
							.setBackgroundResource(R.drawable.date_item_content);
				}
				break;

			case R.id.tv_backOrder:
				if (lvBackOrder.getVisibility() == View.GONE) {
					tvBackOrder.setCompoundDrawables(null, null,
							drawableArrow[1], null);
					lvBackOrder.setVisibility(View.VISIBLE);
					if (null != listNonPayment && listBackOrder.size() > 0) {
						tvBackOrder
								.setBackgroundResource(R.drawable.content_top_bg);
					}
				} else {
					tvBackOrder.setCompoundDrawables(null, null,
							drawableArrow[0], null);
					lvBackOrder.setVisibility(View.GONE);
					tvBackOrder
							.setBackgroundResource(R.drawable.date_item_content);
				}
				break;

			}
		}
	}

	public void getMyOrdersRequest() {
		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId == null || userId < 0) {
			showToast(getString(R.string.hint_longinFirst));
			return;
		}

		//
		clearData();

		JSONObject ob = new JSONObject();
		ob.put("userId", userId);
		HttpRequestFactory.getInstance().doHttpsPostRequest(Urls.MY_ORDER_QUERY, ob,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

						LogUtil.printPayLog("getMyOrders request is handled");
						LogUtil.printPayLog(content);

						//
						BaseListModel<OrderManageModel> model = JSON
								.parseObject(
										content,
										new TypeReference<BaseListModel<OrderManageModel>>() {
										});
						if (model == null) {
							LogUtil.printPayLog("empty order list");
							return;
						}

						if (GlobleConstant.REQUEST_SUCCESS
								.equalsIgnoreCase(model.getCode())) {
							OrderManageModel orderItem;
							List<OrderManageModel> orderList = model
									.getResult();
							int size = orderList.size();
							if (size < 1) {
								return;
							}

							boolean changeArray[] = { false, false, false,
									false };
							for (int i = 0; i < orderList.size(); i++) {
								orderItem = orderList.get(i);
								if (orderItem.getStatusCode().equals(
										PayUtil.ORDER_STATUS_UN_PAYED)) {
									listNonPayment.add(orderItem);
									changeArray[0] = true;
								} else if (orderItem.getStatusCode().equals(
										PayUtil.ORDER_STATUS_PAYED_UN_CONSUME)) {
									changeArray[1] = true;
									listNonConsume.add(orderItem);
								} else if (orderItem.getStatusCode().equals(
										PayUtil.ORDER_STATUS_CONSUMED)) {
									changeArray[2] = true;
									listConsumed.add(orderItem);
								} else if (orderItem.getStatusCode().equals(
										PayUtil.ORDER_STATUS_REFUNDED)
										|| orderItem.getStatusCode().equals(
												PayUtil.ORDER_STATUS_REFUNDING)) {
									changeArray[3] = true;
									listBackOrder.add(orderItem);
								} else {
									LogUtil.printPayLog("wrong order state");
								}

							}

							if (changeArray[0]) {
								adapterNonPayment.notifyDataSetChanged();
							}

							if (changeArray[1]) {
								adapterNonConsume.notifyDataSetChanged();
							}

							if (changeArray[2]) {
								adapterConsumed.notifyDataSetChanged();
							}

							if (changeArray[3]) {
								adapterBackOrder.notifyDataSetChanged();
							}
						}

					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.printPayLog("getMyOrders failed");

					};

					@Override
					public void onFinish() {
						LogUtil.printPayLog("getMyOrders finished");
					};
				});
	}

	private void clearData() {
		if (listNonPayment != null) {
			listNonPayment.clear();
		}

		if (listNonConsume != null) {
			listNonConsume.clear();
		}

		if (listConsumed != null) {
			listConsumed.clear();
		}

		if (listBackOrder != null) {
			listBackOrder.clear();
		}

	}
}
