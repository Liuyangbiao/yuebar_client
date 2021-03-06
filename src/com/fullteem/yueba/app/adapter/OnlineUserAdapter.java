package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ImageType;
import com.fullteem.yueba.model.OnlineUserModel;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.StringUtils.Gender;

public class OnlineUserAdapter extends
		BaseDataAdapter.Build<OnlineUserModel, OnlineUserAdapter.ViewHolder> {
	private Context context;

	public OnlineUserAdapter(Context context, List<OnlineUserModel> listData) {
		super(listData, ImageType.NO_DEFAULT);
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.adapter_pub_online_user, null);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		showImage(holder.ivUserHeader, listData.get(position).getUserLogoUrl());
		showText(holder.tvUserName, listData.get(position).getUserName());

		if ("男".equalsIgnoreCase(listData.get(position).getUserSex())) {
			StringUtils.changeStyle(context, holder.tvSex, Gender.GENDER_BOY);
		} else {
			StringUtils.changeStyle(context, holder.tvSex, Gender.GENDER_GIRL);
		}
		showText(holder.tvSex, listData.get(position).getUserAge());

	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		ImageView ivUserHeader;
		TextView tvUserName, tvSex;

		public ViewHolder(View itemView) {
			super(itemView);
			ivUserHeader = (ImageView) itemView
					.findViewById(R.id.iv_userHeader);
			tvUserName = (TextView) itemView.findViewById(R.id.tv_userName);
			tvSex = (TextView) itemView.findViewById(R.id.tvSex);
		}

	}
}
