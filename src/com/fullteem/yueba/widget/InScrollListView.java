package com.fullteem.yueba.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义ListView，便于镶嵌在scrollview里
 * 
 * @author ssy
 * 
 */
public class InScrollListView extends ListView {

	public InScrollListView(Context context) {
		super(context);
	}

	public InScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}