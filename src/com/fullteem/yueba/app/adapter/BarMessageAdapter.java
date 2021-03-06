package com.fullteem.yueba.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.fullteem.yueba.R;
import com.fullteem.yueba.engine.push.MessageUtil;
import com.fullteem.yueba.model.BarChatModel;
import com.fullteem.yueba.util.LogUtil;
import com.fullteem.yueba.util.SmileUtils;

public class BarMessageAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<BarChatModel> chatList;

	public BarMessageAdapter(Context context, List<BarChatModel> chatList) {
		this.chatList = chatList;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	/**
	 * 获取item数
	 */
	@Override
	public int getCount() {
		return chatList.size();
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		notifyDataSetChanged();
	}

	@Override
	public BarChatModel getItem(int position) {
		return chatList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final BarChatModel message = getItem(position);
		int chatType = message.getMsgType();

		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.row_bar_chat_message, null);
			if (convertView == null) {
				LogUtil.printPushLog("failed to get view");
				return null;
			}

			holder.userName = (TextView) convertView
					.findViewById(R.id.userName);

			holder.timestamp = (TextView) convertView
					.findViewById(R.id.timestamp);

			if (chatType == MessageUtil.MESSAGE_TYPE_TXT) {
				holder.content = (TextView) convertView
						.findViewById(R.id.chatcontent);
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// set values
		holder.userName.setText(message.getUserName());

		String str = message.getMsgContent();
		holder.content.setText(SmileUtils.getSmiledText(context, str),
				BufferType.SPANNABLE);

		holder.timestamp.setText(message.getCreateDate());

		return convertView;
	}

	public static class ViewHolder {
		// CircleImageView headImg;// user head image
		TextView userName;
		TextView content;// message body
		TextView timestamp;// time

	}

}