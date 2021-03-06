package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.model.PerssonalVisitorModel;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月11日&emsp;下午9:54:21
 * @use 详细资料页面访客 Adapter
 */
public class PerssonalVisitorAdapter extends
		RecyclerView.Adapter<PerssonalVisitorAdapter.ViewHolder> {
	private List<PerssonalVisitorModel> listVisitor;

	public PerssonalVisitorAdapter(List<PerssonalVisitorModel> listVisitor) {
		this.listVisitor = listVisitor;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_perssonal_visitor, null);
		ViewHolder mHolder = new ViewHolder(itemView);
		return mHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		ImageLoaderUtil.getImageLoader().displayImage(
				DisplayUtils.getAbsolutelyUrl(listVisitor.get(position)
						.getVisitorHeader()), viewHolder.ivVisitorHeader);
		viewHolder.tvVisitor.setText(listVisitor.get(position)
				.getVisitorNickname());
	}

	@Override
	public int getItemCount() {
		return listVisitor.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		ImageView ivVisitorHeader;
		TextView tvVisitor;

		public ViewHolder(View itemView) {
			super(itemView);
			ivVisitorHeader = (ImageView) itemView
					.findViewById(R.id.iv_visitorHeader);
			tvVisitor = (TextView) itemView.findViewById(R.id.tv_visitor);
		}
	}
}
