package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.model.PubModel;
import com.fullteem.yueba.widget.CircleImageView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月18日&emsp;上午11:00:42
 * @use 类说明
 */
public class NearByPubListAdapter extends
		BaseDataAdapter.Build<PubModel, NearByPubListAdapter.ViewHold> {

	public NearByPubListAdapter(List<PubModel> listData) {
		super(listData, R.drawable.img_loading_default_copy,
				R.drawable.img_loading_empty_copy,
				R.drawable.img_loading_fail_copy);
	}

	@Override
	public ViewHold onCreateViewHolder(ViewGroup viewGroup, int position) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_pub_xlistview, null);
		return new ViewHold(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHold holder, int position) {
		showImage(holder.ImgViewHeader, listData.get(position).getBarLogoUrl());
		if (TextUtils.isEmpty(listData.get(position).getBarName()))
			holder.tvName.setVisibility(View.GONE);
		else {
			holder.tvName.setVisibility(View.VISIBLE);
			showText(holder.tvName, listData.get(position).getBarName());
		}
		String barType = listData.get(position).getBarType() + "";
		if (null != barType && "1".equals(barType)) {
			showText(holder.tvBarStyle, "清吧");
		} else if (null != barType && "2".equals(barType)) {
			showText(holder.tvBarStyle, "迪吧吧");
		} else if (null != barType && "3".equals(barType)) {
			showText(holder.tvBarStyle, "演绎吧");
		} else {
			showText(holder.tvBarStyle, listData.get(position).getDictName());
		}
		showText(holder.tvPlace, listData.get(position).getBarAddress());
		holder.RatingBarPraise.setRating(listData.get(position).getStar());
		if (listData.get(position).isCoupon())
			holder.ivCoupon.setVisibility(View.VISIBLE);
		else
			holder.ivCoupon.setVisibility(View.GONE);
		if (listData.get(position).isSingingBar())
			holder.ivSingingBar.setVisibility(View.VISIBLE);
		else
			holder.ivSingingBar.setVisibility(View.GONE);
		if (listData.get(position).isPromotion())
			holder.ivPromotion.setVisibility(View.VISIBLE);
		else
			holder.ivPromotion.setVisibility(View.GONE);
		if (listData.get(position).isReservation())
			holder.ivReservation.setVisibility(View.VISIBLE);
		else
			holder.ivReservation.setVisibility(View.GONE);
	}

	class ViewHold extends BaseDataAdapter.ViewHolder {
		CircleImageView ImgViewHeader;
		TextView tvName;
		RatingBar RatingBarPraise;
		TextView tvBarStyle;
		// TextView tvPay;
		TextView tvPlace;
		ImageView ivSingingBar, ivCoupon, ivPromotion, ivReservation;

		public ViewHold(View itemView) {
			super(itemView);
			ImgViewHeader = (CircleImageView) itemView
					.findViewById(R.id.ImgViewHeader);
			tvName = (TextView) itemView.findViewById(R.id.tvName);
			RatingBarPraise = (RatingBar) itemView
					.findViewById(R.id.RatingBarPraise);
			tvBarStyle = (TextView) itemView.findViewById(R.id.tvBarStyle);
			// tvPay = (TextView) itemView.findViewById(R.id.tvPay);
			tvPlace = (TextView) itemView.findViewById(R.id.tvPlace);
			ivCoupon = (ImageView) itemView.findViewById(R.id.ivCoupon);
			ivSingingBar = (ImageView) itemView.findViewById(R.id.ivSingingBar);
			ivPromotion = (ImageView) itemView.findViewById(R.id.ivPromotion);
			ivReservation = (ImageView) itemView
					.findViewById(R.id.ivReservation);
		}

	}
}
