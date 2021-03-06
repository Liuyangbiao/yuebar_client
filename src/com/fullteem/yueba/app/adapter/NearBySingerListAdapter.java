package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.singer.model.SingerListModel;
import com.fullteem.yueba.app.ui.PubDetailsActivity;
import com.fullteem.yueba.globle.GlobleConstant;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月19日&emsp;下午3:02:53
 * @use 歌手列表Adapter
 */
public class NearBySingerListAdapter
		extends
		BaseDataAdapter.Build<SingerListModel, NearBySingerListAdapter.ViewHolder> {
	private Context context;

	public NearBySingerListAdapter(Context context,
			List<SingerListModel> listData) {
		super(listData, R.drawable.img_loading_default,
				R.drawable.img_loading_empty, R.drawable.img_loading_fail);
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_singer_xlistview, null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		showImage(holder.ImgViewHeader, TextUtils.isEmpty(listData
				.get(position).getLogoUrl()) ? listData.get(position)
				.getUserLogoUrl() : listData.get(position).getLogoUrl());
		showText(holder.tvName, listData.get(position).getSingerName());
		showText(holder.tvSingerValue, listData.get(position).getSingerDetail());
		showText(holder.tvComments, listData.get(position).getPageviews());
		showText(holder.tvPraise, listData.get(position).getPraiseTotal());
		/*
		 * if (TextUtils.isEmpty(listData.get(position).getBarName()))
		 * holder.btnBarName.setVisibility(View.GONE); else {
		 * holder.btnBarName.setVisibility(View.VISIBLE);
		 * showText(holder.btnBarName, listData.get(position).getBarName()); }
		 */

		holder.btnBarName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PubDetailsActivity.class);
				intent.putExtra(GlobleConstant.PUB_ID, listData.get(position)
						.getBarId());
				context.startActivity(intent);
			}
		});
	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		ImageView ImgViewHeader; // 头像
		TextView tvName;// 歌手名
		Button btnBarName;// 驻场酒吧名
		TextView tvSingerValue;// 简介
		TextView tvComments;// 浏览量、访问量
		TextView tvPraise;// 点赞数

		public ViewHolder(View itemView) {
			super(itemView);
			ImgViewHeader = (ImageView) itemView
					.findViewById(R.id.ImgViewHeader);
			tvName = (TextView) itemView.findViewById(R.id.tvName);
			btnBarName = (Button) itemView.findViewById(R.id.btnBarName);
			tvSingerValue = (TextView) itemView
					.findViewById(R.id.tvSingerValue);
			tvComments = (TextView) itemView.findViewById(R.id.tvComments);
			tvPraise = (TextView) itemView.findViewById(R.id.tvPraise);
		}
	}

}
