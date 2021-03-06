package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.PerssonalInfoAcitivity;
import com.fullteem.yueba.model.DatePersonModel;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.StringUtils.Gender;

/**
 * @author jun
 * @use 已参与/已包名adapter
 */
public class EnrollAndPartakeAdapter
		extends
		BaseDataAdapter.Build<DatePersonModel, EnrollAndPartakeAdapter.ViewHolder> {
	private Context context;

	public EnrollAndPartakeAdapter(Context context,
			List<DatePersonModel> listData) {
		super(listData, R.drawable.img_loading_default,
				R.drawable.img_loading_empty, R.drawable.img_loading_fail);
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_datedetails_elistview, null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		showImage(holder.ivUsrHeader, listData.get(position).getUserHeaderUrl());
		showText(holder.tvUsrNickname, listData.get(position).getUserNickname());
		showText(holder.tvUsrMood, listData.get(position)
				.getUserPersonalitySignature());
		showText(holder.tvUsrAge, listData.get(position).getUserAge());

		if ("男".equals(listData.get(position).getUserGender())) {
			StringUtils
					.changeStyle(context, holder.tvUsrAge, Gender.GENDER_BOY);
		} else {
			StringUtils.changeStyle(context, holder.tvUsrAge,
					Gender.GENDER_GIRL);
		}

		holder.ivUsrHeader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						PerssonalInfoAcitivity.class);
				intent.putExtra("userId", listData.get(position).getUserId()
						+ "");
				context.startActivity(intent);
			}
		});

	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		ImageView ivUsrHeader;
		TextView tvUsrNickname;
		TextView tvUsrAge;
		TextView tvUsrMood;

		public ViewHolder(View itemView) {
			super(itemView);
			ivUsrHeader = (ImageView) itemView.findViewById(R.id.iv_usrheader);
			tvUsrNickname = (TextView) itemView
					.findViewById(R.id.tv_usrnickname);
			tvUsrAge = (TextView) itemView.findViewById(R.id.tv_userGender);
			tvUsrMood = (TextView) itemView.findViewById(R.id.tv_usrmood);
		}
	}
}
