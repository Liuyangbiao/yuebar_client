package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.AppContext;
import com.fullteem.yueba.app.ui.OrderManageActivity.OrderType;
import com.fullteem.yueba.app.ui.PubDetailsActivity;
import com.fullteem.yueba.app.ui.fragment.OrderPayFragment;
import com.fullteem.yueba.engine.pay.OrderCallBack;
import com.fullteem.yueba.engine.pay.PayManager;
import com.fullteem.yueba.engine.pay.PayUtil;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.OrderManageModel;
import com.fullteem.yueba.model.OrderPayModel;
import com.fullteem.yueba.model.presentmodel.OrderPayPresentModel;
import com.fullteem.yueba.util.Base64Utils;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UmengUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月8日&emsp;上午10:47:12
 * @use 订单管理Adapter
 */
public class OrderManageAdapter extends BaseAdapter {
	private List<OrderManageModel> listOrder;
	private OrderType orderType;
	private Context context;
	private EventListener mListener;
	private BaseAdapter adapter;

	public OrderManageAdapter(Context context,
			List<OrderManageModel> listOrder, OrderType orderType) {
		this.context = context;
		this.listOrder = listOrder;
		this.orderType = orderType;
		this.adapter = this;
		mListener = new EventListener();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.adapter_ordermanage_elistview, null);
			mHolder.ivOrderHeader = (ImageView) convertView
					.findViewById(R.id.iv_orderHeader);
			mHolder.tvOrderName = (TextView) convertView
					.findViewById(R.id.tv_orderName);
			mHolder.tvPubName = (TextView) convertView
					.findViewById(R.id.tv_pubName);
			mHolder.tvOrderPrice = (TextView) convertView
					.findViewById(R.id.tv_orderPrice);
			mHolder.llNonPayment = (LinearLayout) convertView
					.findViewById(R.id.ll_nonPayment);
			mHolder.llNonConsume = (LinearLayout) convertView
					.findViewById(R.id.ll_nonConsume);
			mHolder.llConsumed = (LinearLayout) convertView
					.findViewById(R.id.ll_consumed);
			mHolder.llBackOrder = (LinearLayout) convertView
					.findViewById(R.id.ll_backOrder);
			mHolder.btnDelete = (Button) convertView
					.findViewById(R.id.btn_delete);
			mHolder.btnRightAwayPay = (Button) convertView
					.findViewById(R.id.btn_rightAwayPay);
			mHolder.tvOrderCode = (TextView) convertView
					.findViewById(R.id.tv_orderCode);
			mHolder.btnRefund = (Button) convertView
					.findViewById(R.id.btn_refund);
			mHolder.tv_refundStatus = (TextView) convertView
					.findViewById(R.id.tv_refundStatus);

