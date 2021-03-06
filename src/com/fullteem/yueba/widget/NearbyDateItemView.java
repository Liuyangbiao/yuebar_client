package com.fullteem.yueba.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.model.DateModel;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月2日&emsp;下午2:20:06
 * @use 自定义view,用于viewpager不确定个数且View只加载一个xml页面文件
 */
public class NearbyDateItemView extends View {
	private Context context;

	public NearbyDateItemView(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 创建一个item视图，布局文件为
	 * 
	 * @return
	 */
	public View createItemView(final DateModel successfulDateModel) {
		View view = View
				.inflate(context, R.layout.adapter_date_elistview, null);
		CircleImageView ivPubHeader = (CircleImageView) view
				.findViewById(R.id.ImgViewHeader);
		TextView tvPubName = (TextView) view.findViewById(R.id.tv_pubname);
		TextView tvDateTitle = (TextView) view.findViewById(R.id.tv_date_title);
		TextView tvSponsor = (TextView) view.findViewById(R.id.tv_sponsor);
		ImageView ivSponsorHeader = (ImageView) view
				.findViewById(R.id.iv_sponsor_header);
		TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
		TextView tvAttendPeoples = (TextView) view
				.findViewById(R.id.tv_attendpeoples);
		LinearLayout llInvolved = (LinearLayout) view
				.findViewById(R.id.ll_involved);
		llInvolved.setVisibility(View.GONE);
		TextView tvExpect = (TextView) view.findViewById(R.id.tv_expect);

		// ivPubHeader.setBackgroundColor(Color.TRANSPARENT);
		// ivSponsorHeader.setBackgroundColor(Color.TRANSPARENT);
		ImageLoaderUtil.getImageLoader().displayImage(
				DisplayUtils.getAbsolutelyUrl(successfulDateModel
						.getBarLogoUrl()),
				ivPubHeader,
				ImageLoaderUtil.getOptions(R.drawable.img_loading_default_copy,
						R.drawable.img_loading_empty_copy,
						R.drawable.img_loading_fail_copy));
		ImageLoaderUtil.getImageLoader().displayImage(
				DisplayUtils.getAbsolutelyUrl(successfulDateModel
						.getUserLogoUrl()),
				ivSponsorHeader,
				ImageLoaderUtil.getOptions(R.drawable.img_loading_default,
						R.drawable.img_loading_empty,
						R.drawable.img_loading_fail));
		tvDateTitle.setText(successfulDateModel.getUserAppointmentTitle());
		tvPubName.setText(successfulDateModel.getBarName());
		tvSponsor.setText(successfulDateModel.getUserName());
		tvTime.setText(successfulDateModel.getUserAppointmentDate());
		tvAttendPeoples.setText("" + successfulDateModel.getHasCount());
		tvExpect.setText(successfulDateModel.getUserAppointmentObj());

		return view;
	}
}
