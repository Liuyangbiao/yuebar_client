package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ImageType;
import com.fullteem.yueba.app.ui.DateManageDetailAcitvity;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.model.DateModel;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月16日&emsp;下午3:15:28
 * @use 类说明
 */
public class DateManageAdapter extends
		BaseDataAdapter.Build<DateModel, DateManageAdapter.ViewHolder> {

	/**
	 * 约会类型
	 */
	public enum DateType {
		/** 已发布约会 */
		DATEPUBLISHED,
		/** 已报名约会 */
		DATEENROLLED
	}

	private DateType dateType;

	public DateManageAdapter(List<DateModel> listData, DateType dateType) {
		super(listData, ImageType.NO_DEFAULT);
		this.dateType = dateType;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_date_manage, null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		if (dateType == DateType.DATEPUBLISHED) {
			holder.btnDateManage.setVisibility(View.VISIBLE);
			holder.ivDataState.setVisibility(View.GONE);
			holder.llEnrolled.setVisibility(View.GONE);
			holder.llexpect.setVisibility(View.GONE);
		}
		if (dateType == DateType.DATEENROLLED) {
			holder.btnDateManage.setVisibility(View.GONE);
			holder.ivDataState.setVisibility(View.VISIBLE);
			holder.llEnrolled.setVisibility(View.VISIBLE);
			holder.llexpect.setVisibility(View.VISIBLE);
		}
		holder.btnDateManage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						DateManageDetailAcitvity.class);
				DateModel dateModel = listData.get(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(GlobleConstant.DATE_MODEL, dateModel);
				intent.putExtras(bundle);
				v.getContext().startActivity(intent);
			}
		});
		int dateState = listData.get(position).getStatus();
		if (dateState == 1) {
			dateState = listData.get(position).getUserAppointmentJoinType();
			holder.ivDataState
					.setImageResource(dateState == 0 ? R.drawable.img_state_reject
							: dateState == 1 ? R.drawable.img_state_joined
									: R.drawable.img_state_apply);
			holder.btnDateManage.setText(holder.btnDateManage.getContext()
					.getString(R.string.hint_manage));
		} else {
			holder.ivDataState
					.setImageResource(dateState == 0 ? R.drawable.img_state_cancel
							: R.drawable.img_state_stop);
			holder.btnDateManage.setText(dateState == 0 ? holder.btnDateManage
					.getContext().getString(R.string.hint_cancled)
					: holder.btnDateManage.getContext().getString(
							R.string.hint_sured));
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
		Button btnDateManage;
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
			btnDateManage = (Button) itemView.findViewById(R.id.btn_dateManage);
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
