package com.fullteem.yueba.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fullteem.yueba.R;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年12月26日&emsp;下午3:47:52
 * @use 提示内容Popupwindow
 */
public class HintContentPopWindow {
	private RelativeLayout rlParentView;
	private Activity mContext;
	private PopupWindow mPopupWindow;

	public HintContentPopWindow(Activity context) {
		this.mContext = context;
		this.rlParentView = (RelativeLayout) View.inflate(context,
				R.layout.popwindow_hint_content, null);
		mPopupWindow = new PopupWindow(rlParentView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
		mPopupWindow
				.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);

		rlParentView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPopupWindow != null && mPopupWindow.isShowing())
					mPopupWindow.dismiss();
			}
		});
		rlParentView.findViewById(R.id.llContent).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
	}

	/**
	 * @param btnText
	 *            null即使用xml指定的，""可起不显示作用
	 * @param onClickListener
	 */
	public HintContentPopWindow setBottomButton(String btnText,
			OnClickListener onClickListener) {
		Button btnOK = (Button) rlParentView.findViewById(R.id.btnOk);
		if (btnText != null)
			btnOK.setText(btnText);
		btnOK.setOnClickListener(onClickListener == null ? new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
			}
		}
				: onClickListener);
		return this;
	}

	/**
	 * @param content
	 *            null即使用xml指定的，""可起不显示作用
	 */
	public HintContentPopWindow setCenterText(CharSequence content) {
		if (content == null)
			return this;
		TextView tvContent = (TextView) rlParentView
				.findViewById(R.id.tvContent);
		tvContent.setText(content);
		return this;
	}

	/**
	 * 设置中间文本对齐方式
	 * 
	 * @param gravity
	 * @return
	 */
	public HintContentPopWindow setCenterGravity(int gravity) {
		((TextView) rlParentView.findViewById(R.id.tvContent))
				.setGravity(gravity);
		return this;
	}

	/**
	 * 设置中间展示视图
	 * 
	 * @return
	 */
	public HintContentPopWindow setCenterView(View view) {
		LinearLayout lLayout = (LinearLayout) rlParentView
				.findViewById(R.id.llCenterContent);
		lLayout.removeAllViews();
		lLayout.addView(view);
		return this;
	}

	/**
	 * 设置宽度
	 * 
	 * @param width
	 * @return
	 */
	public HintContentPopWindow setWidth(int width) {
		LinearLayout lLayout = (LinearLayout) rlParentView
				.findViewById(R.id.llContent);
		LayoutParams layoutParams = lLayout.getLayoutParams();
		layoutParams.width = width;
		lLayout.setLayoutParams(layoutParams);
		return this;
	}

	/**
	 * @param title
	 *            null即使用xml指定的，""可起不显示作用
	 */
	public HintContentPopWindow setTitle(String title) {
		if (title == null)
			return this;
		TextView tvContent = (TextView) rlParentView.findViewById(R.id.tvTitle);
		tvContent.setText(title);
		return this;
	}

	public PopupWindow getPopupWindow() {
		return mPopupWindow;
	}

	public HintContentPopWindow showWindow() {
		setBottomButton(null, null);
		mPopupWindow.showAtLocation(mContext.getWindow().getDecorView(),
				Gravity.CENTER, 0, 0);
		return this;
	}

	public HintContentPopWindow showWindow(OnClickListener onClickListener) {
		setBottomButton(null, onClickListener);
		mPopupWindow.showAtLocation(mContext.getWindow().getDecorView(),
				Gravity.CENTER, 0, 0);
		return this;
	}

	public HintContentPopWindow showWindow(CharSequence tvContent) {
		setCenterText(tvContent);
		setBottomButton(null, null);
		mPopupWindow.showAtLocation(mContext.getWindow().getDecorView(),
				Gravity.CENTER, 0, 0);
		return this;
	}

	public HintContentPopWindow showWindow(String title,
			CharSequence tvContent, String btnContent,
			OnClickListener onClickListener) {
		setTitle(title);
		setCenterText(tvContent);
		setBottomButton(btnContent, onClickListener);
		mPopupWindow.showAtLocation(mContext.getWindow().getDecorView(),
				Gravity.CENTER, 0, 0);
		return this;
	}

}
