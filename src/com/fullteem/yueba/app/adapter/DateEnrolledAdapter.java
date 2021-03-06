package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ImageType;
import com.fullteem.yueba.model.DateModel;

/**
 * 我报名的也会adapter
 * 
 * @author jecinwang
 * 
 */
public class DateEnrolledAdapter extends
		BaseDataAdapter.Build<DateModel, DateEnrolledAdapter.ViewHolder> {

	public DateEnrolledAdapter(List<DateModel> listData) {
		super(listData, ImageType.NO_DEFAULT);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_date_enrolled, null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {

		int dateState = listData.get(position).getStatus();
		if (dateState == 1) {
			dateState = listData.get(position).getUserAppointmentJoinType();
			holder.ivDataState
					.setImageResource(dateState == 0 ? R.drawable.img_state_reject
							: dateState == 1 ? R.drawable.img_state_joined
									: R.drawable.img_state_apply);
		} else {
			holder.ivDataState
					.setImageResource(dateState == 0 ? R.drawable.img_state_cancel
							: R.drawable.img_state_stop);
		}

		showImage(holder.ivPubHeader, listData.get(position).getBarLogoUrl());
		showText(holder.tvDateTitle, listData.get(position)
				.getUserAppointmentTitle());
		showText(holder.tvPubName, listData.get(position).getBarName());
		showText(holder.tvTime, listData.get(position).getUserAppointmentDate());
		showText(holder.tvAttendpeoples, listData.get(position).getCount() + "");
		showText(holder.tvInvolved, listData.get(position).getHasCount() + "");
		showText(holder.tvSponsor, listData.get(position).getUserName());
		showText(holder.tvExpect, listData.get(position)
				.getUserAppointmentObj());
		showImage(holder.ivSponsorHeader, listData.get(position)
				.getUserLogoUrl());
	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		ImageView ivPubHeader, ivDataState;
		TextView tvDateTitle;
		TextView tvPubName;
		TextView tvTime;
		TextView tvAttendpeoples;
		TextView tvInvolved;
		LinearLayout llEnrolled, llexpect;
		TextView tvExpect, tvSponsor;
		ImageView ivSponsorHeader;

		public ViewHolder(View itemView) {
			super(itemView);
			ivPubHeader = (ImageView) itemView.findViewById(R.id.iv_pubHeader);
			tvDateTitle = (TextView) itemView.findViewById(R.id.tv_date_title);
			tvPubName = (TextView) itemView.findViewById(R.id.tv_pubname);
			tvTime = (TextView) itemView.findViewById(R.id.tv_time);
			tvAttendpeoples = (TextView) itemView
					.findViewById(R.id.tv_attendpeoples);
			tvInvolved = (TextView) itemView.findViewById(R.id.tv_involved);
			ivDataState = (ImageView) itemView.findViewById(R.id.ivDataState);
			llEnrolled = (LinearLayout) itemView.findViewById(R.id.llEnrolled);
			llexpect = (LinearLayout) itemView.findViewById(R.id.ll_expect);
			tvExpect = (TextView) itemView.findViewById(R.id.tv_expect);
			tvSponsor = (TextView) itemView.findViewById(R.id.tv_sponsor);
			ivSponsorHeader = (ImageView) itemView
					.findViewById(R.id.iv_sponsor_header);
		}
	}
}
