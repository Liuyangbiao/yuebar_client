package com.fullteem.yueba.widget;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fullteem.yueba.R;

public class CommonPopWindow {

	private PopupWindow popupWindow;
	private Activity mParent;
	private OnClickListener rightOnclick, leftOnclick;
	private String msg;
	private boolean isEditText;
	private EditText editText;
	private TextView textView;

	public CommonPopWindow(Activity mParent, String msg,
			OnClickListener leftOnclick, OnClickListener rightOnclick) {
		this.mParent = mParent;
		this.rightOnclick = rightOnclick;
		this.leftOnclick = leftOnclick;
		this.msg = msg;
	}

	/**
	 * 必须在调用show之后
	 * 
	 * @return
	 */
	private View initMenuView() {
		View menuView = mParent.getLayoutInflater().inflate(
				R.layout.popwindow_common, null);
		menuView.findViewById(R.id.btnOk).setOnClickListener(leftOnclick);
		menuView.findViewById(R.id.btnCancle).setOnClickListener(rightOnclick);
		editText = (EditText) menuView.findViewById(R.id.edtEditText);
		textView = (TextView) menuView.findViewById(R.id.Value);
		if (isEditText) {
			editText.setVisibility(View.VISIBLE);
			textView.setVisibility(View.GONE);
		} else {
			editText.setVisibility(View.GONE);
			textView.setVisibility(View.VISIBLE);
			textView.setText(msg);
		}
		return menuView;
	}

	/**
	 * 返回View，必须调用
	 * 
	 * @return
	 */
	public PopupWindow getMenu() {
		return popupWindow;
	}

	/**
	 * 是否edit框
	 * 
	 * @param isEditText
	 */
	public void setIsEditText(boolean isEditText) {
		this.isEditText = isEditText;

	}

	/**
	 * 返回edittext,必须在setIsEditText为true的情况下生效
	 * 
	 * @return
	 */
	public EditText getEditText() {
		return editText;
	}

	/**
	 * 展示前的准备，必须调用
	 */
	public void preperShow() {
		View menu = initMenuView();
		popupWindow = new PopupWindow(menu);
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(LayoutParams.MATCH_PARENT);
		popupWindow.setAnimationStyle(R.style.PopupAnimation);
	}

}
