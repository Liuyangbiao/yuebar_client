package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.ui.PerssonalInfoAcitivity;
import com.fullteem.yueba.model.NearbyPubFriendsModel.PubFriendsModel;
import com.fullteem.yueba.util.DisplayUtils;
import com.fullteem.yueba.util.StringUtils;
import com.fullteem.yueba.util.StringUtils.Gender;
import com.fullteem.yueba.widget.CircleImageView;

/**
 * @author ssy 附近吧友
 */
public class NearByFriendsListAdapter
		extends
		BaseDataAdapter.Build<PubFriendsModel, NearByFriendsListAdapter.ViewHolder> {
	private Context context;

	public NearByFriendsListAdapter(Activity context,
			List<PubFriendsModel> listData) {
		super(listData, R.drawable.img_loading_default,
				R.drawable.img_loading_empty, R.drawable.img_loading_fail);
		this.context = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View itemView = LayoutInflater.from(context).inflate(
				R.layout.adapter_barfriends_xlistview, null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.tvName.setText(listData.get(position).getUserName());

		if (listData.get(position).getDistance() == null
				|| "null"
						.equalsIgnoreCase(listData.get(position).getDistance())) {
			holder.tvSpace.setText("100米以内");
		} else {
			holder.tvSpace.setText(listData.get(position).getDistance());
		}
		String createDate = listData.get(position).getCreateDate();
		String showTime = createDate;
		if (TextUtils.isEmpty(createDate) == false && createDate.length() > 3) {
			showTime = createDate.substring(6, createDate.length() - 3);
		}

		holder.tvTime.setText(showTime);
		final int lastPosition = position;
		holder.ImgViewHeader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						PerssonalInfoAcitivity.class);
				intent.putExtra("userId", listData.get(lastPosition)
						.getUserId());
				context.startActivity(intent);

			}
		});

		if ("男".equalsIgnoreCase(listData.get(position).getUserSex())) {
			StringUtils.changeStyle(context, holder.tvSex, Gender.GENDER_BOY);
		} else {
			StringUtils.changeStyle(context, holder.tvSex, Gender.GENDER_GIRL);
		}
		holder.tvSex.setText(listData.get(position).getUserAge());
		holder.tvMood.setText(listData.get(position).getUserAsign());
		showImage(holder.ImgViewHeader,
				DisplayUtils.getAbsolutelyUrl(listData.get(position)
						.getUserLogoUrl(), R.drawable.img_loading_default));
	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		private CircleImageView ImgViewHeader;
		private TextView tvName;
		private TextView tvSpace;
		private TextView tvTime;
		private TextView tvSex;
		private TextView tvMood;

		// private TextView tvAge;

		public ViewHolder(View itemView) {
			super(itemView);
			ImgViewHeader = (CircleImageView) itemView
					.findViewById(R.id.ImgViewHeader);
			tvName = (TextView) itemView.findViewById(R.id.tvName);
			tvSpace = (TextView) itemView.findViewById(R.id.tvSpace);
			tvTime = (TextView) itemView.findViewById(R.id.tvTime);
			tvSex = (TextView) itemView.findViewById(R.id.tvSex);
			tvMood = (TextView) itemView.findViewById(R.id.tvMood);
		}
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		// TODO Auto-generated method stub

	}
}
