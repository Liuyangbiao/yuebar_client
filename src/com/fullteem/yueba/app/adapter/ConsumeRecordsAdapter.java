package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.model.ConsumeRecordsModel;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月10日&emsp;上午11:29:07
 * @use 消费记录Adapter
 */
public class ConsumeRecordsAdapter extends BaseAdapter {
	private List<ConsumeRecordsModel> listConsumeRecords;

	public ConsumeRecordsAdapter(List<ConsumeRecordsModel> listConsumeRecords) {
		this.listConsumeRecords = listConsumeRecords;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = View.inflate(parent.getContext(),
					R.layout.adapter_consume_records_listview, null);
			mHolder.tvConsumeDate = (TextView) convertView
					.findViewById(R.id.tv_consumeDate);
			mHolder.tvConsumeContent = (TextView) convertView
					.findViewById(R.id.tv_consumeContent);
			mHolder.choseColor = new ForegroundColorSpan(parent.getContext()
					.getResources().getColor(R.color.girl_red));
			convertView.setTag(mHolder);
		} else
			mHolder = (ViewHolder) convertView.getTag();

		mHolder.tvConsumeDate.setText(listConsumeRecords.get(position)
				.getConsumeDate());
		SpannableStringBuilder builder = new SpannableStringBuilder(parent
				.getContext().getString(R.string.consume_sum)
				+ listConsumeRecords.get(position).getConsumeContent()
				+ parent.getContext().getString(R.string.yuan));
		builder.setSpan(mHolder.choseColor, 4, builder.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mHolder.tvConsumeContent.setText(builder);

		return convertView;
	}

	@Override
	public int getCount() {
		return listConsumeRecords.size();
	}

	@Override
	public Object getItem(int position) {
		return listConsumeRecords.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		TextView tvConsumeDate; // 消费时间
		TextView tvConsumeContent; // 消费内容
		ForegroundColorSpan choseColor;
	}

}
