package com.fullteem.yueba.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.fullteem.yueba.R;

public class ShowMoreListview extends ListView {

	// 最多展示3条信息
	public static final int MAX_COUNT = 3;
	public static boolean isLoadMore;
	Context context;
	BaseAdapter adapter;
	View view;

	public ShowMoreListview(Context context) {
		super(context);
		view = LayoutInflater.from(context).inflate(R.layout.widght_footview,
				null);
	}

	public ShowMoreListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ShowMoreListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	public void addFooter(int Size, View v) {
		if (Size > MAX_COUNT) {
			isLoadMore = true;
		} else {
			isLoadMore = false;
		}
		addFooterView(v);
	}

	/**
	 * 此处必须要调用
	 * 
	 * @param <T>
	 * 
	 * @param adapter
	 */
	public void initListAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
		setAdapter(adapter);
	}

	public void setFooterVisible(View v) {
		v.setVisibility(View.GONE);
	}
}
