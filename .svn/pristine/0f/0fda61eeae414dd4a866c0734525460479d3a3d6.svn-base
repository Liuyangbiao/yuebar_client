package com.fullteem.yueba.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fullteem.yueba.R;

public class MessageMenuAdapter extends BaseAdapter {
	private String[] strList;
	private Context context;

	public MessageMenuAdapter(Context context, String[] strList) {
		this.strList = strList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strList.length;
	}

	@Override
	public Object getItem(int position) {
		return strList[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.adapter_msgmenu_listview, null);
		}
		tv = (TextView) convertView.findViewById(R.id.TvMsgMenu);
		tv.setText(strList[position]);
		return convertView;
	}

}
