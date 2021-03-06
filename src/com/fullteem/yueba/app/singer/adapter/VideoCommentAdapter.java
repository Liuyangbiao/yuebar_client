package com.fullteem.yueba.app.singer.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BaseDataAdapter;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.Build;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ImageType;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ViewHolder;
import com.fullteem.yueba.app.singer.model.VideoCommentModel;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月17日&emsp;下午6:25:22
 * @use 视频评论adapter
 */
public class VideoCommentAdapter
		extends
		BaseDataAdapter.Build<VideoCommentModel, VideoCommentAdapter.ViewHolder> {

	public VideoCommentAdapter(List<VideoCommentModel> listData) {
		super(listData, ImageType.NO_DEFAULT);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_video_comment, null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		showImage(holder.ivHeader, listData.get(position).getHeader());
		showText(holder.tvName, listData.get(position).getNickname());
		showText(holder.tvTime, listData.get(position).getTime());
		showText(holder.tvContent, listData.get(position).getContent());
	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		ImageView ivHeader;
		TextView tvName, tvTime, tvContent;

		public ViewHolder(View itemView) {
			super(itemView);

			ivHeader = (ImageView) itemView.findViewById(R.id.ivHeader);
			tvName = (TextView) itemView.findViewById(R.id.tvName);
			tvTime = (TextView) itemView.findViewById(R.id.tvTime);
			tvContent = (TextView) itemView.findViewById(R.id.tvContent);
		}

	}

}
