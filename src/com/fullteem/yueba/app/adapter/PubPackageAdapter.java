package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ImageType;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.app.ui.fragment.OrderPayFragment;
import com.fullteem.yueba.model.OrderPayModel;
import com.fullteem.yueba.model.PubPackageModel;
import com.fullteem.yueba.model.presentmodel.OrderPayPresentModel;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.UmengUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月15日&emsp;下午1:54:56
 * @use 酒吧详情-套餐adapter
 */
public class PubPackageAdapter extends
		BaseDataAdapter.Build<PubPackageModel, PubPackageAdapter.ViewHolder> {
	private BaseActivity activity;
	private int payState;
	private boolean isFromPub;//是否从酒吧详情页面进入套餐adapter

	public PubPackageAdapter(BaseActivity activity,
			List<PubPackageModel> listData, int payState,boolean isFromPub) {
		super(listData, ImageType.NO_DEFAULT);
		this.activity = activity;
		this.payState = payState;
		this.isFromPub = isFromPub;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_pub_package, null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		showText(holder.tvPackageName, listData.get(position)
				.getBarCouponName());
		showText(holder.tvPackageCOntent, listData.get(position)
				.getBarCouponDetail());

		SpannableStringBuilder builder = new SpannableStringBuilder(
				StringUtils.formatStrD2Str(holder.tvPackagePrice.getContext(),
						R.string.order_price, listData.get(position)
								.getBarCouponPrice()));
		builder.setSpan(holder.choseColor, 3, builder.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		showText(holder.tvPackagePrice, builder);
		if (TextUtils.isEmpty(listData.get(position).getBarCouponImgUrl())) {
			holder.ivPackageHeader.setVisibility(View.GONE);
		} else {
			holder.ivPackageHeader.setVisibility(View.VISIBLE);
			showImage(holder.ivPackageHeader, listData.get(position)
					.getBarCouponImgUrl());
		}
		
		if(payState == 0){
			holder.btnBuy.setVisibility(View.VISIBLE);
		}else if(payState == 1){
			holder.btnBuy.setVisibility(View.INVISIBLE);
		}

		holder.btnBuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isFromPub){
					UmengUtil.onEvent(activity , "bar_buy_drinks_button_hits");
					LogUtil.printUmengLog("bar_buy_drinks_button_hits");
				}else{
					UmengUtil.onEvent(activity , "buy_button_hits");
					LogUtil.printUmengLog("buy_button_hits");
				}
				PubPackageModel packageModel = listData.get(position);
				OrderPayModel orderPayModel = new OrderPayModel(packageModel);

				(activity)
						.getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_from_left,
								R.anim.slide_out_to_right)
						.replace(
								android.R.id.content,
								new OrderPayFragment(new OrderPayPresentModel(
										activity, orderPayModel), null))
						.addToBackStack(null).commit();
			}
		});
	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		TextView tvPackageName, tvPackageCOntent, tvPackagePrice;
		Button btnBuy;
		ImageView ivPackageHeader;
		ForegroundColorSpan choseColor;

		public ViewHolder(View itemView) {
			super(itemView);
			tvPackageName = (TextView) itemView
					.findViewById(R.id.tv_packageName);
			tvPackageCOntent = (TextView) itemView
					.findViewById(R.id.tv_packageCOntent);
			tvPackagePrice = (TextView) itemView
					.findViewById(R.id.tv_packagePrice);
			btnBuy = (Button) itemView.findViewById(R.id.btn_buy);
			ivPackageHeader = (ImageView) itemView
					.findViewById(R.id.ivPackageHeader);
			choseColor = new ForegroundColorSpan(itemView.getContext()
					.getResources().getColor(R.color.girl_red));
		}
	}

}
