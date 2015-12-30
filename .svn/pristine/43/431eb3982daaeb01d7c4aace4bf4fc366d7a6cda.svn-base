package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.model.GiftModel;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;
import com.fullteem.yueba.widget.HintContentPopWindow;
//import com.fullteem.yueba.model.presentmodel.BuyGiftPopWPresentModel;
//import com.fullteem.yueba.widget.BuyGiftPopWindow;
//import com.fullteem.yueba.widget.BuyGiftPopWindow.OnPayButtonClickListener;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月8日&emsp;下午4:36:17
 * @use 礼物adapter
 */
public class GiftAdapter extends BaseAdapter {
	private List<GiftModel> listGift;
	private int singerId = -1;// 送礼页面跳过来？
	// private BuyGiftPopWindow mPopWindow;
	private HintContentPopWindow buyGiftSuccessWindow;
	private Activity mActivity;
	private int surplusGold = -1;
	private IBuyGiftPopupWindDismissListener onDismissListener;

	public GiftAdapter(Activity mActivity, List<GiftModel> listGift,
			int singerId, IBuyGiftPopupWindDismissListener onDismissListener) {
		this.mActivity = mActivity;
		this.listGift = listGift;
		this.singerId = singerId;
		this.onDismissListener = onDismissListener;
		initPopupWindow();
	}

	public void setSurplusGold(int surplusGold) {
		this.surplusGold = surplusGold;
	}

	private void initPopupWindow() {
		/*
		 * mPopWindow = mPopWindow == null ? new BuyGiftPopWindow(mActivity, new
		 * BuyGiftPopWPresentModel(mActivity, new OrderPayModel())) :
		 * mPopWindow; buyGiftSuccessWindow = buyGiftSuccessWindow == null ? new
		 * HintContentPopWindow(mActivity) : buyGiftSuccessWindow;
		 * buyGiftSuccessWindow.getPopupWindow().setOnDismissListener(new
		 * OnDismissListener() {
		 * 
		 * @Override public void onDismiss() { if (onDismissListener != null)
		 * onDismissListener.onDismiss(); } });
		 */
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = View.inflate(parent.getContext(),
					R.layout.adapter_gift_xlistview, null);
			mHolder.ivGiftHeader = (ImageView) convertView
					.findViewById(R.id.iv_giftHeader);
			mHolder.tvGiftName = (TextView) convertView
					.findViewById(R.id.tv_giftName);
			mHolder.tvGiftRemnants = (TextView) convertView
					.findViewById(R.id.tv_giftRemnants);
			mHolder.tvGold = (TextView) convertView.findViewById(R.id.tv_gold);
			mHolder.tvScore = (TextView) convertView
					.findViewById(R.id.tv_score);
			mHolder.btnBuyGift = (TextView) convertView
					.findViewById(R.id.btn_buyGift);
			convertView.setTag(mHolder);
		} else
			mHolder = (ViewHolder) convertView.getTag();

		ImageLoaderUtil.getImageLoader().displayImage(
				DisplayUtils.getAbsolutelyUrl(listGift.get(position)
						.getGiftLogoUrl()), mHolder.ivGiftHeader);
		mHolder.tvGiftName.setText(listGift.get(position).getGiftName());
		mHolder.tvGiftRemnants.setText(listGift.get(position).getTotal() + "");
		mHolder.tvGold.setText("" + listGift.get(position).getGold());
		mHolder.tvScore.setText("" + listGift.get(position).getCharm());
		if (singerId == -1) {
			mHolder.btnBuyGift.setText(parent.getContext().getString(
					R.string.buy));
			mHolder.btnBuyGift.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					buyGift(position);
				}
			});
		} else {
			if (listGift.get(position).getTotal() > 0) {
				mHolder.btnBuyGift.setText(parent.getContext().getString(
						R.string.give));
				mHolder.btnBuyGift.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO 赠送接口
					}
				});
			} else {
				mHolder.btnBuyGift.setText(parent.getContext().getString(
						R.string.buyAndGive));
				mHolder.btnBuyGift.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO 购买并赠送实现
						buyGift(position);
					}
				});
			}
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return listGift.size();
	}

	@Override
	public Object getItem(int position) {
		return listGift.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private void buyGift(int position) {
		/*
		 * OrderPayModel orderPayModel = new OrderPayModel();
		 * orderPayModel.setOrderName(listGift.get(position).getGiftName());
		 * orderPayModel.setOrderPrice(listGift.get(position).getGold() + "");
		 * mPopWindow.getModel().setModel(orderPayModel);
		 * mPopWindow.showWindow(new OnPayButtonClickListener() {
		 * 
		 * @Override public void onPayClick(View v, BuyGiftPopWPresentModel
		 * presentModel) { if ((surplusGold -
		 * Integer.valueOf(presentModel.getModle().getOrderPriceTotal())) < 0) {
		 * presentModel
		 * .setErrorState(mActivity.getString(R.string.hint_surplusGoldNotEnough
		 * )); return; } surplusGold -=
		 * Integer.valueOf(presentModel.getModle().getOrderPriceTotal()); //TODO
		 * 座等金币购买礼物接口
		 * 
		 * mPopWindow.getPopupWindow().dismiss();
		 * 
		 * SpannableStringBuilder tvContent = new SpannableStringBuilder();
		 * 
		 * //花费 SpannableStringBuilder builderCost = new
		 * SpannableStringBuilder(mActivity.getString(R.string.cost) +
		 * presentModel.getOrderPriceTotal()); builderCost.setSpan(new
		 * ForegroundColorSpan
		 * (mActivity.getResources().getColor(R.color.girl_red)), 2,
		 * builderCost.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //剩余
		 * SpannableStringBuilder builderSurplus = new
		 * SpannableStringBuilder(mActivity.getString(R.string.surplus) +
		 * surplusGold + mActivity.getString(R.string.gold));
		 * builderSurplus.setSpan(new
		 * ForegroundColorSpan(mActivity.getResources(
		 * ).getColor(R.color.girl_red)), 2, builderSurplus.length(),
		 * Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		 * 
		 * tvContent.append(mActivity.getString(R.string.hint_buyGiftSuccess));
		 * tvContent.append(builderCost); tvContent.append("\n");
		 * tvContent.append(builderSurplus);
		 * 
		 * buyGiftSuccessWindow.showWindow(tvContent).setCenterGravity(Gravity.LEFT
		 * ); } });
		 */
	}

	private class ViewHolder {
		ImageView ivGiftHeader; // 礼物头像
		TextView tvGiftName;// 礼物名称
		TextView tvGiftRemnants;// 礼物剩余数量
		TextView tvGold;// 礼物价格-金币
		TextView tvScore;// 礼物价格-积分 /礼物魅力值
		TextView btnBuyGift;// 购买按钮
	}

	/**
	 * 购买礼物弹窗消失后回调
	 * 
	 * @author jun
	 * 
	 */
	public interface IBuyGiftPopupWindDismissListener {
		void onDismiss();
	}
}
