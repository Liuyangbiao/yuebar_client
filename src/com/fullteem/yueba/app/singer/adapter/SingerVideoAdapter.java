package com.fullteem.yueba.app.singer.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.singer.SimpleVideoPlayActivity;
import com.fullteem.yueba.app.singer.model.SingerVideoListModel;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.UmengUtil;


public class SingerVideoAdapter extends PagerAdapter {
	List<View> singerVideoViewList;
	List<SingerVideoListModel> videoModelList;
	Context context;
	public SingerVideoAdapter(Context context, List<View> viewList, List<SingerVideoListModel> listVideo) {
		singerVideoViewList = viewList;
		videoModelList = listVideo;
		this.context = context;
	}

	@Override
	public int getCount() {
		return singerVideoViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position) {
		
		view.addView(singerVideoViewList.get(position));
		ImageView imgView = (ImageView) singerVideoViewList.get(
				position).findViewById(R.id.imgView);
		
		Button playBtn = (Button)  singerVideoViewList.get(
				position).findViewById(R.id.btn_playAndPause);

		imgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		
		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				UmengUtil.onEvent(context, "vedio_play_hits");
				LogUtil.printUmengLog("vedio_play_hits");
				Intent intent = new Intent(context, SimpleVideoPlayActivity.class);
				intent.putExtra("logoUrl", videoModelList.get(position).getVideoLogoUrl());
				intent.putExtra("videoUrl", videoModelList.get(position).getVideoPlayUri());
				context.startActivity(intent);
			}
			
		});
		
		return singerVideoViewList.get(position);
	}
}