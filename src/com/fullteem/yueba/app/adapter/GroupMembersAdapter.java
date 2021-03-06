package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.model.PicModle;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2015年1月12日&emsp;下午3:12:09
 * @use 群成员Adapter
 */
public class GroupMembersAdapter extends
		RecyclerView.Adapter<GroupMembersAdapter.ViewHolder> {
	private List<PicModle> listMembers;
	private Context context;

	public GroupMembersAdapter(List<PicModle> listMembers) {
		this.listMembers = listMembers;
	}

	@Override
	public int getItemCount() {
		return listMembers.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		context = viewGroup.getContext();
		View itemView = View.inflate(context, R.layout.adapter_group_members,
				null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		ImageLoaderUtil.getImageLoader().displayImage(
				DisplayUtils.getAbsolutelyUrl(listMembers.get(position)
						.getBarImgUrl()),
				viewHolder.ivHeader,
				ImageLoaderUtil.getOptions(R.drawable.img_loading_default_copy,
						R.drawable.img_loading_empty_copy,
						R.drawable.img_loading_fail_copy));
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		ImageView ivHeader;

		public ViewHolder(View itemView) {
			super(itemView);
			ivHeader = (ImageView) itemView.findViewById(R.id.ivHeader);
		}
	}

}
