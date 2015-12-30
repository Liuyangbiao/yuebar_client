package com.fullteem.yueba.widget;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fullteem.yueba.R;

public class ListPopupWindow {
	private ListView popuplistview;
	private PopupWindow popupListsign;
	private popuplva adapter;
	private Context context;
	private List<String> popupStr;// 数据
	private ISettext msettext;// 回调接口
	private int showWidth, showHeight;// popupdown显示的宽高

	public ListPopupWindow(Context context, List<String> popupStr,
			ISettext msettext, int showWidth, int showHeight) {
		this.context = context;
		this.popupStr = popupStr;
		this.msettext = msettext;
		if (showWidth > 0)
			this.showWidth = showWidth;
		else
			this.showWidth = 100;
		if (showHeight > 0)
			this.showHeight = showHeight;
		else
			this.showHeight = this.showWidth;
	}

	public PopupWindow initPopupWindowList() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.popwindow_list, null);
		popuplistview = (ListView) view.findViewById(R.id.lv_popupwdList);
		adapter = new popuplva();
		popuplistview.setAdapter(adapter);
		popuplistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (msettext != null) {
					msettext.settext(popupStr.get(position), position);
				}
				popupListsign.dismiss();
			}
		});
		// popupListsign = new PopupWindow(view, showWidth, showHeight);
		popupListsign = new PopupWindow(view, showWidth,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		popupListsign
				.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupListsign.setTouchable(true);
		popupListsign.setOutsideTouchable(true);
		popupListsign.setFocusable(true);
		return popupListsign;
	}

	public static interface ISettext {
		public void settext(String str, int positon);
	}

	private class popuplva extends BaseAdapter {

		@Override
		public int getCount() {

			return popupStr.size();
		}

		@Override
		public Object getItem(int position) {
			return popupStr.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv;
			if (convertView == null) {
				tv = new TextView(context);
				convertView = new LinearLayout(context);

				tv.setPadding(4, 5, 4, 5);
				tv.setSingleLine(true);
				tv.setTextColor(0xbf101010);
				tv.setTextSize(14);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.CENTER;
				tv.setLayoutParams(lp);
				tv.setGravity(Gravity.CENTER);

				((LinearLayout) convertView)
						.setOrientation(LinearLayout.VERTICAL);
				((LinearLayout) convertView).addView(tv);

				convertView.setTag(tv);
			} else
				tv = (TextView) convertView.getTag();
			tv.setText(popupStr.get(position));
			return convertView;
		}
	}

	public void notifyDataSetChange(List<String> list) {
		popupStr.clear();
		popupStr.addAll(list);
		adapter.notifyDataSetChanged();
	}
}
