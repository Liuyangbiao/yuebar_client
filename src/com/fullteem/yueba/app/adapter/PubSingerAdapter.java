package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ImageType;
import com.fullteem.yueba.app.singer.SingerVideoListActivity;
import com.fullteem.yueba.app.singer.model.SingerModel;
import com.fullteem.yueba.globle.GlobleConstant;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UmengUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月15日&emsp;下午1:54:56
 * @use 酒吧详情-歌手adapter
 */
public class PubSingerAdapter extends
		BaseDataAdapter.Build<SingerModel, PubSingerAdapter.ViewHolder> {
	private Context context;

	public PubSingerAdapter(Context context, List<SingerModel> listData) {
		super(listData, ImageType.NO_DEFAULT);
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int position) {
		View view = View.inflate(viewGroup.getContext(),
				R.layout.adapter_pub_singer, null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UmengUtil.onEvent(context, "bar_singer_hits");
				LogUtil.printUmengLog("bar_singer_hits");
				Intent intent = new Intent(context,
						SingerVideoListActivity.class);
				intent.putExtra(GlobleConstant.SINGER_ID, listData
						.get(position).getSingerId());
				intent.putExtra(GlobleConstant.SINGER_NAME,
						listData.get(position).getSingerName());
				context.startActivity(intent);

			}
		});
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		showImage(holder.ivUsrheader, listData.get(position).getLogoUrl());
		showText(holder.tvUsrnickname, listData.get(position).getSingerName());
		showText(holder.tvIntroduction, listData.get(position)
				.getSingerIntroduction());
		holder.btnVideo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context,
						SingerVideoListActivity.class);
				intent.putExtra(GlobleConstant.SINGER_ID, listData
						.get(position).getSingerId());
				context.startActivity(intent);

				// context.startActivity(new Intent(context,
				// VideoPlayActivity.class));
			}
		});
	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		ImageView ivUsrheader;
		TextView tvUsrnickname, tvIntroduction;
		Button btnVideo;

		public ViewHolder(View itemView) {
			super(itemView);
			ivUsrheader = (ImageView) itemView.findViewById(R.id.iv_usrheader);
			tvUsrnickname = (TextView) itemView
					.findViewById(R.id.tv_usrnickname);
			tvIntroduction = (TextView) itemView
					.findViewById(R.id.tv_introduction);
			btnVideo = (Button) itemView.findViewById(R.id.btn_video);
		}

	}
}
