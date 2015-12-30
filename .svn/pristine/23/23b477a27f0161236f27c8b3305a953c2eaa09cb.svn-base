package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ImageType;
import com.fullteem.yueba.app.ui.ChatActivity;
import com.fullteem.yueba.app.ui.GroupDetailActivity;
import com.fullteem.yueba.model.GroupsModel.Groups;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.widget.CircleImageView;

/**
 * 群组adapter
 * 
 * @author ssy
 * 
 */
public class GroupAdapter extends
		BaseDataAdapter.Build<Groups, GroupAdapter.ViewHodler> {
	private Context context;
	private List<Groups> list;

	public GroupAdapter(Activity context, List<Groups> list) {
		super(list, ImageType.NO_DEFAULT);
		this.context = context;
		this.list = list;
	}

	@Override
	public ViewHodler onCreateViewHolder(ViewGroup viewGroup, final int position) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_groups, null);
		itemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String groupId = list.get(position).getGroupId();
				// 栏目点击跳转
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				intent.putExtra("groupId", groupId);
				context.startActivity(intent);
			}
		});
		return new ViewHodler(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHodler holder, final int position) {
		holder.tvName.setText(listData.get(position).getGroupName());
		holder.tvPeopleNums.setText(StringUtils.formatStrD2Str(context,
				R.string.group_peoplenums, listData.get(position).getTotal(),
				listData.get(position).getNum()));
		showText(holder.tvSpace, listData.get(position).getDistance());
		showText(holder.tvMood, listData.get(position).getGroupDesc());
		showImage(holder.ImgViewHeader, listData.get(position).getGroupIcon());
		holder.ImgViewHeader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String groupId = list.get(position).getGroupId();
				if (groupId == null) {
					Toast.makeText(
							context,
							context.getResources().getString(
									R.string.group_jump_error),
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(context,
							GroupDetailActivity.class);
					intent.putExtra("GROUP_ID", groupId);
					context.startActivity(intent);
				}
			}
		});
	}

	class ViewHodler extends BaseDataAdapter.ViewHolder {
		TextView tvName;
		TextView tvSpace;
		TextView tvPeopleNums;
		TextView tvMood;
		CircleImageView ImgViewHeader;

		public ViewHodler(View itemView) {
			super(itemView);
			ImgViewHeader = (CircleImageView) itemView
					.findViewById(R.id.ImgViewHeader);
			tvName = (TextView) itemView.findViewById(R.id.tvName);
			tvSpace = (TextView) itemView.findViewById(R.id.tvSpace);
			tvPeopleNums = (TextView) itemView.findViewById(R.id.tvPeopleNums);
			tvMood = (TextView) itemView.findViewById(R.id.tvMood);
		}

	}

}