			mHolder.btnViewBarNonPayment = (Button) convertView
					.findViewById(R.id.btn_viewBar_nonPayment);
			mHolder.btnViewBarNonConsume = (Button) convertView
					.findViewById(R.id.btn_viewBar_nonConsume);
			convertView.setTag(mHolder);
		} else
			mHolder = (ViewHolder) convertView.getTag();

		ImageLoaderUtil.getImageLoader()
				.displayImage(
						DisplayUtils.getAbsolutelyUrl(listOrder.get(position)
								.getExt1()), mHolder.ivOrderHeader);
		mHolder.tvOrderName.setText(listOrder.get(position)
				.getProductDescription());
		mHolder.tvPubName.setText(listOrder.get(position).getExt3());
		mHolder.tvOrderPrice.setText(listOrder.get(position).getAmount() + "元");
		mHolder.btnDelete.setTag(position);
		mHolder.btnDelete.setOnClickListener(mListener);
		mHolder.btnRightAwayPay.setTag(position);
		mHolder.btnRightAwayPay.setOnClickListener(mListener);

		// view bar
		mHolder.btnViewBarNonPayment.setTag(position);
		mHolder.btnViewBarNonPayment.setOnClickListener(mListener);

		mHolder.btnViewBarNonConsume.setTag(position);
		mHolder.btnViewBarNonConsume.setOnClickListener(mListener);

		// click pubName jump to pubdetailActivity
		mHolder.tvPubName.setTag(position);
		mHolder.tvPubName.setOnClickListener(mListener);

		String payNo = listOrder.get(position).getPayNo();
		LogUtil.printPayLog("pay No encoded:" + payNo);
		String decodedPayNo = Base64Utils.decode(payNo);
		mHolder.tvOrderCode.setText(decodedPayNo);

		mHolder.btnRefund.setTag(position);
		mHolder.btnRefund.setOnClickListener(mListener);

		// when request order status, currently refund list displayed orders
		// which has been refunded.
		// should also consider whether refunding is being processed by now.
		if (listOrder.get(position).getStatusCode()
				.equals(PayUtil.ORDER_STATUS_REFUNDED)) {
			mHolder.tv_refundStatus.setText(R.string.refund_status_finished);
		} else if (listOrder.get(position).getStatusCode()
				.equals(PayUtil.ORDER_STATUS_REFUNDING)) {
			mHolder.tv_refundStatus.setText(R.string.refund_status_processing);
		}

		//
		LogUtil.printPayLog("order name:"
				+ listOrder.get(position).getProductDescription());
		LogUtil.printPayLog("order price:"
				+ listOrder.get(position).getAmount());

		if (orderType == OrderType.NONPAYMENT) {
			mHolder.llNonPayment.setVisibility(View.VISIBLE);
			mHolder.llNonConsume.setVisibility(View.GONE);
			mHolder.llConsumed.setVisibility(View.GONE);
			mHolder.llBackOrder.setVisibility(View.GONE);
		} else if (orderType == OrderType.NONCONSUME) {
			mHolder.llNonPayment.setVisibility(View.GONE);
			mHolder.llNonConsume.setVisibility(View.VISIBLE);
			mHolder.llConsumed.setVisibility(View.GONE);
			mHolder.llBackOrder.setVisibility(View.GONE);
		} else if (orderType == OrderType.CONSUMED) {
			mHolder.llNonPayment.setVisibility(View.GONE);
			mHolder.llNonConsume.setVisibility(View.GONE);
			mHolder.llConsumed.setVisibility(View.VISIBLE);
			mHolder.llBackOrder.setVisibility(View.GONE);
		} else if (orderType == OrderType.BACKORDER) {
			mHolder.llNonPayment.setVisibility(View.GONE);
			mHolder.llNonConsume.setVisibility(View.GONE);
			mHolder.llConsumed.setVisibility(View.GONE);
			mHolder.llBackOrder.setVisibility(View.VISIBLE);
		} else {
			mHolder.llNonPayment.setVisibility(View.GONE);
			mHolder.llNonConsume.setVisibility(View.GONE);
			mHolder.llConsumed.setVisibility(View.GONE);
			mHolder.llBackOrder.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return listOrder.size();
	}

	@Override
	public Object getItem(int position) {
		return listOrder.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		ImageView ivOrderHeader; // 订单头像
		TextView tvOrderName; // 订单名
		TextView tvOrderPrice; // 订单金额
		TextView tvPubName; // 酒吧名称
		LinearLayout llNonPayment, llNonConsume, llConsumed, llBackOrder; // 四种不同订单类型

		// 未付款
		Button btnDelete, btnRightAwayPay;

		// 未消费
		TextView tvOrderCode;
		Button btnRefund;

		// 退订单
		TextView tv_refundStatus;

		// 查看酒吧/订单
		Button btnViewBarNonPayment, btnViewBarNonConsume;
	}

	@SuppressLint("ShowToast")
	private class EventListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// Toast.makeText(context, "当前位置" + v.getTag(), 1).show();
			final int tag = (Integer) v.getTag();
			final OrderManageModel orderManageModel = listOrder.get(tag);
			OrderPayModel orderPayModel;
			String userId;

			switch (v.getId()) {
			case R.id.btn_delete:
				if(orderType == OrderType.NONPAYMENT){
					UmengUtil.onEvent(context, "unpayment_delete_hits");
					LogUtil.printUmengLog("unpayment_delete_hits");
				}
				LogUtil.printPayLog("OrderManageAdapter:start to delete");
				LogUtil.printPayLog("orderManageModel info:"
						+ orderManageModel.toString());

				userId = AppContext.getApplication().getUserInfo().getUserId();
				PayManager.delete(userId, orderManageModel.getTradeNo(),
						new OrderCallBack() {

							@Override
							public void onSuccess() {
								listOrder.remove(tag);
								adapter.notifyDataSetChanged();
								LogUtil.printPayLog("notify data changed");
							}

							@Override
							public void onFailure(String code) {

							}

						});

				break;

			case R.id.btn_rightAwayPay:
				LogUtil.printPayLog("OrderManageAdapter:rightly pay order");
				orderPayModel = new OrderPayModel(orderManageModel);

				((FragmentActivity) context)
						.getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_from_left,
								R.anim.slide_out_to_right)
						.replace(
								android.R.id.content,
								new OrderPayFragment(new OrderPayPresentModel(
										context, orderPayModel), null))
						.addToBackStack(null).commit();

				break;

			case R.id.btn_refund:
				LogUtil.printPayLog("OrderManageAdapter:start to refund");
				if(orderType == OrderType.NONCONSUME){
					UmengUtil.onEvent(context, "unexpended_refund_hits");
					LogUtil.printUmengLog("unexpended_refund_hits");
				}else if(orderType == OrderType.CONSUMED){
					UmengUtil.onEvent(context, "expended_amount");
					LogUtil.printUmengLog("expended_amount");
				}
				userId = AppContext.getApplication().getUserInfo().getUserId();
				PayManager.refund(userId, orderManageModel.getOrderNo(),
						new OrderCallBack() {

							@Override
							public void onSuccess() {
								LogUtil.printPayLog("refund succeed");
								Toast.makeText(
										context,
										context.getString(R.string.refund_operation_success),
										0);

								listOrder.remove(tag);
								adapter.notifyDataSetChanged();
								LogUtil.printPayLog("notify data changed");

//								((OrderManageActivity) context)
//										.getMyOrdersRequest();
							}

							@Override
							public void onFailure(String code) {
								LogUtil.printPayLog("refund failed");
								Toast.makeText(
										context,
										context.getString(R.string.refund_operation_failed),
										0);
							}

						});
				break;

			case R.id.btn_viewBar_nonPayment:
			case R.id.btn_viewBar_nonConsume:
				LogUtil.printPayLog("OrderManageAdapter:view bar. bar id:"
						+ orderManageModel.getExtInt2() + " bar name:"
						+ orderManageModel.getExt3());
				//
				// Intent intent = new Intent(context,
				// PubDetailsActivity.class);
				// intent.putExtra(GlobleConstant.PUB_ID,
				// Integer.parseInt(orderManageModel.getExtInt2()));
				// intent.putExtra(GlobleConstant.PUB_NAME,
				// orderManageModel.getExt3());
				// context.startActivity(intent);

				break;

			case R.id.tv_pubName:
				Intent intent = new Intent(context, PubDetailsActivity.class);
				intent.putExtra(GlobleConstant.PUB_ID,
						Integer.parseInt(orderManageModel.getExtInt2()));
				intent.putExtra(GlobleConstant.PUB_NAME,
						orderManageModel.getExt3());
				context.startActivity(intent);
				break;
			}
		}

	}
}
