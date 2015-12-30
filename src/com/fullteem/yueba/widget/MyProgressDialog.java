package com.fullteem.yueba.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fullteem.yueba.R;

/**
 * 自定义加载弹框
 * 
 * @author QiuXinlong
 * 
 */
public class MyProgressDialog extends Dialog {
	private View view;
	private TextView tv_title;
	private TextView tv_divider;
	public TextView tv_value;
	private Context context;

	public MyProgressDialog(Context context) {
		super(context, R.style.MyDialogStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	/**
	 * 初始化默认无标题的progressDialog
	 * 
	 * @param context
	 *            Context
	 * @param cancelable
	 */
	public MyProgressDialog(Context context, boolean cancelable) {
		super(context, R.style.MyDialogStyle);
		this.context = context;
		initView();
		tv_title.setVisibility(View.GONE);
		tv_divider.setVisibility(View.GONE);
		setCancelable(cancelable);
	}

	/**
	 * 初始化无标题的progressDialog
	 * 
	 * @param context
	 *            Context
	 * @param msg
	 *            消息
	 * @param cancelable
	 */
	public MyProgressDialog(Context context, String msg, boolean cancelable) {
		super(context, R.style.MyDialogStyle);
		this.context = context;
		initView();
		tv_title.setVisibility(View.GONE);
		tv_divider.setVisibility(View.GONE);
		tv_value.setText(msg);
		setCancelable(cancelable);
	}

	/**
	 * 初始化有标题的progressDialog
	 * 
	 * @param context
	 *            Context
	 * @param title
	 *            标题
	 * @param msg
	 *            内容
	 * @param cancelable
	 */
	public MyProgressDialog(Context context, String title, String msg,
			boolean cancelable) {
		super(context, R.style.MyDialogStyle);
		this.context = context;
		initView();
		tv_title.setText(title);
		tv_divider.setVisibility(View.VISIBLE);
		tv_value.setText(msg);
		setCancelable(cancelable);
	}

	@SuppressLint("InflateParams")
	public void initView() {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.loadingbar, null);
		tv_title = (TextView) view.findViewById(R.id.pro_title);
		tv_value = (TextView) view.findViewById(R.id.pro_value);
		tv_divider = (TextView) view.findViewById(R.id.pro_divider);
		setContentView(view);
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		if (tv_title != null) {
			tv_title.setText(title);
			tv_divider.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置提醒消息
	 * 
	 * @param msg
	 */
	public void setMsg(String msg) {
		if (tv_value != null) {
			tv_value.setText(msg);
		}
	}
}
