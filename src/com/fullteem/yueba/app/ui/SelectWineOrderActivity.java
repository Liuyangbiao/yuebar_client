package com.fullteem.yueba.app.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.adapter.OrderManageAdapter;
import com.fullteem.yueba.app.adapter.PubPackageAdapter;
import com.fullteem.yueba.app.ui.OrderManageActivity.OrderType;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.BaseListModel;
import com.fullteem.yueba.model.OrderManageModel;
import com.fullteem.yueba.model.PubPackageModel;
import com.fullteem.yueba.model.ResponeModel;
import com.fullteem.yueba.net.Urls;
import com.fullteem.yueba.net.http.AsyncHttpResponseHandler;
import com.fullteem.yueba.net.http.CustomAsyncResponehandler;
import com.fullteem.yueba.net.http.HttpRequest;
import com.fullteem.yueba.net.http.HttpRequestFactory;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.widget.EmptyView;
import com.fullteem.yueba.widget.TopTitleView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2015年1月6日&emsp;下午4:54:35
 * @use 发布约会选择酒水界面
 */
public class SelectWineOrderActivity extends BaseActivity {
	private Button btnSelect, btnBuy;
	private LinearLayout llSelect, llBuy;
	private ListView lvSelect, lvBuy;
	private ImageView ivSelect, ivBuy;
	private EventListener mListener;
	private int barId = -1;

	private PubPackageAdapter adapterBuy;
	private OrderManageAdapter adapterNonConsume;
	private List<PubPackageModel> listBuy;
	private List<OrderManageModel> listNonConsume;

	public SelectWineOrderActivity() {
		super(R.layout.activity_select_wineorder);
	}

	@Override
	public void initViews() {
		initTopTitle();
		btnSelect = (Button) findViewById(R.id.btnSelect);
		btnBuy = (Button) findViewById(R.id.btnBuy);
		llSelect = (LinearLayout) findViewById(R.id.llSelect);
		llBuy = (LinearLayout) findViewById(R.id.llBuy);
		ivSelect = (ImageView) findViewById(R.id.ivSelect);
		ivBuy = (ImageView) findViewById(R.id.ivBuy);
		lvSelect = (ListView) findViewById(R.id.lvSelect);
		lvBuy = (ListView) findViewById(R.id.lvBuy);

		EmptyView emptyViewSelect = new EmptyView(SelectWineOrderActivity.this);
		emptyViewSelect.setVisibility(View.GONE);
		((ViewGroup) lvSelect.getParent()).addView(emptyViewSelect);
		lvSelect.setEmptyView(emptyViewSelect);
		// lvSelect.setVisibility(View.GONE);
		EmptyView emptyViewBuy = new EmptyView(SelectWineOrderActivity.this);
		emptyViewBuy.setVisibility(View.GONE);
		((ViewGroup) lvBuy.getParent()).addView(emptyViewBuy);
		lvBuy.setEmptyView(emptyViewBuy);
		// lvBuy.setVisibility(View.GONE);
	}

