package com.fullteem.yueba.app.singer.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BaseDataAdapter;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.Build;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ViewHolder;
import com.fullteem.yueba.app.singer.VideoPlayActivity;
import com.fullteem.yueba.app.singer.VideoPlayerView;
import com.fullteem.yueba.app.singer.model.SingerVideoListModel;
import com.fullteem.yueba.globle.GlobleConstant;

public class VideoListAdapter
		extends
		BaseDataAdapter.Build<SingerVideoListModel, VideoListAdapter.ViewHolder> {
	private Context context;

	public VideoListAdapter(Context context, List<SingerVideoListModel> listData) {
		super(listData, R.drawable.img_loading_default_big,
				R.drawable.img_loading_empty_big,
				R.drawable.img_loading_fail_big);
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = View.inflate(viewGroup.getContext(),
				R.layout.adapter_singer_videolist, null);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		showText(holder.tvVideoIntroduction, listData.get(position)
				.getVideoDetail());
		// holder.viewVideoPlay.setIsShowCenterButton(false);
		showImage(holder.ivVideoBg, listData.get(position).getVideoLogoUrl());

		holder.ivVideoBg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int videoId = listData.get(position).getVideoId();
				if (videoId == -1) {
					Toast.makeText(context,
							context.getString(R.string.hint_videoLoadingError),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(context, VideoPlayActivity.class);
				intent.putExtra(GlobleConstant.VIDEO_ID, videoId);
				context.startActivity(intent);
			}
		});

		// holder.viewVideoPlay.init(null, null,
		// listData.get(position).getPlayUri(), null);
	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		private TextView tvVideoIntroduction;
		private VideoPlayerView viewVideoPlay;
		private ImageView ivVideoBg;

		private Button btnPlayAndPause;

		public ViewHolder(View itemView) {
			super(itemView);
			tvVideoIntroduction = (TextView) itemView
					.findViewById(R.id.tv_videoIntroduction);
			viewVideoPlay = (VideoPlayerView) itemView
					.findViewById(R.id.view_videoPlay);
			ivVideoBg = (ImageView) itemView.findViewById(R.id.iv_videoBg);
			btnPlayAndPause = (Button) itemView
					.findViewById(R.id.btn_playAndPause);
		}
	}
}
