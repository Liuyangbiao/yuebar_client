package com.fullteem.yueba.app.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.app.ui.PicturesActivity;
import com.fullteem.yueba.model.PicModle;

public class PicAdapter extends
		BaseDataAdapter.Build<PicModle, PicAdapter.ViewHolder> {
	private Context context;

	public PicAdapter(List<PicModle> listData) {
		super(listData, R.drawable.img_loading_default_big,
				R.drawable.img_loading_empty_big,
				R.drawable.img_loading_fail_big);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View itemView = View.inflate(context = viewGroup.getContext(),
				R.layout.adapter_onlypic, null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		showImage(holder.imgPic, listData.get(position).getBarImgUrl());
		holder.imgPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PicturesActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("list", (Serializable) listData);
				intent.putExtra("position", position);
				intent.putExtra("images", listData.get(position).getBarImgUrl());// 非必须
				int[] location = new int[2];
				holder.imgPic.getLocationOnScreen(location);
				intent.putExtra("locationX", location[0]);// 必须
				intent.putExtra("locationY", location[1]);// 必须
				intent.putExtra("width", holder.imgPic.getWidth());// 必须
				intent.putExtra("height", holder.imgPic.getHeight());// 必须
				intent.putExtra("bundle", b);
				context.startActivity(intent);
				((BaseActivity) context).overridePendingTransition(0, 0);
			}
		});
	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		ImageView imgPic;

		public ViewHolder(View itemView) {
			super(itemView);
			imgPic = (ImageView) itemView.findViewById(R.id.imgPic);
		}

	}

}
