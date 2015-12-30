package com.fullteem.yueba.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fullteem.yueba.R;

/**
 * @author jun
 * @version 1.0.0
 * @created 2014年1月5日&emsp;下午3:47:52
 * @use 带确认取消的提示Popupwindow
 */
public class HintConfirmationPopWindow {
	private RelativeLayout rlParentView;
	private Activity mContext;
	private PopupWindow mPopupWindow;

	public HintConfirmationPopWindow(Activity context) {
		this.mContext = context;
		this.rlParentView = (RelativeLayout) View.inflate(context,
				R.layout.popwindow_common, null);
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
	 * 设置确认按钮
	 * 
	 * @param btnText
	 *            null即使用xml指定的，""可起不显示作用
	 * @param onClickListener
	 */
	public HintConfirmationPopWindow setSureButton(String btnText,
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
	 * 设置取消按钮
	 * 
	 * @param btnText
	 *            null即使用xml指定的，""可起不显示作用
	 * @param onClickListener
	 */
	public HintConfirmationPopWindow setCancelButton(String btnText,
			OnClickListener onClickListener) {
		Button btnCancle = (Button) rlParentView.findViewById(R.id.btnCancle);
		if (btnText != null)
			btnCancle.setText(btnText);
		btnCancle
				.setOnClickListener(onClickListener == null ? new OnClickListener() {
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
	 * 设置确认按钮，可自定义确认按钮背景
	 * @param btnText
	 * @param resid
	 * @param onClickListener
	 * @return
	 */
	public HintConfirmationPopWindow setSureButton(String btnText,int resid,
			OnClickListener onClickListener) {
		Button btnOK = (Button) rlParentView.findViewById(R.id.btnOk);
		btnOK.setBackgroundResource(resid);
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
	 * 设置确认按钮，可自定义取消按钮背景
	 * @param btnText
	 * @param resid
	 * @param onClickListener
	 * @return
	 */
	public HintConfirmationPopWindow setCancelButton(String btnText,int resid,
			OnClickListener onClickListener) {
		Button btnCancle = (Button) rlParentView.findViewById(R.id.btnCancle);
		btnCancle.setBackgroundResource(resid);
		if (btnText != null)
			btnCancle.setText(btnText);
		btnCancle
				.setOnClickListener(onClickListener == null ? new OnClickListener() {
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
	public HintConfirmationPopWindow setCenterText(CharSequence content) {
		if (content == null)
			return this;
		TextView tvContent = (TextView) rlParentView.findViewById(R.id.Value);
		tvContent.setText(content);
		return this;
	}

	/**
	 * 设置中间文本对齐方式
	 * 
	 * @param gravity
	 * @return
	 */
	public HintConfirmationPopWindow setCenterGravity(int gravity) {
		((TextView) rlParentView.findViewById(R.id.Value)).setGravity(gravity);
		return this;
	}

	/**
	 * 设置中间展示视图
	 * 
	 * @return
	 */
	public HintConfirmationPopWindow setCenterView(View view) {
		RelativeLayout lLayout = (RelativeLayout) rlParentView
				.findViewById(R.id.rlContent);
		lLayout.removeAllViews();
		lLayout.addView(view);
		return this;
	}

	/**
	 * 底部按钮可视情况
	 * 
	 * @param visibility
	 * @return
	 */
	public HintConfirmationPopWindow setBottomButtonVisibility(int visibility) {
		rlParentView.findViewById(R.id.rlBottomBtn).setVisibility(visibility);
		return this;
	}

	public TextView getCenterTextView() {
		return (TextView) rlParentView.findViewById(R.id.Value);
	}

	/**
	 * @param title
	 *            null即使用xml指定的，""可起不显示作用
	 */
	public HintConfirmationPopWindow setTitle(String title) {
		if (title == null)
			return this;
		TextView tvContent = (TextView) rlParentView.findViewById(R.id.title);
		tvContent.setText(title);
		return this;
	}

	public PopupWindow getPopupWindow() {
		return mPopupWindow;
	}

	public HintConfirmationPopWindow showWindow() {
		showWindow(null);
		return this;
	}

	public HintConfirmationPopWindow showWindow(CharSequence tvContent) {
		setCenterText(tvContent);
		mPopupWindow.showAtLocation(mContext.getWindow().getDecorView(),Gravity.CENTER, 0, 0);
		return this;
	}

	public HintConfirmationPopWindow showWindow(String title,
			CharSequence tvContent, String btnSureContent,
			OnClickListener onSureClickListener, String btnCancelContent,
			OnClickListener onCancelClickListener) {
		setTitle(title);
		setCenterText(tvContent);
		setSureButton(btnSureContent, onSureClickListener);
		setCancelButton(btnCancelContent, onCancelClickListener);
		mPopupWindow.showAtLocation(mContext.getWindow().getDecorView(),
				Gravity.CENTER, 0, 0);
		return this;
	}

	public void disMissWindow() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

}
