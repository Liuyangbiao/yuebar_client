package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.model.GiftModel;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.ImageLoaderUtil;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月11日&emsp;下午9:54:21
 * @use 详细资料页面礼物 Adapter
 */
public class PerssonalGiftAdapter extends
		RecyclerView.Adapter<PerssonalGiftAdapter.ViewHolder> {
	private List<GiftModel> listGift;

	public PerssonalGiftAdapter(List<GiftModel> listGift) {
		this.listGift = listGift;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_perssonal_gift, null);
		ViewHolder mHolder = new ViewHolder(itemView);
		return mHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		ImageLoaderUtil.getImageLoader().displayImage(
				DisplayUtils.getAbsolutelyUrl(listGift.get(position)
						.getGiftLogoUrl()), viewHolder.ivGiftHeader);
		viewHolder.tvGift.setText(listGift.get(position).getTotal() + "");
	}

	@Override
	public int getItemCount() {
		return listGift.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		ImageView ivGiftHeader;
		TextView tvGift;

		public ViewHolder(View itemView) {
			super(itemView);
			ivGiftHeader = (ImageView) itemView
					.findViewById(R.id.iv_giftHeader);
			tvGift = (TextView) itemView.findViewById(R.id.tv_gift);
		}

	}
}
