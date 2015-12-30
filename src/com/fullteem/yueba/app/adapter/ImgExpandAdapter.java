package com.fullteem.yueba.app.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.BaseActivity;
import com.fullteem.yueba.app.ui.PicturesActivity;
import com.fullteem.yueba.model.AlbumPhotoModel;
import com.fullteem.yueba.util.DisplayUtils;

/**
 * @author jun
 * @version 1.0.0
 * @created 2015年1月19日&emsp;下午5:04:49
 * @use 展开图片Adapter
 */
public class ImgExpandAdapter extends
		BaseDataAdapter.Build<String, ImgExpandAdapter.ViewHolder> {
	private Context context;
	private Activity activity;

	public ImgExpandAdapter(List<String> listData) {
		super(listData, R.drawable.img_loading_default_copy,
				R.drawable.img_loading_empty_copy,
				R.drawable.img_loading_fail_copy);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		context = context == null ? viewGroup.getContext() : context;
		activity = (Activity) context;
		View itemView = View.inflate(context, R.layout.adapter_dynamic_grid,
				null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		int width = DisplayUtils.getScreenWidht(context);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams((int) (width / 3.6),
				width / 5);
		ll.setMargins(3, 2, 0, 2);
		ll.gravity = Gravity.CENTER;
		holder.imgMyPics.setLayoutParams(ll);
		showImage(holder.imgMyPics, listData.get(position));
		imgOnClicked(holder.imgMyPics, position);

	}

	private void imgOnClicked(final ImageView imgMyPics, final int position) {
		// TODO Auto-generated method stub

		imgMyPics.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("心情图片被点击了");
				Intent intent = new Intent(context, PicturesActivity.class);
				intent.putExtra("position", position);
				intent.putExtra("images", listData.get(position));// 非必须
				int[] location = new int[2];
				imgMyPics.getLocationOnScreen(location);
				intent.putExtra("locationX", location[0]);// 必须
				intent.putExtra("locationY", location[1]);// 必须
				intent.putExtra("width", imgMyPics.getWidth());// 必须
				intent.putExtra("height", imgMyPics.getHeight());// 必须
				intent.putExtra("list", (Serializable) (listData));
				context.startActivity(intent);
				activity.overridePendingTransition(0, 0);

			}
		});

	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		ImageView imgMyPics;

		public ViewHolder(View itemView) {
			super(itemView);
			imgMyPics = (ImageView) itemView.findViewById(R.id.imgMyPics);
		}
	}

}