	private void initTopTitle() {
		TopTitleView topTitle = (TopTitleView) findViewById(R.id.top_title);
		topTitle.showLeftImag(R.drawable.back, new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		topTitle.showCenterText(getString(R.string.hint_dateWine), null);
	}

	@Override
	public void initData() {
		mListener = new EventListener();
		barId = getIntent().getIntExtra("BAR_ID", -1);

		lvBuy.setAdapter(adapterBuy = adapterBuy == null ? new PubPackageAdapter(
				SelectWineOrderActivity.this,
				listBuy = listBuy == null ? new LinkedList<PubPackageModel>()
						: listBuy,getIntent().getIntExtra(GlobleConstant.ENABLE_PAY, 0),false) : adapterBuy);

		listNonConsume = new ArrayList<OrderManageModel>();
		adapterNonConsume = new OrderManageAdapter(this, listNonConsume,
				OrderType.NONCONSUME);
		lvSelect.setAdapter(adapterNonConsume);
		loadData(barId);
	}

	@Override
	public void bindViews() {
		btnSelect.setOnClickListener(mListener);
		btnBuy.setOnClickListener(mListener);
		lvSelect.setOnItemClickListener(mListener);
		lvBuy.setOnItemClickListener(mListener);
	}

	private class EventListener implements OnClickListener, OnItemClickListener {
		@SuppressLint("NewApi")
		// api 11
		@Override
		public void onClick(View v) {
			if (v == btnSelect) {
				if (llSelect.getVisibility() == View.GONE) {
					LogUtil.printPushLog("SelectWineOrderActivity onClick. btnSelect, GONE");
					ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
							ivSelect, "rotation", 0F, 180F);
					objectAnimator
							.setInterpolator(new DecelerateInterpolator(1)); // Decelerate的增值器
					objectAnimator.setDuration(200).start();
					llSelect.setVisibility(View.VISIBLE);
					btnSelect.setBackgroundResource(R.drawable.content_top_bg);
				} else {
					LogUtil.printPushLog("SelectWineOrderActivity onClick. btnSelect, not GONE");
					ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
							ivSelect, "rotation", 180F, 360F);
					objectAnimator
							.setInterpolator(new DecelerateInterpolator(2));
					objectAnimator.setDuration(200).start();
					llSelect.setVisibility(View.GONE);
					btnSelect
							.setBackgroundResource(R.drawable.date_item_content);
				}
				return;
			}
			if (v == btnBuy) {
				if (llBuy.getVisibility() == View.GONE) {
					LogUtil.printPushLog("SelectWineOrderActivity onClick. btnBuy, GONE");
					ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
							ivBuy, "rotation", 0F, 180F);
					objectAnimator
							.setInterpolator(new DecelerateInterpolator(1)); // Decelerate的增值器
					objectAnimator.setDuration(200).start();
					llBuy.setVisibility(View.VISIBLE);
					btnBuy.setBackgroundResource(R.drawable.content_top_bg);
				} else {
					LogUtil.printPushLog("SelectWineOrderActivity onClick. btnBuy, not GONE");
					ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
							ivBuy, "rotation", 180F, 360F);
					objectAnimator
							.setInterpolator(new DecelerateInterpolator(2));
					objectAnimator.setDuration(200).start();
					llBuy.setVisibility(View.GONE);
					btnBuy.setBackgroundResource(R.drawable.date_item_content);
				}
				return;
			}
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//如果购买状态为1，说明不可购买
			if(getIntent().getIntExtra(GlobleConstant.ENABLE_PAY, 0) == 1){
				return;
			}
			
			String producteDescription;
			if (view == lvSelect) {
				producteDescription = listNonConsume.get(position)
						.getProductDescription();
			} else {
				producteDescription = listBuy.get(position).getBarCouponName();
			}
			LogUtil.printPushLog("SelectWineOrderActivity onItemClick");

			Intent intent = new Intent();
			intent.putExtra("producteDescription", producteDescription);
			setResult(GlobleConstant.ACTION_ORDER_CODE, intent);
			finish();
		}
	}

	/**
	 * 获取可选择购买的套餐
	 */
	private void loadData(int barId) {
		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (barId == -1 || userId == null) {
			showToast(getString(R.string.hint_getPubDetailError));
			finish();
		}
		HttpRequest.getInstance().getPubDetait(barId, userId,
				new CustomAsyncResponehandler() {
					@Override
					public void onSuccess(ResponeModel baseModel) {
						if (baseModel == null
								|| baseModel.getResultObject() == null
								|| baseModel.getDataResult() == null) {
							showToast(getString(R.string.hint_getPubDetailError));
							return;
						}

						// 套餐选择
						List<PubPackageModel> barPackage = ((List<PubPackageModel>) baseModel
								.getListObject("barCouponList",
										PubPackageModel.class));
						if (barPackage == null || barPackage.size() <= 0
								|| barPackage.get(0) == null) {
							return;
						} else {
							listBuy.addAll(barPackage);
							adapterBuy.notifyDataSetChanged();
						}

					}

					@Override
					public void onFailure(Throwable error, String content) {
					}

					@Override
					public void onFinish() {
						if (findViewById(R.id.lvLoadingBuy).getVisibility() != View.GONE)
							findViewById(R.id.lvLoadingBuy).setVisibility(
									View.GONE);
					}
				});
	}

	/**
	 * 加载订单列表，即可选择项
	 */

	/**
	 * private void loadOrderList() { Integer userId =
	 * Integer.valueOf(AppContext.getApplication().getUserInfo().getUserId());
	 * if (userId == null || userId < 0) {
	 * showToast(getString(R.string.hint_longinFirst)); return; }
	 * HttpRequest.getInstance().getOrderList(userId, barId, 1, 10, new
	 * CustomAsyncResponehandler() {
	 * 
	 * @Override public void onSuccess(ResponeModel baseModel) { }
	 * 
	 * @Override public void onFailure(Throwable error, String content) { }
	 * 
	 * @Override public void onFinish() { if (lvSelect.getVisibility() !=
	 *           View.VISIBLE) lvSelect.setVisibility(View.VISIBLE); if
	 *           (findViewById(R.id.lvLoadingSelect).getVisibility() !=
	 *           View.GONE)
	 *           findViewById(R.id.lvLoadingSelect).setVisibility(View.GONE); }
	 *           }); }
	 */

	/**
	 * 加载订单列表，即可选择项
	 */
	private void loadOrderList() {
		Integer userId = Integer.valueOf(AppContext.getApplication()
				.getUserInfo().getUserId());
		if (userId == null || userId < 0) {
			showToast(getString(R.string.hint_longinFirst));
			return;
		}

		JSONObject ob = new JSONObject();
		ob.put("userId", userId);
		HttpRequestFactory.getInstance().doHttpsPostRequest(Urls.MY_ORDER_QUERY, ob,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);

						LogUtil.printPayLog("getMyOrders request is handled");
						LogUtil.printPayLog(content);

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

							for (int i = 0; i < orderList.size(); i++) {
								orderItem = orderList.get(i);
								String payNo = orderItem.getPayNo();
								String orderNo = orderItem.getOrderNo();
								int currentBarId = Integer.parseInt(orderItem.getExtInt2());
								// String isRefund = orderItem.getIsRefund();
								if (orderItem.getStatusCode().equals("10")
										&& payNo != null && orderNo != null && barId == currentBarId) {
									listNonConsume.add(orderItem);
									adapterNonConsume.notifyDataSetChanged();
								} else {
									LogUtil.printPayLog("wrong order state");
								}
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
						if (lvSelect.getVisibility() != View.VISIBLE)
							lvSelect.setVisibility(View.VISIBLE);
						if (findViewById(R.id.lvLoadingSelect).getVisibility() != View.GONE)
							findViewById(R.id.lvLoadingSelect).setVisibility(
									View.GONE);
					};
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (listNonConsume != null) {
			listNonConsume.clear();
		}
		loadOrderList();
	}
}
