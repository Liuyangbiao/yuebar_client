package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.app.adapter.BaseDataAdapter.ImageType;
import com.fullteem.yueba.model.RechargRecordsModel;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月9日&emsp;上午9:41:49
 * @use 充值记录adapter
 */
public class RechargRecordsAdapter
		extends
		BaseDataAdapter.Build<RechargRecordsModel, RechargRecordsAdapter.ViewHolder> {

	public enum RechargRecordsType {
		NONSHOWDATE, SHOWDATE
	}

	private List<RechargRecordsModel> listRechargRecords;
	private RechargRecordsType rechargRecordsType;

	public RechargRecordsAdapter(List<RechargRecordsModel> listRechargRecords,
			RechargRecordsType rechargRecordsType) {
		super(listRechargRecords, ImageType.NO_IMAGE);
		this.listRechargRecords = listRechargRecords;
		this.rechargRecordsType = rechargRecordsType;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View itemView = View.inflate(viewGroup.getContext(),
				R.layout.adapter_recharg_records_listview, null);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if (rechargRecordsType == RechargRecordsType.NONSHOWDATE) {
			holder.tvRecordsDate.setVisibility(View.GONE);
		} else {
			holder.tvRecordsDate.setVisibility(View.VISIBLE);
		}
		holder.tvRecordsContent.setText(listRechargRecords.get(position)
				.getRecordsContent());
		holder.tvRecordsDate.setText(listRechargRecords.get(position)
				.getRecordsDate());

	}

	class ViewHolder extends BaseDataAdapter.ViewHolder {
		TextView tvRecordsContent; // 记录详情
		TextView tvRecordsDate;// 记录日期

		public ViewHolder(View itemView) {
			super(itemView);
			tvRecordsContent = (TextView) itemView
					.findViewById(R.id.tv_recordsContent);
			tvRecordsDate = (TextView) itemView
					.findViewById(R.id.tv_recordsDate);
		}
	}
}
