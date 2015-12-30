package com.fullteem.yueba.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ImageType;
import com.fullteem.yueba.app.singer.model.SingerDetailModel;
import com.fullteem.yueba.model.AlbumPhotoModel;
import com.fullteem.yueba.model.MoodModel;

import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.widget.ExpandGridView;

public class MoodDetailsAdapter extends
		BaseDataAdapter.Build<MoodModel, MoodDetailsAdapter.ViewHolder> {
	private SingerDetailModel singerModel;

	public MoodDetailsAdapter(List<MoodModel> listData,
			SingerDetailModel singerModel) {
		super(listData, ImageType.NO_DEFAULT);
		this.singerModel = singerModel;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		LogUtil.LogDebug(null, "onCreateViewHolder" + i, null);
		View view = View.inflate(viewGroup.getContext(),
				R.layout.adapter_mood_details_listview, null);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		showImage(holder.ivUserHeader, singerModel.getLogoUrl());
		showText(holder.tvNickname, singerModel.getSingerName());
		showText(holder.tvCreateDate, listData.get(position).getCreateDate());
		showText(holder.tvMoodContent, listData.get(position)
				.getMoodRecordText());
		showText(holder.tvMoodComments, listData.get(position).getPageviews());
		showText(holder.tvMoodPraise, listData.get(position).getPraise());

		String urls = listData.get(position).getMoodRecordImgUrl();
		if (TextUtils.isEmpty(urls)) {
			holder.gridView.setVisibility(View.GONE);
			return;
		}
		holder.gridView.setVisibility(View.VISIBLE);
		String[] urlsArray;
		if (urls != null && urls.length() > 0 && urls.contains(",")) {
			urlsArray = urls.split(",");
		} else
			urlsArray = new String[] { urls };

		List<String> strList = new ArrayList<String>();
		for (String string : urlsArray) {
			strList.add(string);
		}
		holder.gridView.setAdapter(new ImgExpandAdapter(strList));
	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		ImageView ivUserHeader; // 用户头像
		TextView tvNickname; // 用户昵称
		TextView tvCreateDate; // 心情发布日期
		ExpandGridView gridView; // 心情配图
		TextView tvMoodContent;// 心情内容
		TextView tvMoodComments;// 心情评论数还是浏览量
		TextView tvMoodPraise; // 心情点赞数

		public ViewHolder(View itemView) {
			super(itemView);
			ivUserHeader = (ImageView) itemView
					.findViewById(R.id.iv_userHeader);
			tvNickname = (TextView) itemView.findViewById(R.id.tv_nickname);
			tvCreateDate = (TextView) itemView.findViewById(R.id.tv_postDate);
			gridView = (ExpandGridView) itemView.findViewById(R.id.exGridview);
			tvMoodContent = (TextView) itemView
					.findViewById(R.id.tv_moodContent);
			tvMoodComments = (TextView) itemView
					.findViewById(R.id.tv_moodComments);
			tvMoodPraise = (TextView) itemView.findViewById(R.id.tv_moodPraise);
		}
	}

}
