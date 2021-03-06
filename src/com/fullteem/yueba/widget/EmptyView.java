package com.fullteem.yueba.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fullteem.yueba.R;
import com.fullteem.yueba.util.DisplayUtils;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月25日&emsp;下午6:02:26
 * @use ListView或者GridView没有内容时展示View
 */
public class EmptyView extends LinearLayout {
	private TextView tvState;

	public EmptyView(Context context) {
		this(context, null);
	}

	public EmptyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		this.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);

		LayoutParams lpChild = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lpChild.setMargins(0, 4, 0, 4);

		ImageView ico = new ImageView(context);
		ico.setBackgroundResource(R.drawable.ic_launcher);
		this.addView(ico, lpChild);

		tvState = new TextView(context);
		tvState.setTextColor(0xb9000000);
		tvState.getPaint().setFakeBoldText(true);
		tvState.setTextSize(DisplayUtils.dp2px(context, 8));
		tvState.setText(context.getString(R.string.thisNone));
		this.addView(tvState, lpChild);
	}

	public void setText(CharSequence text) {
		tvState.setText(text);
	}

}
