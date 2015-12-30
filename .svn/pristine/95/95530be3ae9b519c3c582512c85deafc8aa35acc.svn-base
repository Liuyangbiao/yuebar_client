package com.fullteem.yueba.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fullteem.yueba.R;

;
/**
 * 自定义顶部布局控件
 * 
 * @author QiuXinlong
 * 
 */
public class HeaderBar extends RelativeLayout {
	private Context context;

	public Button back;
	private TextView top_title;
	public TextView top_right_btn;
	public ImageView top_right_img;

	public HeaderBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public HeaderBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public HeaderBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	@SuppressLint("InflateParams")
	public void initView(Context context) {
		this.context = context;
		View v = LayoutInflater.from(context).inflate(R.layout.headerbar, null);
		back = (Button) v.findViewById(R.id.btn_top_back);
		top_title = (TextView) v.findViewById(R.id.btn_top_title);
		top_right_btn = (TextView) v.findViewById(R.id.btn_top_right);
		top_right_img = (ImageView) v.findViewById(R.id.img_top_right);
		back.setOnClickListener(onClickListener);
		top_right_btn.setOnClickListener(onClickListener);
		top_right_img.setOnClickListener(onClickListener);
		// this.addView(v);
		@SuppressWarnings("deprecation")
		LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		this.addView(v, lp);
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_top_back:
				((Activity) context).finish();
				break;
			case R.id.btn_top_right:

				break;
			case R.id.img_top_right:
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 设置标题
	 * 
	 * @param title
	 *            标题内容
	 */
	public void setTitle(String title) {
		if (title != null) {
			top_title.setText(title);
		}
	}

	/**
	 * 设置是否显示左边返回按钮
	 * 
	 * @param isShow
	 */
	public void setShowLeft(boolean isShow) {
		if (isShow) {
			back.setVisibility(View.VISIBLE);
		} else {
			back.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置右边按钮
	 * 
	 * @param text
	 *            按钮内容
	 */
	public void setRightText(String text) {
		if (top_right_btn != null) {
			top_right_btn.setText(text);
		}
	}

	/**
	 * 设置右边区域为空
	 */
	public void setRightUnShow() {
		if (top_right_btn != null && top_right_img != null) {
			top_right_btn.setVisibility(View.GONE);
			top_right_img.setVisibility(View.GONE);
		}
	}

}
