package com.fullteem.yueba.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月11日&emsp;下午4:43:49
 * @use 内容太长时滚动显示的Textview
 */
public class AutoRollTextView extends TextView {

	public AutoRollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AutoRollTextView(Context context) {
		this(context, null);
	}

	@Override
	public boolean isFocused() {
		return true;
	}

	private void init() {
		setSingleLine(true);
		setEllipsize(TextUtils.TruncateAt.MARQUEE);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setGravity(Gravity.LEFT);
	}
}
